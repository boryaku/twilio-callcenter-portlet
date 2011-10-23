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
package com.ps.cc.controller;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.ps.cc.configuration.Action;
import com.twilio.sdk.client.TwilioCapability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: cnbuckley
 * Date: 10/5/11
 * Time: 3:40 PM
 */
@Controller(value = "CallCenterController")
@RequestMapping("VIEW")
public class Init {
    private Logger logger = LoggerFactory.getLogger(Init.class);

    @RequestMapping
    public String index(ModelMap map, PortletRequest request){

        try{
            User user =  PortalUtil.getUser(request);
            map.addAttribute("user", user);

            String portletResource = "purePhone_WAR_purephone";
            PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(request, portletResource);

            String acctSid = prefs.getValue(Action.ACCT_SID, "AC4a96626e76164b2ba24a708d956f45df");
            String authToken = prefs.getValue(Action.AUTH_TOKEN, "22ad3656de217ebe17b8308ac236c61b");
            String appSid = prefs.getValue(Action.APP_SID, "AP55c85930482a4826850334b21c0a372a");

            TwilioCapability capability = new TwilioCapability(acctSid, authToken);

            capability.allowEventStream(null);
            capability.allowClientIncoming(user.getScreenName());

            Map<String, String> params = new HashMap<String, String>();

            capability.allowClientOutgoing(appSid, params);

            String token = null;
            try {
                token = capability.generateToken();

            } catch (TwilioCapability.DomainException e) {
                e.printStackTrace();
            }

            //get the current group's user list and their availability ie. online or not...
            ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);

            map.addAttribute(Action.AUTH_TOKEN, token);

            map.addAttribute("now", new Date().getTime());
        }catch (Exception ex){
            logger.error("Init exception", ex);
        }

        return "index";
    }
}
