/*package com.televisory.capitalmarket.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{

	Logger _log = Logger.getLogger(WebSecurity.class);

	@Value("${CM.ALLOWED.IPADDR}")
	 public String ipAddresses;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
			
	String[] ipaddressList =ipAddresses.split(",");
	
	String allowedIpAddr =null;
	
	for(int i=0;i<ipaddressList.length;i++){
		
		if (i==0)
			allowedIpAddr="hasIpAddress('"+ipaddressList[i].trim()+"')";
		else
			allowedIpAddr=allowedIpAddr+" or hasIpAddress('"+ipaddressList[i].trim()+"')" ;
	}	
	
	_log.info("Allowed Ips are:-"+ipAddresses);
	
	  http.authorizeRequests()
		.anyRequest().access(allowedIpAddr);
	}
}
*/