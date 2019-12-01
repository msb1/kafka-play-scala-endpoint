package controllers

import actors.DataRecord.EpdSimActor
import actors.DataRecord.SimClass.{InitSimulator, StartSimulator, StopSimulator}
import actors.KafkaProducerClass.InitProducer
import actors.{DataRecord, KafkaProducerActor}
import akka.actor.{ActorSystem, Props, Terminated}
import akka.stream.Materializer
import akka.stream.scaladsl.{BroadcastHub, Flow, Keep, MergeHub, Sink, Source}
import akka.{Done, NotUsed}
import ch.qos.logback.classic.{Level, Logger}
import controllers.KafkaConfigForm.ConfigData
import javax.inject.Inject
import org.slf4j.LoggerFactory
import play.api.data._
import play.api.http.websocket._
import play.api.mvc._

import scala.concurrent.Future


case class NumRecords(var numProducerRecords: Long, var numConsumerRecords: Long)

class HomeController @Inject()(cc: MessagesControllerComponents)(implicit system: ActorSystem, mat: Materializer)
  extends MessagesAbstractController(cc) {

  // set org, akka and kafka Logger levels to WARN (to avoid excess verbosity)
  LoggerFactory.getLogger("org").asInstanceOf[Logger].setLevel(Level.WARN)
  LoggerFactory.getLogger("akka").asInstanceOf[Logger].setLevel(Level.WARN)
  LoggerFactory.getLogger("kafka").asInstanceOf[Logger].setLevel(Level.WARN)

  // start play logger (logback with slf4j)
  val logger = play.api.Logger(getClass).logger
  private val postUrl = routes.HomeController.kafkaConfigPost()

  // Create actors for Kafka Producer and EPD Simulator
  logger.info("Start Kafka Producer Stream")
  val kafkaProducer = system.actorOf(Props(new KafkaProducerActor(hubSink)), "kafkaProducer")
  val simulate = system.actorOf(Props(new EpdSimActor(kafkaProducer)), "simulator")

  // Read in simulator configuration
  val filePath = "/home/bw/scala/Misc/kafkaTest/"
  logger.info("Kafka Akka Test program...")
  val epdConfig = DataRecord.readConfigData(filePath + "epd.json")

  // Create hubSink and hubSource for feeding websocket - messages all go to runwith(hubSink)
  val (hubSink, hubSource) =
    MergeHub.source[String].toMat(BroadcastHub.sink[String])(Keep.both).run()

  var kafkaConfig = None: Option[ConfigData]
  val numRecords = NumRecords(0L, 0L)

  // Create messageSink for messages received from websockets where message is two parts separated by ';'
  // Each message has a header and a body
  val messageSink: Sink[String, Future[Done]] = Sink.foreach { wsMessage =>
    // logger.info(s"Message received by Sink: $wsMessage")
    val msg = wsMessage.split(";")
    if (msg(0) == "controlMessage") {
      msg(1).trim match {
        case "startSimulator" => {
          logger.info("Simulator started...")
          val config = kafkaConfig.getOrElse(KafkaConfigForm.getDefaultConfig)
          kafkaProducer ! InitProducer(config.bootstrapServer, numRecords, hubSink)
          simulate ! InitSimulator(config.producerTopic, epdConfig)
          KafkaConsumer.initConsumer(config, numRecords, hubSink)
          KafkaConsumer.runConsumer
          simulate ! StartSimulator
          sendMessagetoWebSocket("status;Simulator Started", hubSink)
        }
        case "stopSimulator" => {
          logger.info("Simulator stopped...")
          simulate ! StopSimulator
        }
        case "closeSimulator" => {
          simulate ! Terminated
          logger.info("Simulator closed...")
        }
        case _ => {
          logger.info("Message received - unrecognizable...")
        }
      }
    }
  }

  // Start (login) view
  def index() = Action { implicit request: MessagesRequest[AnyContent] =>
    logger.info("Home page login with app startup...")
    Ok(views.html.start(KafkaConfigForm.form, postUrl))
  }

  // Get request from Start view
  def kafkaConfigGet() = Action { implicit request: MessagesRequest[AnyContent] =>
    logger.info("Home page from REST GET... Currently unused...")
    Ok(views.html.start(KafkaConfigForm.form, postUrl))
  }

  // Post request from Start view - accepts form data with Kafka Config
  def kafkaConfigPost() = Action { implicit request: MessagesRequest[AnyContent] =>
    val errorFunction = { formWithErrors: Form[KafkaConfigForm.ConfigData] =>
      logger.error("Home page login form POST submit with errors...")
      BadRequest(views.html.start(formWithErrors, postUrl))
    }
    val successFunction = { config: ConfigData =>
      val configString = s"Sim Name: ${config.epdSimName}, Bootstrap Server: ${config.bootstrapServer}, " +
        s"Producer Topic: ${config.producerTopic}, Consumer Topic: ${config.consumerTopic}, Group Id: ${config.groupId}"
      kafkaConfig = Some(config)
      logger.info("Home page login successful...")
      logger.info(s"Kafka Config: $configString")
      Ok(views.html.epdsim(config))
    }
    KafkaConfigForm.form.bindFromRequest.fold(errorFunction, successFunction)
  }

  implicit val transformer: WebSocket.MessageFlowTransformer[Message, Message] = WebSocket.MessageFlowTransformer.identityMessageFlowTransformer
  val userFlow: Flow[Message, Message, NotUsed] =
    Flow[Message].mapAsync(1) {
      case TextMessage(text) => Future.successful(text)
      case BinaryMessage(data) => Future.successful(data.utf8String)
      case PingMessage(data) => Future.successful(data.utf8String)
      case PongMessage(data) => Future.successful(data.utf8String)
      case CloseMessage(statusCode, reason) => Future.successful(s"${statusCode.getOrElse(-1)} -- Websocket closed: $reason")
    }.via(Flow.fromSinkAndSource(messageSink, hubSource))
      .map[Message](string => TextMessage(string))

  // Basic Play WebSockets based on userFlow - no origin check
  // def ws: WebSocket = WebSocket.accept[Message, Message] { request =>
  //   userFlow
  // }

  // Play WebSockets based on userFlow - origin check enabled
  def ws: WebSocket = WebSocket.acceptOrResult[Message, Message] {request =>
    Future.successful(request.headers.get("Origin") match {
      case Some(originValue) if originMatches(originValue) => {
        logger.info(s"originCheck: originValue = $originValue")
        sendMessagetoWebSocket(s"status;originCheck: originValue = $originValue", hubSink)
        Right(userFlow)
      }
      case Some(badOrigin) => {
        logger.error(s"originCheck: rejecting request because Origin header value ${badOrigin} is not in the same origin")
        sendMessagetoWebSocket("error;*** ORIGIN CHECK FAILED ***", hubSink)
        Left(Forbidden)
      }
      case None => {
        logger.error("originCheck: rejecting request because no Origin header found")
        sendMessagetoWebSocket("error;*** ORIGIN HEADER NOT FOUND - FAILED ***", hubSink)
        Left(Forbidden)
      }
    })
  }

  def sendMessagetoWebSocket(msg: String, hubSink: Sink[String, NotUsed]): Unit = {
    Source.single(msg).runWith(hubSink)
  }

  // Returns true if the value of the Origin header contains an acceptable value.
  // Production done through configuration same as the allowedhosts filter.
  def originMatches(origin: String): Boolean = {
    origin.contains("localhost:9000") || origin.contains("localhost:19001")
  }
}

