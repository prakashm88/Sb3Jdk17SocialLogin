package com.itechgenie.apps.jdk11.sb3.configs;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(a -> a.requestMatchers("/**login**", "/error", "/webjars/**", "/templates/**")
				.permitAll().anyRequest().authenticated())
				.csrf(c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
				.logout(l -> l.logoutSuccessUrl("/login").permitAll())
				//.oauth2Login(oauth2 -> oauth2
					//	.userInfoEndpoint(userInfo -> userInfo.oidcUserService(this.oidcUserService())));
				.oauth2Login(Customizer.withDefaults());

		return http.build();
	}

	/*private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
		final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

		return (userRequest) -> {

			try {

				System.out.println("OICD User: " + userRequest);

				// Delegate to the default implementation for loading a user
				OAuth2User oidcUser = delegate.loadUser(userRequest);
				System.out.println("OICD User: " + oidcUser);

				OAuth2AccessToken accessToken = userRequest.getAccessToken();

				System.out.println("accessToken: " + accessToken);

				Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

				OidcUserInfo user = new OidcUserInfo(oidcUser);
				
				// TODO
				// 1) Fetch the authority information from the protected resource using
				// accessToken
				// 2) Map the authority information to one or more GrantedAuthority's and add it
				// to mappedAuthorities

				// 3) Create a copy of oidcUser but use the mappedAuthorities instead
				OidcUser oidcUserNew = new DefaultOidcUser(mappedAuthorities, accessToken, oidcUser.getName() );

				return oidcUserNew;
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}

		};
	}  */
}
