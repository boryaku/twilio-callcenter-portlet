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
package com.ps.cc.configuration;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

import javax.portlet.*;

/**
 * Author: cnbuckley
 * Date: 10/12/11
 * Time: 10:14 AM
 */
public class Action implements ConfigurationAction {

    public static final String ACCT_SID = "ACCT_SID";
    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String APP_SID = "APP_SID";

    @Override
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        String portletResource = ParamUtil.getString(actionRequest, "portletResource");

        PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(actionRequest, portletResource);
        prefs.setValue(ACCT_SID, actionRequest.getParameter(ACCT_SID));
        prefs.setValue(AUTH_TOKEN, actionRequest.getParameter(AUTH_TOKEN));
        prefs.setValue(APP_SID, actionRequest.getParameter(APP_SID));
        prefs.store();

        actionRequest.setAttribute(ACCT_SID, prefs.getValue(Action.ACCT_SID, ""));
        actionRequest.setAttribute(AUTH_TOKEN, prefs.getValue(Action.AUTH_TOKEN, ""));
        actionRequest.setAttribute(APP_SID, prefs.getValue(Action.APP_SID, ""));

        SessionMessages.add(actionRequest, portletConfig.getPortletName() + ".doConfigure");
    }

    @Override
    public String render(PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {

        String portletResource = ParamUtil.getString(renderRequest, "portletResource");
        PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(renderRequest, portletResource);

        renderRequest.setAttribute(ACCT_SID, prefs.getValue(Action.ACCT_SID, ""));
        renderRequest.setAttribute(AUTH_TOKEN, prefs.getValue(Action.AUTH_TOKEN, ""));
        renderRequest.setAttribute(APP_SID, prefs.getValue(Action.APP_SID, ""));

        return "/configuration.jsp";
    }
}
