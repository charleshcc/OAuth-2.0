package com.oauthpassword.authpassword.config;

import java.util.ArrayList;
import java.util.Collection;

import com.oauthpassword.authpassword.model.CustomUser;
import com.oauthpassword.authpassword.model.UserEntity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Value("${client.sc}")
    private String oascope;

    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserEntity userEntity = new UserEntity();

		try {
			BCryptPasswordEncoder bcencoder = new BCryptPasswordEncoder();
			
			userEntity.setId(username);
			userEntity.setName(username.substring(0, 3)+" "+username.substring(3));
            userEntity.setPassword(bcencoder.encode("****"));
            Collection<GrantedAuthority> grantedAuthoritiesList = new ArrayList<>();
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(oascope);
			grantedAuthoritiesList.add(grantedAuthority);
			userEntity.setGrantedAuthoritiesList(grantedAuthoritiesList);
			
			if (userEntity != null && userEntity.getId() != null && !"".equalsIgnoreCase(userEntity.getId())) {
				CustomUser customUser = new CustomUser(userEntity);
				return customUser;
			} else {
				throw new UsernameNotFoundException("User " + username + " was not found!");
			}
		} catch (Exception e) {
			throw new UsernameNotFoundException("User " + username + " was not found!");
		}

	}
}
