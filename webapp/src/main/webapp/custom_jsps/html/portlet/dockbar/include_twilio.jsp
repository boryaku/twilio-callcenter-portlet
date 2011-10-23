<script type="text/javascript">

    var connection;

    $(document).ready(function(){

        $("#incomingCall").dialog({
            autoOpen: false,
            show: 'fade',
            hide: 'fade',
            position: ['right', 35],
            resizable: false,
            height: 85
        });

        $("#softPhone").dialog({
            autoOpen: false,
            show: 'fade',
            hide: 'fade',
            position: ['right', 35],
            resizable: false,
            height: 85
        });

        Twilio.Device.setup("<%=session.getAttribute("USER_twilio_token")%>");

        $("#call").click(function() {
            params = { "client" : $("#client_to_call").val() };
            Twilio.Device.connect(params);
        });

        $("#hangup").click(function() {
            Twilio.Device.disconnectAll();
        });

        Twilio.Device.incoming(function (conn) {
            connection = conn;
            $("#incomingCall").dialog('open');
            $("#incomingCall").show();
            $('#incomingText').html('<p>Call from '+conn.parameters.From+'</p>');
        });

        Twilio.Device.ready(function (device) {
            $('#status').attr("class", "statusConnected");
            //allow phone calls
            $("#status").click(function(e) {
                $("#softPhone").dialog('open');
                $("#softPhone").show();
                return false;
            });
        });

        Twilio.Device.offline(function (device) {
            $('#status').attr("class", "statusConnecting");
        });

        Twilio.Device.error(function (error) {
            $('#status').text(error);
        });

        Twilio.Device.connect(function (conn) {
            toggleCallStatus();
        });

        Twilio.Device.disconnect(function (conn) {
            toggleCallStatus();
        });

        function toggleCallStatus(){
            $('#call').toggle();
            $('#hangup').toggle();
        }
    });

    function declineIncoming(){
        connection.disconnect();
        $('#incomingCall').dialog('close');
    }

    function acceptIncoming(){
        connection.accept();
        $('#acceptCall').hide();
    }
</script>

<li id="status" class="statusConnecting"></li>

<div id="incomingCall" class="incomingCall">
    <span class="dialogClose" id="incomingCallDialogClose" onclick="declineIncoming();">[x]</span>
    <div id="incomingText"></div>
    <input type="button" id="acceptCall" value="" class="connectCall" alt="Call" onclick="acceptIncoming();"/>
    <input type="button" id="hangupCall" value="" class="disconnectCall" alt="End Call" onclick="declineIncoming();"/>
</div>

<div id="softPhone" class="softPhone">
    <span class="dialogClose" id="softPhoneDialogClose" onclick="$('#softPhone').dialog('close');">[x]</span>
    <br/>
    Who would you like to call?<br/>
    <input type="text" id="client_to_call" name="client_to_call"/>
    <input type="button" id="call" value="" class="connectCall" alt="Call"/>
    <input type="button" id="hangup" value="" style="display:none;" class="disconnectCall" alt="End Call"/>
</div>
