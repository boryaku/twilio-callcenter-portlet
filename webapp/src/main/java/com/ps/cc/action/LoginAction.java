package com.ps.cc.action;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.twilio.sdk.client.TwilioCapability;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
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

            String acctSid = "AC4a96626e76164b2ba24a708d956f45df";
            String authToken = "22ad3656de217ebe17b8308ac236c61b";
            String appSid = "AP55c85930482a4826850334b21c0a372a";

            TwilioCapability capability = new TwilioCapability(acctSid, authToken);

            capability.allowEventStream(null);
            capability.allowClientIncoming(user.getScreenName());

            Map<String, String> params = new HashMap<String, String>();

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
