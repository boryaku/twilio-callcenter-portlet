package com.ps.cc.model;


/**
 * Author: cnbuckley
 * Date: 10/24/11
 * Time: 9:18 AM
 */
public class TwilioConfiguration {

    private String acctSid;
    private String authToken;
    private String appSid;

    public String getAcctSid() {
        return acctSid;
    }

    public void setAcctSid(String acctSid) {
        this.acctSid = acctSid;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAppSid() {
        return appSid;
    }

    public void setAppSid(String appSid) {
        this.appSid = appSid;
    }
}
