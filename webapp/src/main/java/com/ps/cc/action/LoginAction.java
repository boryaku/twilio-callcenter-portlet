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

package com.ps.cc.action;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.ps.cc.controller.Init;
import com.ps.cc.model.TwilioConfiguration;
import com.twilio.sdk.client.TwilioCapability;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.portlet.PortletPreferences;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: cnbuckley
 * Date: 10/17/11
 * Time: 4:13 PM
 */
public class LoginAction extends Action {

    private static Log _log = LogFactory.getLog(LoginAction.class);

    @Override
    public void run(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        try{
            HttpSession session = request.getSession();

            long companyId = PortalUtil.getCompanyId(request);
			long userId = PortalUtil.getUserId(request);

            User user = UserLocalServiceUtil.getUserById(companyId, userId);
            TwilioConfiguration twilioConfiguration = new TwilioConfiguration();

            try{
                File file = new File("twilioConfiguration.txt");
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                twilioConfiguration = (TwilioConfiguration)in.readObject();
                in.close();
            }catch (Exception ex){
                //didn't exist...
            }

            String acctSid = twilioConfiguration.getAcctSid();
            String authToken = twilioConfiguration.getAuthToken();
            String appSid = twilioConfiguration.getAppSid();

            TwilioCapability capability = new TwilioCapability(acctSid, authToken);

            capability.allowEventStream(null);
            capability.allowClientIncoming(user.getScreenName());

            Map<String, String> params = new HashMap<String, String>();
            params.put("portraitId", String.valueOf(user.getPortraitId()));

            capability.allowClientOutgoing(appSid, params);

            String token = capability.generateToken();

            //Now we will share the json object with everyone
            session.setAttribute("USER_twilio_token", token);

            if(_log.isInfoEnabled()){
                _log.info("Twilio Token Generated: "+token+" ("+user.getScreenName()+")");
            }

        }catch(Exception ex){
            _log.error("Error during post login", ex);
        }
    }
}
