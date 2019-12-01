package controllers

import akka.NotUsed
import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ClosedShape
import akka.stream.scaladsl._
import controllers.KafkaConfigForm.ConfigData
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer

object KafkaConsumer {

  // define ActorSystem and Materializer for akka streams
  implicit val system = ActorSystem("Kafka")

  val logger = play.api.Logger(getClass).logger

  // config for akka Kafka Consumer
  val consumerConfig = system.settings.config.getConfig("akka.kafka.consumer")
  var consumerSettings = None:Option[ConsumerSettings[String, String]]
  var topicSet = None:Option[Set[String]]

  val defaultConsumerSettings = ConsumerSettings(consumerConfig, new StringDeserializer, new StringDeserializer)
    .withBootstrapServers("localhost:9092")
    .withGroupId("group1")

  var messageSink = None:Option[Sink[String, NotUsed]]
  val defaultSink = Sink.ignore
  var numRecords = None:Option[NumRecords]
  val defaultNumrecords = NumRecords(0L, 0L)

  def initConsumer(config: ConfigData, records: NumRecords, hubSink: Sink[String,NotUsed]): Unit = {
    consumerSettings = Some(ConsumerSettings(consumerConfig, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers(config.bootstrapServer)
      .withGroupId(config.groupId))
    topicSet = Some(Set(config.consumerTopic))
    messageSink = Some(hubSink)
    numRecords = Some(records)
  }
  // implement Runnable Graph to split Kafka Consumer Source to Print Console, Websocket, MongoDB, etc...
  def runConsumer: Unit = {

    val consumerGraph = GraphDSL.create() { implicit builder =>
      import GraphDSL.Implicits._

      val webSocketSink = messageSink.getOrElse(defaultSink)
      // Kafka consumer to akka reactive stream
      val controlSource = Consumer.plainSource(consumerSettings.getOrElse(defaultConsumerSettings), Subscriptions.topics(topicSet.getOrElse(Set("ctopic"))))
      // Create printSink to logger (or console)
      val printSink = Sink.foreach[ConsumerRecord[String, String]](rec => {
        logger.info(s"Consumer: topic = ${rec.topic}, partition = ${rec.partition}, offset = ${rec.offset}, key = ${rec.key} -- ${rec.value}")
      })
      // Convert consumer messages to strings to be sent to websocket
      val messageConvert = Flow[ConsumerRecord[String, String]].map(msg => {
        val formatValue = msg.value.substring(1, msg.value.length - 1).replaceAll(",", ", ").replaceAll(":", ": ")
        // logger.info(s"--> $formatValue")
        s"consumer;$formatValue"
      })
      // Stream message summary status with each consumer message received
      val statusConvert = Flow[ConsumerRecord[String, String]].map(_ => {
        val num = numRecords.getOrElse(defaultNumrecords)
        num.numConsumerRecords += 1
        s"status;Producer Records Sent: ${num.numProducerRecords} -- Consumer Records Received: ${num.numConsumerRecords}"
      })

      // use broadcaster to branch out reactive streams to multiple sinks
      val broadcaster = builder.add(Broadcast[ConsumerRecord[String, String]](3))
      controlSource ~> broadcaster.in
      broadcaster.out(0) ~> printSink
      broadcaster.out(1) ~> messageConvert ~> webSocketSink
      broadcaster.out(2) ~> statusConvert ~> webSocketSink
      ClosedShape
    }
    RunnableGraph.fromGraph(consumerGraph).run
  }
}


