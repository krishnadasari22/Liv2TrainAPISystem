package com.televisory.capitalmarket.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class FactSetConfig {

	@Value("${CM.DB.DRIVER}") 
    private String DRIVER; 
 
    @Value("${CM.DB.PASSWORD}")
    private String PASSWORD;
  
    @Value("${CM.FACTSET.DB.URL}")
    private String factSetURL;
 
    @Value("${CM.DB.USERNAME}")
    private String USERNAME;
	
	@Value("${CM.HIBERNATE.DIALECT}")
    private String DIALECT;
 
    @Value("${CM.HIBERNATE.SHOW_SQL}")
    private String SHOW_SQL;
 
    @Value("${entitymanager.packagesToScan}")
    private String PACKAGES_TO_SCAN;

    @Bean(name="factSetDataSource")
   	public DataSource macrodataSource() {
   		DriverManagerDataSource dataSource = new DriverManagerDataSource();
   		dataSource.setDriverClassName(DRIVER);
   		dataSource.setUrl(factSetURL);
   		dataSource.setUsername(USERNAME);
   		dataSource.setPassword(PASSWORD);
   		return dataSource;
   	}
       
   @Bean(name="factSetSessionFactory")
   public LocalSessionFactoryBean macroSessionFactory() {
       
   	LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
       
       sessionFactory.setDataSource(macrodataSource());
       sessionFactory.setPackagesToScan(PACKAGES_TO_SCAN);
       
       Properties hibernateProperties = new Properties();
       hibernateProperties.put("hibernate.dialect",DIALECT);
       hibernateProperties.put("hibernate.show_sql", SHOW_SQL);
    
       sessionFactory.setHibernateProperties(hibernateProperties);
       return sessionFactory;
   
   
   }
       
   @Bean(name="factSetTransactionManger")
   public HibernateTransactionManager macroTransactionManager() {
       HibernateTransactionManager txManager = new HibernateTransactionManager();
       txManager.setSessionFactory(macroSessionFactory().getObject());
       return txManager;
   }
	
}
