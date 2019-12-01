$(document).ready(function(){
	var pmessages = [];
	var cmessages = [];
	conn = new WebSocket("ws://" + document.domain + ':' + location.port +  "/ws");

	conn.onopen = function(e) {
		conn.send("connection established, client reply to server");
	};
	conn.onmessage = function (evt) {
        var msg = evt.data.split(';')
		// conn.send('pong');
		if(msg[0] === 'producer'){
			//maintain a list of four data records
			if (pmessages.length >= 4){
	            pmessages.shift()
	        }
	        pmessages.push(msg[1]);
	        // console.log("pmessage.length=" + pmessages.length + " -- Latest Message: " + msg[1])
	        output = '';
	        for (var i = 0; i < pmessages.length; i++){
	            output = output + '<p>' + pmessages[i] + '</p>';
	        }
			document.getElementById("producer").innerHTML = output;
		}
		else if (msg[0] === 'consumer') {
			//maintain a list of four data records
			if (cmessages.length >= 4){
	            cmessages.shift()
	        }
	        cmessages.push(msg[1]);
	        output = '';
	        for (var i = 0; i < cmessages.length; i++){
	            output = output + '<p>' + cmessages[i] + '</p>';
	        }
				document.getElementById("consumer").innerHTML = output;
		}
		else if (msg[0] === 'status') {
			document.getElementById("bstatus").innerHTML = msg[1].toString();
		}
		else if (msg[0] === 'params') {
			if (msg[1] === 'ping') {
				conn.send('pong');
			}
		}
	}
});

// start EPD simulator and kafka broker messages
function startSim() {
    conn.send("controlMessage; startSimulator");
}
// stop EPD simulator and kafka broker messages
function stopSim() {
 	conn.send("controlMessage; stopSimulator");
}
// close EPD simulator; shutdown akka http server; leave webpage
// would hide and password protect in production (or leave out entirely)
function closeSim() {
  	conn.send("controlMessage; closeSimulator");
   	window.open ('https://www.bing.com/','_self',false)
}


