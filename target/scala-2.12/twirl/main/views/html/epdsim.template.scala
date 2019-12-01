
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

object epdsim extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template1[KafkaConfigForm.ConfigData,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(config: KafkaConfigForm.ConfigData):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.1*/("""
"""),format.raw/*3.1*/("""<html lang="en">

<!-- this is a simulator page -->

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="Endpoint Simulator">
    <meta name="author" content="Barnwaldo">

    <title>Analytics Simulator</title>

    <!-- Custom fonts for this template-->
    <link rel="stylesheet" type="text/css" href='"""),_display_(/*18.51*/routes/*18.57*/.Assets.versioned("vendor/fontawesome-free/css/all.min.css")),format.raw/*18.117*/("""'>
    <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
          rel="stylesheet">

    <!-- Custom styles for this template-->
    <link rel="stylesheet" type="text/css" href='"""),_display_(/*23.51*/routes/*23.57*/.Assets.versioned("stylesheets/sb-admin-2.css")),format.raw/*23.104*/("""'>

    <style>
	input.side-nav """),format.raw/*26.17*/("""{"""),format.raw/*26.18*/("""
		"""),format.raw/*27.3*/("""margin: 0 auto;
		font-size: 0.85rem;
		text-align: center;
	"""),format.raw/*30.2*/("""}"""),format.raw/*30.3*/("""
	
	"""),format.raw/*32.2*/("""div.side-nav """),format.raw/*32.15*/("""{"""),format.raw/*32.16*/("""
		"""),format.raw/*33.3*/("""margin: 0 auto;
		text-align: center;
	"""),format.raw/*35.2*/("""}"""),format.raw/*35.3*/("""
	
	"""),format.raw/*37.2*/("""button.side-nav """),format.raw/*37.18*/("""{"""),format.raw/*37.19*/("""
		"""),format.raw/*38.3*/("""margin: 0 auto;
		text-align: center;
		font-size: 0.85rem;
		color: #3a3b45;
	"""),format.raw/*42.2*/("""}"""),format.raw/*42.3*/("""
	
	"""),format.raw/*44.2*/("""a.side-nav """),format.raw/*44.13*/("""{"""),format.raw/*44.14*/("""
		"""),format.raw/*45.3*/("""margin: 0 auto;
		text-align: center;
	"""),format.raw/*47.2*/("""}"""),format.raw/*47.3*/("""
	
	"""),format.raw/*49.2*/("""h6.side-nav """),format.raw/*49.14*/("""{"""),format.raw/*49.15*/("""
		"""),format.raw/*50.3*/("""margin: 0 auto;
		text-align: center;
	"""),format.raw/*52.2*/("""}"""),format.raw/*52.3*/("""

    """),format.raw/*54.5*/("""</style>
</head>

<body id="page-top">

<!-- Page Wrapper -->
<div id="wrapper">

    <!-- Sidebar -->
    <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">
        <!-- Sidebar - Brand -->
        <a class="sidebar-brand d-flex align-items-center justify-content-center" href="/">
            <div class="sidebar-brand-icon rotate-n-15">
                <i class="fas fa-laugh-wink"></i>
            </div>
            <div class="sidebar-brand-text mx-3">Simulator <sup>4.0</sup>
            </div>
        </a>

        <!-- Divider -->
        <hr class="sidebar-divider my-0">

        <!-- Nav Item - Dashboard -->
        <li class="nav-item active"><a class="nav-link" href="/"> <i class="fas fa-fw fa-tachometer-alt"></i> <span>Dashboard</span></a>
        </li>

        <!-- Divider -->
        <hr class="sidebar-divider">

        <!-- Heading -->
        <div class="sidebar-heading">"""),_display_(/*84.39*/config/*84.45*/.epdSimName),format.raw/*84.56*/("""</div>
        <br/>

        <!-- Nav Item - Pages Collapse Menu -->
        <!-- <li class="nav-item"><a class="nav-link collapsed" href="#"	data-toggle="collapse" data-target="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
        <i class="fas fa-fw fa-cog"></i> <span>Selector</span>
        </a>
            <div id="collapseOne" class="collapse" aria-labelledby="headingOne"	data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Go to page:</h6>
                </div>
            </div></li> -->

        <!-- Divider -->
        <hr class="sidebar-divider">

        <!-- Heading -->
        <div class="sidebar-heading">Simulator Operation</div>

        <!-- Nav Item - Pages Collapse Menu -->
        <li class="nav-item"><a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseTwo"
                                aria-expanded="true" aria-controls="collapseTwo">
            <i class="fas fa-fw fa-cog"></i> <span>Start/Stop</span>
        </a>
            <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Operation:</h6>
                    <a class="collapse-item" onclick="startSim();">Start Simulator</a>
                    <a class="collapse-item" onclick="stopSim();">Stop Simulator</a>
                    <a class="collapse-item" onclick="closeSim();">Close Simulator</a>
                </div>
            </div>
        </li>

        <!-- Divider -->
        <hr class="sidebar-divider">
        <hr class="sidebar-divider">

        <!-- Nav Item - Simulator Inputs Menu -->
        <li class="nav-item"><a class="nav-link collapsed" href="#" data-toggle="collapse"
                                data-target="#collapseUtilities" aria-expanded="true" aria-controls="collapseUtilities">
            <i class="fas fa-fw fa-wrench"></i> <span>Kafka Broker Settings</span>
        </a>
            <div id="collapseUtilities" class="collapse" aria-labelledby="headingUtilities"
                 data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header side-nav">Custom Settings:</h6>
                    <form>
                        <div class="form-group collapse-item side-nav">
                            <label for="simname">Simulator:</label>
                            <input type="text" class="form-control bg-light border-0 small side-nav" id="simname"
                                   name="simname" value="""),_display_(/*135.58*/config/*135.64*/.epdSimName),format.raw/*135.75*/(""">
                        </div>
                        <div class="form-group collapse-item side-nav">
                            <label for="bootstrap">bootstrap server:</label>
                            <input type="text" class="form-control bg-light border-0 small side-nav" id="bootstrap"
                                   name="bootstrap" value="""),_display_(/*140.60*/config/*140.66*/.bootstrapServer),format.raw/*140.82*/(""">
                        </div>
                        <div class="form-group collapse-item side-nav">
                            <label for="ptopic">producer topic:</label>
                            <input type="text" class="form-control bg-light border-0 small side-nav" id="ptopic"
                                   name="ptopic" value="""),_display_(/*145.57*/config/*145.63*/.producerTopic),format.raw/*145.77*/(""">
                        </div>
                        <div class="form-group collapse-item side-nav">
                            <label for="ctopic">consumer topic:</label>
                            <input type="text" class="form-control bg-light border-0 small side-nav" id="ctopic"
                                   name="ctopic" value="""),_display_(/*150.57*/config/*150.63*/.consumerTopic),format.raw/*150.77*/(""">
                        </div>
                        <div class="form-group collapse-item side-nav">
                            <label for="groupId">group id:</label>
                            <input type="text" class="form-control bg-light border-0 small side-nav" id="groupId"
                                   name="groupId" value="""),_display_(/*155.58*/config/*155.64*/.groupId),format.raw/*155.72*/(""">
                        </div>
                    </form>
                </div>
            </div>
        </li>

        <hr class="sidebar-divider">


    </ul>
    <!-- End of Sidebar -->

    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">

        <!-- Main Content -->
        <div id="content">

            <!-- Topbar -->
            <nav
                    class="navbar navbar-expand navbar-light bg-white topbar mb-4 static-top shadow">
                <!-- Sidebar Toggle (Topbar) -->
                <button id="sidebarToggleTop"
                        class="btn btn-link d-md-none rounded-circle mr-3">
                    <i class="fa fa-bars"></i>
                </button>

                <div class="card shadow mb-4"
                     style="width: 100%; height: 100%; margin: auto;">

                    <!-- <div class="card-body" style="width: 100%;"> -->
                    <div class="container" id="scontent" style="width: 100%; margin: auto;">
                        <div class="row">
                            <div id="bstatus" style="width: 100%; text-align: center;"></div>
                        </div>
                    </div>
                    <!-- </div>  -->
                </div>

            </nav>


            <!-- End of Topbar -->

            <!-- Begin Page Content -->
            <div class="container-fluid">

                <!-- Page Heading -->
                <div
                        class="d-sm-flex align-items-center justify-content-between mb-4">
                    <h1 class="h3 mb-0 text-gray-800">Dashboard</h1>
                    <!-- <a href="#" class="d-none d-sm-inline-block btn btn-sm btn-primary shadow-sm"><i class="fas fa-download fa-sm text-white-50"></i> Generate Report</a> -->
                </div>

                <!-- Content Row -->
                <div class="row">

                    <!-- Content Column -->
                    <div class="col-lg-6 mb-4" id="producerdata">

                        <!-- Illustrations -->
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Endpoint Producer</h6>
                            </div>
                            <div class="card-body">
                                <div class="container" id="content">
                                    <div class="row">
                                        <p>Messages sent to Kafka broker appear here:</p>
                                        <div id="producer" style="margin-top: 25px;"></div>
                                        <!-- /#log -->
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-lg-6 mb-4" id="consumerdata">
                        <!-- Approach -->
                        <div class="card shadow mb-4">
                            <div class="card-header py-3">
                                <h6 class="m-0 font-weight-bold text-primary">Endpoint Consumer</h6>
                            </div>
                            <div class="card-body">
                                <div class="container" id="content">
                                    <div class="row">
                                        <p>Messages received from Kafka broker appear here:</p>
                                        <div id="consumer" style="margin-top: 25px;"></div>
                                        <!-- /#log -->
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            <!-- /.container-fluid -->

        </div>
        <!-- End of Main Content -->

        <!-- Footer -->
        <footer class="sticky-footer bg-white">
            <div class="container my-auto">
                <div class="copyright text-center my-auto">
                    <span>Copyright &copy; Barnwaldo 2019</span>
                </div>
            </div>
        </footer>
        <!-- End of Footer -->

    </div>
    <!-- End of Content Wrapper -->

</div>
<!-- End of Page Wrapper -->

<!-- Bootstrap core JavaScript-->
<script type='text/javascript' src='"""),_display_(/*275.38*/routes/*275.44*/.Assets.versioned("vendor/jquery/jquery.min.js")),format.raw/*275.92*/("""'></script>
<script type='text/javascript' src='"""),_display_(/*276.38*/routes/*276.44*/.Assets.versioned("vendor/bootstrap/js/bootstrap.bundle.min.js")),format.raw/*276.108*/("""'></script>

<!-- Core plugin JavaScript-->
<script type='text/javascript' src='"""),_display_(/*279.38*/routes/*279.44*/.Assets.versioned("vendor/jquery-easing/jquery.easing.min.js")),format.raw/*279.106*/("""'></script>

<!-- Custom scripts for all pages-->
<script type='text/javascript' src='"""),_display_(/*282.38*/routes/*282.44*/.Assets.versioned("javascripts/sb-admin-2.js")),format.raw/*282.90*/("""'></script>
<script type='text/javascript' src='"""),_display_(/*283.38*/routes/*283.44*/.Assets.versioned("javascripts/epdsim.js")),format.raw/*283.86*/("""'></script>

</body>

</html>"""))
      }
    }
  }

  def render(config:KafkaConfigForm.ConfigData): play.twirl.api.HtmlFormat.Appendable = apply(config)

  def f:((KafkaConfigForm.ConfigData) => play.twirl.api.HtmlFormat.Appendable) = (config) => apply(config)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: 2019-12-01T12:28:19.093
                  SOURCE: /home/bw/sbt/play-scala-endpoint/app/views/epdsim.scala.html
                  HASH: c38284c75cf6b46f375715a4ef59544a4a6e2820
                  MATRIX: 750->1|880->38|907->39|1409->514|1424->520|1506->580|1786->833|1801->839|1870->886|1930->918|1959->919|1989->922|2077->983|2105->984|2136->988|2177->1001|2206->1002|2236->1005|2302->1044|2330->1045|2361->1049|2405->1065|2434->1066|2464->1069|2570->1148|2598->1149|2629->1153|2668->1164|2697->1165|2727->1168|2793->1207|2821->1208|2852->1212|2892->1224|2921->1225|2951->1228|3017->1267|3045->1268|3078->1274|4051->2220|4066->2226|4098->2237|6868->4979|6884->4985|6917->4996|7302->5353|7318->5359|7356->5375|7730->5721|7746->5727|7782->5741|8156->6087|8172->6093|8208->6107|8579->6450|8595->6456|8625->6464|13133->10944|13149->10950|13219->10998|13296->11047|13312->11053|13399->11117|13508->11198|13524->11204|13609->11266|13724->11353|13740->11359|13808->11405|13885->11454|13901->11460|13965->11502
                  LINES: 21->1|26->2|27->3|42->18|42->18|42->18|47->23|47->23|47->23|50->26|50->26|51->27|54->30|54->30|56->32|56->32|56->32|57->33|59->35|59->35|61->37|61->37|61->37|62->38|66->42|66->42|68->44|68->44|68->44|69->45|71->47|71->47|73->49|73->49|73->49|74->50|76->52|76->52|78->54|108->84|108->84|108->84|159->135|159->135|159->135|164->140|164->140|164->140|169->145|169->145|169->145|174->150|174->150|174->150|179->155|179->155|179->155|299->275|299->275|299->275|300->276|300->276|300->276|303->279|303->279|303->279|306->282|306->282|306->282|307->283|307->283|307->283
                  -- GENERATED --
              */
          