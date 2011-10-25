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

import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.ps.cc.model.TwilioConfiguration;
import com.twilio.sdk.client.TwilioCapability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import javax.portlet.ActionRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import java.io.*;
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

    public static final String ACCT_SID = "ACCT_SID";
    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String APP_SID = "APP_SID";

    @RequestMapping
    public String index(ModelMap map, RenderRequest renderRequest) throws Exception{

        TwilioConfiguration twilioConfiguration = new TwilioConfiguration();

        try{
            File file = new File("twilioConfiguration.txt");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            twilioConfiguration = (TwilioConfiguration)in.readObject();
            in.close();
        }catch (Exception ex){
            //didn't exist...
        }

        map.addAttribute("command", twilioConfiguration);

        return "index";
    }

    @ActionMapping(params = "action=saveTwilioConfiguration")
    public void saveTwilioConfiguration(@ModelAttribute TwilioConfiguration twilioConfiguration, ActionRequest actionRequest) throws Exception{
        String portletResource = "purePhone_WAR_twiliocallcenterportlet";

        ObjectOutput out = new ObjectOutputStream(new FileOutputStream("twilioConfiguration.txt"));
        out.writeObject(twilioConfiguration);
        out.close();

        User user = PortalUtil.getUser(actionRequest);

        TwilioCapability capability = new TwilioCapability(twilioConfiguration.getAcctSid(), twilioConfiguration.getAuthToken());

        capability.allowEventStream(null);
        capability.allowClientIncoming(user.getScreenName());

        Map<String, String> params = new HashMap<String, String>();
        params.put("portraitId", String.valueOf(user.getPortraitId()));

        capability.allowClientOutgoing(twilioConfiguration.getAppSid(), params);

        String token = capability.generateToken();

        //Now we will share the json object with everyone
        PortalUtil.getHttpServletRequest(actionRequest).getSession().setAttribute("USER_twilio_token", token);
    }

}
