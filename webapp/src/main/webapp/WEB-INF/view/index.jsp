<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page contentType="text/html" isELIgnored="false"%>

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
<portlet:actionURL var="saveTwilioConfigurationUrl">
	<portlet:param name="action" value="saveTwilioConfiguration" />
</portlet:actionURL>

<form:form name="twilioConfiguration" id="twilioConfiguration" method="post" action="${saveTwilioConfigurationUrl}">
    <table>
        <tr>
            <td><label style="font-size: larger;">ACCOUNT Sid:</label></td>
            <td> <form:input path="acctSid" class="twilio_input"/></td>
        </tr>
        <tr>
            <td><label style="font-size: larger;">AUTH Token:</label></td>
            <td><form:input path="authToken" class="twilio_input"/></td>
        </tr>
         <tr>
            <td><label style="font-size: larger;">App SID:</label></td>
            <td><form:input path="appSid" class="twilio_input"/></td>
        </tr>
    </table>
    <input type="submit"/>
</form:form>

