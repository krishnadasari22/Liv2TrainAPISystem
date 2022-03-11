package com.televisory.capitalmarket.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * @author vinay
 *
 */
@Configuration
@EnableTransactionManagement
public class CMDatabaseConfig {
	
	@Value("${CM.DB.DRIVER}")
	private String DRIVER;

	@Value("${CM.DB.PASSWORD}")
	private String PASSWORD;

	@Value("${CM.DB.URL}")
	private String cmURL;

	@Value("${CM.DB.USERNAME}")
	private String USERNAME;

	@Value("${CM.HIBERNATE.DIALECT}")
	private String DIALECT;

	@Value("${CM.HIBERNATE.SHOW_SQL}")
	private String SHOW_SQL;

	@Value("${entitymanager.packagesToScan}")
	private String PACKAGES_TO_SCAN;


	@Primary
	@Bean(name = "cmDataSource")
	public DataSource cmdataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(DRIVER);
		dataSource.setUrl(cmURL);
		dataSource.setUsername(USERNAME);
		dataSource.setPassword(PASSWORD);
		return dataSource;
	}

	@Primary
	@Bean(name = "cmSessionFactory")
	public LocalSessionFactoryBean cmSessionFactory() {

		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

		sessionFactory.setDataSource(cmdataSource());
		sessionFactory.setPackagesToScan(PACKAGES_TO_SCAN);

		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", DIALECT);
		hibernateProperties.put("hibernate.show_sql", SHOW_SQL);

		sessionFactory.setHibernateProperties(hibernateProperties);
		return sessionFactory;

	}

	@Primary
	@Bean(name = "cmTransactionManger")
	public HibernateTransactionManager cmTransactionManager() {
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(cmSessionFactory().getObject());
		return txManager;
	}
}
