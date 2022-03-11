package com.pcompany.daoimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.pcompany.dao.FactSetOnDemandDao;
import com.pcompany.entity.CompanyDocumentMetadata;
import com.pcompany.entity.FactSetOnDemandSessionInfo;

@Repository
@Transactional
public class FactsetOnDemandDaoImpl implements FactSetOnDemandDao {
	
	private static final Logger _log = Logger.getLogger(FactsetOnDemandDaoImpl.class);
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;
	
	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmSessionFactory;

	@Override
	public CompanyDocumentMetadata getCompanyDocumentMetadata(String recordKey) {
		
		_log.info("Getting Company Document Metadata for record key "+recordKey+" from database");
		
		String baseQuery= "from CompanyDocumentMetadata where key = :recordKey ";

		Query query = cmSessionFactory.getCurrentSession().createQuery(baseQuery);
		query.setParameter("recordKey", recordKey);
		
		/*CompanyDocumentMetadata metadata =  (CompanyDocumentMetadata) query.uniqueResult();*/
		@SuppressWarnings("unchecked")
		List<CompanyDocumentMetadata> metadatas =  query.list();
		if(!metadatas.isEmpty()){
			return metadatas.get(0);
		}else{
			return null;
		}
	}
	
	@Override
	public FactSetOnDemandSessionInfo getFactSetOndemandSesssionInfo(String environment, String accessLevel) {
		
		_log.info("Getting the session Details for environment: "+ environment +", accessLevel: "+ accessLevel);
		
		String baseQuery= "from FactSetOnDemandSessionInfo where environment = :environment and accessLevel = :accessLevel";

		Query query = cmSessionFactory.getCurrentSession().createQuery(baseQuery);
		query.setParameter("environment", environment);
		query.setParameter("accessLevel", accessLevel);
		
		FactSetOnDemandSessionInfo sessionInfo =  (FactSetOnDemandSessionInfo) query.uniqueResult();

		return sessionInfo;
	}

	@Override
	public void updateSessionInfo(FactSetOnDemandSessionInfo factSetOnDemandSessionInfo) {
		
		_log.info("Updating the session Details counter "+factSetOnDemandSessionInfo.getCounter()+" session Token "+factSetOnDemandSessionInfo.getSessionToken());
		
		String baseQuery= "update factset_ondemand_session_info set counter = :counter , session_token =:sessionToken where id = :id";

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		query.setParameter("counter", factSetOnDemandSessionInfo.getCounter());
		query.setParameter("sessionToken", factSetOnDemandSessionInfo.getSessionToken());
		query.setParameter("id", factSetOnDemandSessionInfo.getId());
		
		query.executeUpdate();

	}

}
