package com.televisory.capitalmarket;

import java.text.NumberFormat;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@Configuration
//@ComponentScan("com.televisory")
@ComponentScan({"com.televisory","com.pcompany","com.privatecompany"})
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class CapitalMarketMainApp extends SpringBootServletInitializer{

	private final static Logger _log = LoggerFactory.getLogger(CapitalMarketMainApp.class);

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {

		new SpringApplicationBuilder(CapitalMarketMainApp.class)
		.properties("spring.config.name:capitalmarket,application","spring.config.location:file:${user.home}/resources/")				
		.build().run(args);   
		showMemory();
	}

	// On Windows, we need an extra "/" in the file URL if it is absolute with a drive prefix (for example,file:///${user.home}/config-repo).
	// *we are not using the method mentioned in above comment because file: works on both Linux and windows environment
	//For loading external properties files (database.properties and application.properties) when application is deployed in external tomcat server
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CapitalMarketMainApp.class)
				.properties("spring.config.name:capitalmarket,application","spring.config.location:file:${user.home}/resources/");
	}  

	// to allow the known extention in rest url (.exe .deb )
	@Configuration
	public static class PathMatchingConfigurationAdapter extends WebMvcConfigurerAdapter {
		@Override
		public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
			configurer.favorPathExtension(false);
		}
	}

	@Bean
	@Primary
	public DozerBeanMapper getDozerBean() {
		return new DozerBeanMapper();
	}

	@Bean
	@Primary
	public Covariance getCovarianceBean(){
		return new Covariance();
	}


	@Bean
	@Primary
	public Variance getVarianceBean(){
		return new Variance();
	}

	public static void showMemory() {
		Runtime runtime = Runtime.getRuntime();

		final NumberFormat format = NumberFormat.getInstance();

		final long maxMemory = runtime.maxMemory();
		final long allocatedMemory = runtime.totalMemory();
		final long freeMemory = runtime.freeMemory();
		final long mb = 1024 * 1024;
		final String mega = " MB";

		_log.info("=================Memory Info=================");
		_log.info("Free memory: " + format.format(freeMemory / mb) + mega);
		_log.info("Allocated memory: " + format.format(allocatedMemory / mb) + mega);
		_log.info("Max memory: " + format.format(maxMemory / mb) + mega);
		_log.info("Total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / mb) + mega);
		_log.info("=============================================");
	}
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
