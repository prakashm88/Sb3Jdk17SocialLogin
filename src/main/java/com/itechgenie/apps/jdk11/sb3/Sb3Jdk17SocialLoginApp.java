package com.itechgenie.apps.jdk11.sb3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.reactive.config.EnableWebFlux;

import lombok.extern.slf4j.Slf4j;

@ComponentScan
@SpringBootApplication
//@EnableAspectJAutoProxy
//@EnableAspectJAutoProxy(proxyTargetClass = true)
@Slf4j
//@EnableWebFlux
public class Sb3Jdk17SocialLoginApp {

	public static void main(String[] args) {
		log.info("Started Sb3Jdk17SocialLoginApp");
		SpringApplication.run(Sb3Jdk17SocialLoginApp.class, args);
	}

}