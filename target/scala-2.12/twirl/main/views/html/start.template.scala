
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

object start extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template3[Form[KafkaConfigForm.ConfigData],Call,MessagesRequestHeader,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(configForm: Form[KafkaConfigForm.ConfigData], postUrl: Call)(implicit request: MessagesRequestHeader):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.1*/("""
"""),format.raw/*3.1*/("""<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>EPD Simlink Start</title>

    <!-- Custom fonts for this template-->
    <link rel="stylesheet" type="text/css" href='"""),_display_(/*16.51*/routes/*16.57*/.Assets.versioned("vendor/fontawesome-free/css/all.min.css")),format.raw/*16.117*/("""'>
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

    <!-- Custom styles for this template-->
    <link rel="stylesheet" type="text/css" href='"""),_display_(/*20.51*/routes/*20.57*/.Assets.versioned("stylesheets/sb-admin-2.css")),format.raw/*20.104*/("""'>

</head>

<body class="bg-gradient-primary">

<div class="container">

    <!-- Outer Row -->
    <div class="row justify-content-center">

        <div class="col-xl-10 col-lg-12 col-md-9">

            <div class="card o-hidden border-0 shadow-lg my-5">
                <div class="card-body p-0">
                    <!-- Nested Row within Card Body -->
                    <div class="row">
                        <div class="col-lg-6 d-none d-lg-block bg-login-image"></div>
                        <div class="col-lg-6">
                            <div class="p-5">
                                <div class="text-center">
                                    <h1 class="h4 text-gray-900 mb-4">Initialize New Simulator!</h1>
                                </div>
                                """),_display_(/*43.34*/helper/*43.40*/.form(postUrl, 'class -> "user")/*43.72*/ {_display_(Seq[Any](format.raw/*43.74*/("""
                                    """),_display_(/*44.38*/helper/*44.44*/.CSRF.formField),format.raw/*44.59*/("""
                                    """),format.raw/*45.37*/("""<div class="form-group">
                                        """),_display_(/*46.42*/helper/*46.48*/.inputText(configForm("epdSimName"), 'class -> "form-control form-control-user", '_showErrors -> false,
                                        'placeholder -> "Enter endpoint simulator name...", '_label -> null, '_help -> null, 'style -> "margin-top: -1rem")),format.raw/*47.156*/("""
                                    """),format.raw/*48.37*/("""</div>
                                    <div class="form-group">
                                        """),_display_(/*50.42*/helper/*50.48*/.inputText(configForm("bootstrapServer"), 'class -> "form-control form-control-user", '_showErrors -> false,
                                        'placeholder -> "Enter Bootstrap Server Address...", '_label -> null, '_help -> null, 'style -> "margin-top: -1rem")),format.raw/*51.157*/("""
                                    """),format.raw/*52.37*/("""</div>
                                    <div class="form-group">
                                        """),_display_(/*54.42*/helper/*54.48*/.inputText(configForm("producerTopic"), 'class -> "form-control form-control-user", '_showErrors -> false,
                                        'placeholder -> "Enter Producer Topic...", '_label -> null, '_help -> null, 'style -> "margin-top: -1rem")),format.raw/*55.147*/("""
                                    """),format.raw/*56.37*/("""</div>
                                    <div class="form-group">
                                        """),_display_(/*58.42*/helper/*58.48*/.inputText(configForm("consumerTopic"), 'class -> "form-control form-control-user", '_showErrors -> false,
                                        'placeholder -> "Enter Consumer Topic...", '_label -> null, '_help -> null, 'style -> "margin-top: -1rem")),format.raw/*59.147*/("""
                                    """),format.raw/*60.37*/("""</div>
                                    <div class="form-group">
                                        """),_display_(/*62.42*/helper/*62.48*/.inputText(configForm("groupId"), 'class -> "form-control form-control-user", '_showErrors -> false,
                                        'placeholder -> "Enter Group Id...", '_label -> null, '_help -> null, 'style -> "margin-top: -1rem")),format.raw/*63.141*/("""
                                    """),format.raw/*64.37*/("""</div>
                                    <a href="javascript:;" onclick="parentNode.submit();" class="btn btn-primary btn-user btn-block">
                                        Start
                                    </a>
                                    <hr>
                                """)))}),format.raw/*69.34*/("""
                                """),format.raw/*70.33*/("""<hr>
                                <!--<div class="text-center">
                                  <a class="small" href="action-one.html">Action One!</a>
                                </div>
                                <div class="text-center">
                                  <a class="small" href="action-two.html">Action Two!</a>
                                </div>-->
                            </div>
                        </div>
                    </div>
                    """),format.raw/*80.82*/("""
                    """),_display_(/*81.22*/if(configForm.hasGlobalErrors)/*81.52*/ {_display_(Seq[Any](format.raw/*81.54*/("""
                        """),_display_(/*82.26*/configForm/*82.36*/.globalErrors.map/*82.53*/ { error: FormError =>_display_(Seq[Any](format.raw/*82.75*/("""
                            """),format.raw/*83.29*/("""<div>
                                """),_display_(/*84.34*/error/*84.39*/.key),format.raw/*84.43*/(""": """),_display_(/*84.46*/error/*84.51*/.message),format.raw/*84.59*/("""
                            """),format.raw/*85.29*/("""</div>
                        """)))}),format.raw/*86.26*/("""
                    """)))}),format.raw/*87.22*/("""
                """),format.raw/*88.17*/("""</div>
            </div>

        </div>

    </div>

</div>

<!-- Bootstrap core JavaScript-->
<script type='text/javascript' src='"""),_display_(/*98.38*/routes/*98.44*/.Assets.versioned("vendor/jquery/jquery.min.js")),format.raw/*98.92*/("""'></script>
<script type='text/javascript' src='"""),_display_(/*99.38*/routes/*99.44*/.Assets.versioned("vendor/bootstrap/js/bootstrap.bundle.min.js")),format.raw/*99.108*/("""'></script>

<!-- Core plugin JavaScript-->
<script type='text/javascript' src='"""),_display_(/*102.38*/routes/*102.44*/.Assets.versioned("vendor/jquery-easing/jquery.easing.min.js")),format.raw/*102.106*/("""'></script>

<!-- Custom scripts for all pages-->
<script type='text/javascript' src='"""),_display_(/*105.38*/routes/*105.44*/.Assets.versioned("javascripts/sb-admin-2.js")),format.raw/*105.90*/("""'></script>

</body>

</html>

<!-- Use Assets.versioned for fixed files in Public Directorry and Assets.at for webjars and externals from sbt -->"""))
      }
    }
  }

  def render(configForm:Form[KafkaConfigForm.ConfigData],postUrl:Call,request:MessagesRequestHeader): play.twirl.api.HtmlFormat.Appendable = apply(configForm,postUrl)(request)

  def f:((Form[KafkaConfigForm.ConfigData],Call) => (MessagesRequestHeader) => play.twirl.api.HtmlFormat.Appendable) = (configForm,postUrl) => (request) => apply(configForm,postUrl)(request)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2019-12-01T12:28:19.166
                  SOURCE: /home/bw/sbt/play-scala-endpoint/app/views/start.scala.html
                  HASH: bfc1e201f0618a9a648b544f00b48ee5144837b6
                  MATRIX: 782->1|978->104|1005->105|1443->516|1458->522|1540->582|1810->825|1825->831|1894->878|2729->1686|2744->1692|2785->1724|2825->1726|2890->1764|2905->1770|2941->1785|3006->1822|3099->1888|3114->1894|3395->2153|3460->2190|3596->2299|3611->2305|3898->2570|3963->2607|4099->2716|4114->2722|4389->2975|4454->3012|4590->3121|4605->3127|4880->3380|4945->3417|5081->3526|5096->3532|5359->3773|5424->3810|5757->4112|5818->4145|6345->4705|6394->4727|6433->4757|6473->4759|6526->4785|6545->4795|6571->4812|6631->4834|6688->4863|6754->4902|6768->4907|6793->4911|6823->4914|6837->4919|6866->4927|6923->4956|6986->4988|7039->5010|7084->5027|7245->5161|7260->5167|7329->5215|7405->5264|7420->5270|7506->5334|7615->5415|7631->5421|7716->5483|7831->5570|7847->5576|7915->5622
                  LINES: 21->1|26->2|27->3|40->16|40->16|40->16|44->20|44->20|44->20|67->43|67->43|67->43|67->43|68->44|68->44|68->44|69->45|70->46|70->46|71->47|72->48|74->50|74->50|75->51|76->52|78->54|78->54|79->55|80->56|82->58|82->58|83->59|84->60|86->62|86->62|87->63|88->64|93->69|94->70|104->80|105->81|105->81|105->81|106->82|106->82|106->82|106->82|107->83|108->84|108->84|108->84|108->84|108->84|108->84|109->85|110->86|111->87|112->88|122->98|122->98|122->98|123->99|123->99|123->99|126->102|126->102|126->102|129->105|129->105|129->105
                  -- GENERATED --
              */
          