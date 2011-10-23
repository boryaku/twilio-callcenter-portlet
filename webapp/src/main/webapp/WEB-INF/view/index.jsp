<%--
/**
 * Copyright (c) Pure Source, LLC All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>
<div class="configure_group_phone" style="display: block;">
    <span style="padding: 10px;">The Phone Portlet needs configuration...</span>
    </span>
</div>

<div>
<button class="call" onclick="call();">
Call
</button>
<button class="hangup" onclick="hangup();">
Hangup
</button>
<input type="text" id="number" name="number"
placeholder="Enter a phone number or client to call"/>
<div id="log">Establishing connectivity...</div>
</div>

<ul id="people"/>

<script type="text/javascript">
Twilio.Device.setup("${AUTH_TOKEN}");

Twilio.Device.ready(function (device) {
$("#log").text("Client '${user.screenName}' is ready");
});
Twilio.Device.error(function (error) {
$("#log").text("Error: " + error.message);
});
Twilio.Device.connect(function (conn) {
$("#log").text("Successfully established call");
});
Twilio.Device.disconnect(function (conn) {
$("#log").text("Call ended");
});
Twilio.Device.incoming(function (conn) {
$("#log").text("Incoming connection from " + conn.parameters.From);
// accept the incoming connection and start two-way audio
conn.accept();
});

Twilio.Device.presence(function (pres) {
if (pres.available) {
// create an item for the client that became available
$("<li>", {id: pres.from, text: pres.from}).click(function () {
$("#number").val(pres.from);
call();
}).prependTo("#people");
}
else {
// find the item by client name and remove it
$("#" + pres.from).remove();
}
});

function call() {
// get the phone number or client to connect the call to
params = {"PhoneNumber": $("#number").val()};
Twilio.Device.connect(params);
}

function hangup() {
Twilio.Device.disconnectAll();
}
</script>