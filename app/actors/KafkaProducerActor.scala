package actors

import akka.NotUsed
import akka.actor.{Actor, ActorSystem}
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.{BroadcastHub, Keep, Sink, Source}
import controllers.NumRecords
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

class KafkaProducerActor(hubSink: Sink[String, NotUsed]) extends Actor {

  import KafkaProducerClass._

  // define ActorSystem and Materializer for akka streams
  implicit val system = ActorSystem("Kafka")
  val logger = play.api.Logger(getClass).logger

  // kafka producer config/settings
  val producerConfig = system.settings.config.getConfig("akka.kafka.producer")
  var producerSettings = None: Option[ProducerSettings[String, String]]
  var topic = None: Option[String]
  var messageSink = None: Option[Sink[String, NotUsed]]
  var numRecords = None: Option[NumRecords]

  val defaultProducerSettings: ProducerSettings[String, String] =
    ProducerSettings(producerConfig, new StringSerializer, new StringSerializer)
      .withBootstrapServers("localhost:9092")
  val defaultSink = Sink.ignore
  val defaultNumrecords = NumRecords(0L, 0L)

  def receive: Receive = {
    case InitProducer(bootStrapServer: String, records: NumRecords, hubSink: Sink[String, NotUsed]) => {
      producerSettings = Some(ProducerSettings(producerConfig, new StringSerializer, new StringSerializer)
        .withBootstrapServers(bootStrapServer))
      messageSink = Some(hubSink)
      numRecords = Some(records)
    }
    case ProducerMessage(topic, key, value) => {
      // logger.info(s"--> $topic  $key  $value")
      val webSocketSink = messageSink.getOrElse(defaultSink)

      val messageSource: Source[ProducerRecord[String, String], NotUsed] =
        Source.single(ProducerMessage(topic, key, value)).map(msg => {
          new ProducerRecord[String, String](topic, 0, msg.key, msg.value)
        }).toMat(BroadcastHub.sink(bufferSize = 1))(Keep.right).run()

      // send Producer messages to Kafka Producer
      messageSource.runWith(Producer.plainSink(producerSettings.getOrElse(defaultProducerSettings)))
      // send Producer messages to hubSink to websocket
      messageSource.map(msg => {
        val formatValue = msg.value.substring(1, msg.value.length - 1).replaceAll(",", ", ").replaceAll(":", ": ")
        s"producer;$formatValue"
      })
        .runWith(webSocketSink)
      // send message summary status to websocket
      messageSource.map(_ => {
        val num = numRecords.getOrElse(defaultNumrecords)
        num.numProducerRecords += 1
        s"status;Producer Records Sent: ${num.numProducerRecords} -- Consumer Records Received: ${num.numConsumerRecords}"
      }).runWith(webSocketSink)
      // output data records to logger
      messageSource.map(msg => logger.info(msg.key + " -- " + msg.value)).runWith(Sink.ignore)
    }
    case ProducerTerminate => {
      system.terminate()
      println("System terminate for Kafka Producer...")
    }
    case _ => println("KafkaProducer received something unexpected... No action taken...")
  }
}

object KafkaProducerClass {

  case class InitProducer(bootStrapServer: String, records: NumRecords, hubSink: Sink[String, NotUsed])

  case class ProducerMessage(topic: String, key: String, value: String)

  case object ProducerTerminate

}