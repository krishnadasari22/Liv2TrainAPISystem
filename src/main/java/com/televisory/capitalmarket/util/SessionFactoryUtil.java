package com.televisory.capitalmarket.util;

import org.springframework.stereotype.Component;

@Component
//@Scope(name = "prototype", description = "")
public class SessionFactoryUtil {
	
	/*@Autowired
	@Qualifier(value="bseSessionFactory")
	private SessionFactory bseSessionFactory;
	
	@Autowired
	@Qualifier(value="idxSessionFactory")
	private SessionFactory idxSessionFactory;
	
	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;
	
	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmSessionFactory;
	
	public SessionFactory getSessionFactory(String exchangeType){
		
		SessionFactory sessionFactory = null;
		
		switch (exchangeType.toUpperCase()) {
			case CMStatic.EXCHANGE_BSE:
				sessionFactory= bseSessionFactory;
			break;
			case CMStatic.EXCHANGE_IDX:
				sessionFactory=idxSessionFactory;
			break;
			case CMStatic.FACTSET:
				sessionFactory=factSetSessionFactory;
			break;
			case CMStatic.CM:
				sessionFactory=cmSessionFactory;
			break;
		
			default :
				
		}
		return sessionFactory;
	}*/

}
