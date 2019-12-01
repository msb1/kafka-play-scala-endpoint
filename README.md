### kafka-play-scala-endpoint

<h4> General Info </h4>
<ol>
  <li> 
    The purpose of this application is enable a Kafka endpoint simulator for testing Kafka, Kafka Connect and Kafka Streams with simulated analytics data streams
  </li>
  <li> In this test application, Alpakka Kafka Producer/Consumer are uitlized. 
    <ul>
      <li>Alpakka Kafka Producer is implemented in an Akka Actor</li>
      <li>Alpakka Kafka Consumer is implemented in a Runnable Graph </li>
      <li> Currently the producer and consumer defaults to bootstrap_servers='localhost:9092. Enter info in side nav menu for bootstrap-server and topics, etc.. </li>
      <li> There is an epd.conf file that should be edited to make changes in the simulator outputs. 
    </ul>
  </li>
  <li> Play WebSockets is implemented with Origin Checking (CSRF is implemented with start page form for Kafka Config)
    <ul>
      <li>Akka BroadcastHub is used to allow Producer Messages to be sent to Kafka Broker, Websockets and Logger</li>
      <li>Akka Runnable Graph with broadcaster is used to send Kafka Consumer messages to Logger, Websockets (and can write directly to DB if desired) </li>
      <li> Currently the producer and consumer defaults to bootstrap_servers='localhost:9092. Enter info in side nav menu for bootstrap-server and topics, etc.. </li>
      <li> There is an epd.conf file that should be edited to make changes in the simulator outputs. 
    </ul>
  </li>
  <li> 
    The web page is based on the bootstrap sample dashboard from https://startbootstrap.com/previews/sb-admin-2/ so there is considerable flexibility in its application. There are currently a start and stop button in the left-side collapsable menu.
  </li>
</ol>
