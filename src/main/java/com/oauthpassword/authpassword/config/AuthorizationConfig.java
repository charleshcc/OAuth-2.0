package com.oauthpassword.authpassword.config;

import java.sql.SQLException;
import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${client.un}")
    private String oaclient;
    @Value("${client.pw}")
    private String oasecret;
    @Value("${client.sc}")
    private String oascope;
    @Value("${client.rid}")
    private String oarid;

    @Autowired
	DataSource dataSource;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    //@Autowired
    //private UserDetailsService userDetailsService;
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //@formatter:off
        clients.inMemory().withClient(oaclient).secret(passwordEncoder.encode(oasecret)).authorizedGrantTypes("password", "refresh_token")
        .accessTokenValiditySeconds(300).refreshTokenValiditySeconds(600)
                     .scopes(oascope)
                     .resourceIds(oarid);
        //@formatter:on
        //clients.jdbc(dataSource).withClient(oaclient).secret(passwordEncoder.encode(oasecret)).authorizedGrantTypes("password", "refresh_token").accessTokenValiditySeconds(300).refreshTokenValiditySeconds(400).scopes(oascope).resourceIds(oarid);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        
        endpoints.tokenStore(new JdbcTokenStore(dataSource)).authenticationManager(authenticationManager).userDetailsService(userDetailsService);
        //endpoints.authenticationManager(authenticationManager).tokenStore(tokenStore()).userDetailsService(userDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
                .checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }


    
}
