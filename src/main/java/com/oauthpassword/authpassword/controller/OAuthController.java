package com.oauthpassword.authpassword.controller;

import javax.servlet.http.HttpServletRequest;

import com.oauthpassword.authpassword.model.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

@RestController
public class OAuthController {
    
    @Autowired
    private TokenStore tokenStore;

    

    @PostMapping("/revoke")
	public String Revoketoken(@RequestBody UserEntity cu) {
        try{
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(cu.getId());
            OAuth2RefreshToken reToken = tokenStore.readRefreshToken(cu.getName());
            tokenStore.removeRefreshToken(reToken);
            tokenStore.removeAccessToken(accessToken);
        }catch (Exception e){
            System.out.println(e);
            return "Logout error.";
        }
        
		return "Success.";
    }
    
    // @GetMapping("/home")
	// public String home() {
	// 	return "test";
	// }
}
