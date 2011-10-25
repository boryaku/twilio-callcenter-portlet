<%--
/**
 * Copyright (c) Pure Source LLC. All rights reserved.
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

            var screenName = conn.parameters.From.split(":")[1];
            var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function() {
                if(xhr.readyState == 4) {
                    var user = eval('(' + xhr.responseText + ')');
                    var img = '<img class=\"avatar\" src=\"/image/user_male_portrait?img_id='+user.portraitId+'&t='+new Date()+'\" width=\"25px\"/>';
                    $('#incomingText').html('<p>'+img+' '+user.fullName+' Calling.</p>');
                    $("#incomingCall").dialog('open');
                    $("#incomingCall").show();
                }
            };
            var url = '/group/control_panel/manage?p_p_id=purePhone_WAR_twiliocallcenterportlet&p_p_lifecycle=2&p_p_resource_id=json_user&screenname='+screenName;

            xhr.open("GET", url, true);
            xhr.send();
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

    function disconnect(element){
        Twilio.Device.disconnectAll();
        $('#'+element).dialog('close');
    }

    function acceptIncoming(){
        connection.accept();
        $('#acceptCall').hide();
    }

</script>

<li id="status" class="statusConnecting"></li>

<div id="incomingCall" class="incomingCall">
    <span class="dialogClose" id="incomingCallDialogClose" onclick="disconnect('incomingCall');"></span>
    <div id="incomingText"></div>
    <input type="button" id="acceptCall" value="" class="connectCall" alt="Call" onclick="acceptIncoming();"/>
    <input type="button" id="hangupCall" value="" class="disconnectCall" alt="End Call" onclick="disconnect('incomingCall');"/>
</div>

<div id="softPhone" class="softPhone">
    <span class="dialogClose" id="softPhoneDialogClose" onclick="disconnect('softPhone');"></span>
    <br/>
    Who would you like to call?<br/>
    <input type="text" id="client_to_call" name="client_to_call"/>
    <input type="button" id="call" value="" class="connectCall" alt="Call"/>
    <input type="button" id="hangup" value="" style="display:none;" class="disconnectCall" alt="End Call"/>
</div>
