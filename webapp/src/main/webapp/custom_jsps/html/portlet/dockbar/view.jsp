<%@ page import="com.liferay.portal.kernel.util.StringUtil" %>
<%@ page import="com.liferay.portal.kernel.util.StringPool" %>

<%@ taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<liferay-util:buffer var="html">

    <liferay-util:include page="/html/portlet/dockbar/view.portal.jsp" />

</liferay-util:buffer>

<liferay-util:buffer var="twilio">

    <liferay-util:include page="/html/portlet/dockbar/include_twilio.jsp" />

</liferay-util:buffer>

<%
    int x = html.lastIndexOf("<ul class=\"aui-toolbar user-toolbar\">");
    StringBuffer htmlBuffer = new StringBuffer(html);

    if (x != -1) {
        htmlBuffer.insert(x+37, twilio);
    }

%>

<%= htmlBuffer.toString() %>