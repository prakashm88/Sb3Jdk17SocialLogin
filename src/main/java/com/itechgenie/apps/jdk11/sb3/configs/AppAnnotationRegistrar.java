package com.itechgenie.apps.jdk11.sb3.configs;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import com.itechgenie.apps.jdk11.sb3.annotations.ItgWebClient;
import com.itechgenie.apps.jdk11.sb3.services.impl.ItgWebClientImpl;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AppAnnotationRegistrar implements BeanPostProcessor {

	@Autowired
	private ItgWebClientImpl itgWebClientImpl;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		log.debug("Inside postProcessBeforeInitialization - beanName: + " + beanName);
		ReflectionUtils.doWithFields(bean.getClass(), field -> processField(field, bean));
		return bean;
	}

	private void processField(Field field, Object bean) {
		ItgWebClient annotation = AnnotationUtils.getAnnotation(field.getType(), ItgWebClient.class);
		if (annotation != null) {
			Object proxy = null;
			Class<?> fieldType = field.getType();
			if (fieldType.isInterface()) {
				proxy = Proxy.newProxyInstance(fieldType.getClassLoader(), new Class[] { fieldType },
						(proxyObj, method, args) -> {
							log.info("Inside ItgWebClientRegistrarV3.processField: - method: " + method + " - args: "
									+ args);
							// Retrieve annotation attributes
							String id = annotation.id();
							String url = annotation.url();
							String[] headers = annotation.headers();

							log.debug("Inside AppAnnotationRegistrar: id:" + id + " - url: " + url + " - headers: "
									+ headers);

							return itgWebClientImpl.execute(id, fieldType.getName(), method, annotation, true, args);
						});
			} else {
				log.info("Not an interface - this cannot happen !!!!");
				// proxy = // Create CGLIB proxy using the original approach
			}
			ReflectionUtils.setField(field, bean, proxy);
		}
	}

}
