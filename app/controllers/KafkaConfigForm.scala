package controllers

import play.api.data.Forms._
import play.api.data.Form

/**
 * Form data for EPD Start page form
 */
object KafkaConfigForm {

  case class ConfigData(epdSimName: String, bootstrapServer: String, producerTopic: String, consumerTopic: String, groupId: String)

  val form: Form[ConfigData] = Form(
    mapping(
      "epdSimName" -> nonEmptyText,
      "bootstrapServer" -> nonEmptyText,
      "producerTopic" -> nonEmptyText,
      "consumerTopic" -> nonEmptyText,
      "groupId" -> nonEmptyText,
    )(ConfigData.apply)(ConfigData.unapply)
  )

  def getDefaultConfig: ConfigData = {
    ConfigData("default", "localhost:9092", "ptopic", "ctopic", "group")
  }
}
