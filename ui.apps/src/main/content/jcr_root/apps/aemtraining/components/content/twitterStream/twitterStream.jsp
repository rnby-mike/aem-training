 <%@include file="/libs/foundation/global.jsp" %>
 
<h3 style="color: gray;"> Search by <span style="color: black;">${fn:toUpperCase(properties.query)}</span></h3>
<div id="tweets"></div>
 
 <script>
  if (typeof (EventSource) !== "undefined") {
   var source = new EventSource("http://localhost:4502/services/stream?query=${fn:replace(properties.query,' ', '%20')}");
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
 </script>
 