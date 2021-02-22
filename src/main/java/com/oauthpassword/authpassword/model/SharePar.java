package com.oauthpassword.authpassword.model;

import org.springframework.beans.factory.annotation.Value;

public class SharePar {
    @Value("${client.un}")
    private String oaclient;
    @Value("${client.pw}")
    private String oasecret;
    @Value("${client.sc}")
    private String oascope;
}
