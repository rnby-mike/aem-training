$( document ).ready(function() {
	if (typeof (EventSource) !== "undefined") {
		var query = document.getElementById("query").innerHTML
		var source = new EventSource("http://localhost:4502/services/stream?query=" + query);
		source.onmessage = function(event) {
			var data = JSON.parse(event.data);
			document.getElementById("tweets").innerHTML = "";
			console.log(data);
			for (var i = 0; i < data.length; i++) {
				var user = data[i].user.screenName;
				var text = data[i].text;
				document.getElementById("tweets").innerHTML += "@" + user
				+ "<br/>" + text + "<br><br>";  
			}
		};
	} else {
		document.getElementById("ServerTime").innerHTML = "Sorry, your browser does not support server-sent events...";
	}
});