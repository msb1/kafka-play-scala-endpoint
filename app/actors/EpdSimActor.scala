package actors

import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar

import actors.KafkaProducerClass.{ProducerMessage, ProducerTerminate}
import akka.actor.{Actor, ActorRef, Terminated}

import scala.io._
import scala.math._
import scala.collection.mutable
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}

/**
 * Data Record Endpoint (EPD) Simulator with Sensor, Category and Result Fields
 */

case class EpdConfig(@JsonProperty("simulator") Simulator: Boolean, // True = run simulator
                     @JsonProperty("errorRate") ErrRate: Double, // error rate for category (switch threshold to opposite)
                     @JsonProperty("successRate") Success: Double, // success rate (fraction of one value outputs for simulator)
                     @JsonProperty("categories") Cat: List[Category],
                     @JsonProperty("sensors") Sens: List[Sensor])

// Category defines category parameters
case class Category(@JsonProperty("name") Label: String, // name of category parameter
                    @JsonProperty("levels") Level: List[String], // all levels defined as strings (even numbers)
                    @JsonProperty("oneThresholds") OneThreshold: List[Double], // thresholds for uniform [0,1] rv simulator zero output
                    @JsonProperty("zeroThresholds") ZeroThreshold: List[Double]) // thresholds for uniform [0,1] rv simulator one output

// Sensor class defines a sensor
// first four parameters define sensor; remaining parameters are for simulator
// sensor simulators are as follows:
//  	First sublist entry (case)
//            0 = two means correlated with two class (normal distributions)
//            1 = two means anti-correlated with  two class (normal distributions)
//            2 = one mean -- no correlation (normal distribution) - use only zero mean and std dev
//            3 = uniformly distributed [0, 1] with no correlation
//      The scale factor multiplies the rv for the output
//		Two output classes are assumed (0 and 1 or pass and fail)
case class Sensor(@JsonProperty("name") Label: String, // name of sensor parameter
                  @JsonProperty("simulatorType") SimType: Int, // simulator type
                  @JsonProperty("scale") Scale: Double, // scale of simulator
                  @JsonProperty("upperLimit") UpperLimit: Double, // max value for sensor
                  @JsonProperty("lowerLimit") LowerLimit: Double, // min value for sensor
                  @JsonProperty("upperControl") UpperControl: Double, // upper warning or control for sensor
                  @JsonProperty("lowerControl") LowerControl: Double, // lower warning or control for sensor
                  @JsonProperty("oneMean") OneMean: Double, // mean of one output simulator
                  @JsonProperty("oneStdDev") OneStdDev: Double, // std dev of one output simulator
                  @JsonProperty("zeroMean") ZeroMean: Double, // mean of zero output simulator
                  @JsonProperty("zeroStdDev") ZeroStdDev: Double) // std dev of zero output simulator


class EpdData {
  var CurrentTime: String = _
  var Topic: String = _
  var Categories: mutable.HashMap[String, String] = mutable.HashMap.empty[String, String]
  var Sensors: mutable.HashMap[String, Double] = mutable.HashMap.empty[String, Double]
  var Result: Int = _
  private val timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def this(topic: String) {
    this();
    this.Topic = topic
    this.CurrentTime = timeFormat.format(Calendar.getInstance.getTime)
  }
}

object DataRecord {

  val logger = play.api.Logger(getClass).logger

  def readConfigData(epdConf: String): EpdConfig = {
    val filename = epdConf
    // read config file
    logger.info(s"Reading ${filename} ...")
    val json = Source.fromFile(filename)
    // map - deserialize
    val epdConfig = jsonToType[EpdConfig](json.getLines.mkString)
    json.close
    return epdConfig
  }

