package com.itechgenie.apps.jdk11.sb3.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiDocsConfig {

	@Value("${spring.security.oauth2.client.provider.gitlab.authorization-uri}")
	String authServerUri;
	
	@Value("${spring.security.oauth2.client.provider.gitlab.token-uri}")
	String tokenUri;

	private static final String OAUTH_SCHEME_NAME = "OAuth Auth";
 
	@Bean
	OpenAPI docsForOpenApi(BuildProperties buildProperties) {
		return new OpenAPI().components(new Components().addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme()))
				.addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME))
				.info(new Info().title("SpringSecuritySamplee").version(buildProperties.getVersion()).description(
						"Spring boot 3, Jdk17, SocialLogin Samples - google, gitlab, facebook, github, Custom annotation Sample")
						.version("1.0"));
	}

	private SecurityScheme createOAuthScheme() {
		OAuthFlows flows = createOAuthFlows();
		return new SecurityScheme().type(SecurityScheme.Type.OAUTH2).flows(flows);
	}

	private OAuthFlows createOAuthFlows() {
		OAuthFlow flow = createAuthorizationCodeFlow();
		return new OAuthFlows().authorizationCode(flow);
	}

	private OAuthFlow createAuthorizationCodeFlow() {
		
		Scopes scopes = new Scopes();
		scopes.addString("openid", "openid");
		scopes.addString("profile", "profile");
		
		return new OAuthFlow().authorizationUrl(authServerUri)
				.scopes(scopes);
	}

	/*
	 * @Bean public OpenAPI customOpenAPI(BuildProperties buildProperties) { return
	 * new OpenAPI() .components(new Components().addSecuritySchemes("basicScheme",
	 * new SecurityScheme().type(SecurityScheme.Type.OAUTH2).scheme("OAuth")))
	 * .info(new Info().title("MyAPIs").version(buildProperties.getVersion())
	 * .license(new License().name("Apache 2.0").url("http://springdoc.org"))); }
	 * 
	 * @Bean GroupedOpenApi customOpenAPIDocs(@Value("${springdoc.version}") String
	 * appVersion) { String[] paths = { "/**" }; return
	 * GroupedOpenApi.builder().group("MyAPIs") .addOpenApiCustomizer(openApi ->
	 * openApi.info(new Info().title("My API").version(appVersion)))
	 * .pathsToMatch(paths).build(); }
	 */

 /*	@Bean
	GroupedOpenApi customOpenAPIDocs(@Value("${springdoc.version}") String appVersion) {
		String[] paths = { "/**" };
		return GroupedOpenApi.builder().group("MyAPIs")
				.addOpenApiCustomizer(openApi -> openApi.info(new Info().title("My API").version(appVersion)))
				.pathsToMatch(paths).build();
	} */
}