  def jsonToType[T](json: String)(implicit m: Manifest[T]): T = {
    val objectMapper = new ObjectMapper() with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)
    objectMapper.readValue[T](json)
  }

  def makeSimulatedRecord(config: EpdConfig, topic: String): String = {
    val random = new java.util.Random()
    // initialize epdData record
    val epd = new EpdData(topic)

    // determine output (1 or 0) for data record
    if (random.nextFloat() < config.Success) {
      epd.Result = 1
    } else {
      epd.Result = 0
    }

    // generate sensor endpoint (numerics) simulated data
    for (s <- config.Sens) {
      // use switch to determine simulator type
      //        var output = 0.0
      val output = s.SimType match {
        case 0 if epd.Result == 1 => random.nextGaussian() * s.OneStdDev + s.OneMean
        case 0 if epd.Result == 0 => random.nextGaussian() * s.ZeroStdDev + s.ZeroMean
        case 1 if epd.Result == 1 => random.nextGaussian() * s.ZeroStdDev + s.ZeroMean
        case 1 if epd.Result == 0 => random.nextGaussian() * s.OneStdDev + s.OneMean
        case 2 => random.nextGaussian() * s.OneStdDev + s.OneMean
        case 3 => random.nextFloat()
        case _ => -1
      }
      if (output != -1) {
        epd.Sensors += (s.Label -> round(1000 * s.Scale * output) / 1000)
      } else {
        logger.warn((s"Improper simulator type for record: $s.Label"))
      }
    }

    // generate category endpoint simulated data
    for (c <- config.Cat) {
      val rv = random.nextDouble()
      var idx = 0
      // generate category level from uniform rv
      if (epd.Result == 1) {
        while (rv >= c.OneThreshold(idx)) {
          idx += 1
        }
      } else {
        while (rv >= c.ZeroThreshold(idx)) {
          idx += 1
        }
      }
      epd.Categories += (c.Label -> c.Level(idx))
      // add variability to simulated data with category error rate
      if (random.nextDouble() < config.ErrRate) {
        val index = random.nextInt(c.Level.length)
        epd.Categories(c.Label) = c.Level(index)
      }
    }

    // convert epdData record to Json string
    val out = new ByteArrayOutputStream()
    val objectMapper = new ObjectMapper() with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)
    objectMapper.writeValue(out, epd)
    // logger.info("TO Producer -> " + out.toString)
    out.toString
  }

  // Actor class for EPD simulator
  class EpdSimActor(producer: ActorRef) extends Actor {

    import SimClass._

    val random = scala.util.Random
    val dummyConfig: EpdConfig = EpdConfig(false, 0.0, 1.0, List.empty[Category], List.empty[Sensor])
    val defaultTopic = "ptopic"

    val logger = play.api.Logger(getClass).logger
    var topic = None: Option[String]
    var epdConfig = None: Option[EpdConfig]
    var simFlag: Boolean = false // flag to start/stop simThread
    var msgFlag: Boolean = false // flag to send/not send simulated data records to Kafka Producer

    val simThread = new Thread {
      override def run {
        var idx: Int = 0
        while (simFlag) {
          if (msgFlag) {
            val key = idx.toString
            val value = DataRecord.makeSimulatedRecord(epdConfig.getOrElse(dummyConfig), topic.getOrElse(defaultTopic))
            // logger.info(value)
            producer ! ProducerMessage(topic.getOrElse(defaultTopic), key, value)
            idx += 1
          }
          val delay: Long = 500 + random.nextInt(2000)
          Thread.sleep(delay)
        }
      }
    }

    def receive: Receive = {
      case InitSimulator(top, epd) => {
        topic = Some(top)
        epdConfig = Some(epd)
        if (!simFlag) {
          simFlag = true
          simThread.start
        }
      }
      case StartSimulator => {
        msgFlag = true
      }
      case StopSimulator => {
        msgFlag = false
      }
      case Terminated => {
        simFlag = false
        simThread.join()
        producer ! ProducerTerminate
        logger.info("EPD Simulator stopped...")
        System.exit(0)
      }
      case _ => logger.info("EPD Simulator Actor received something unexpected...")
    }
  }

  object SimClass {

    case class InitSimulator(topic: String, epdConfig: EpdConfig)

    case object StartSimulator

    case object StopSimulator

  }

}



