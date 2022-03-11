package com.televisory.user.daoimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.televisory.capitalmarket.entities.cm.UserPeerInfo;
import com.televisory.capitalmarket.entities.user.UserDataScoringComment;
import com.televisory.capitalmarket.util.DozerHelper;
import com.televisory.user.dao.TelevisoryUserRepository;
import com.televisory.user.dto.UserDataScoringCommentDto;
import com.televisory.user.dto.UserPeerInfoDTO;

@Repository
@Transactional
public class TelevisoryUserRepositoryImpl implements TelevisoryUserRepository{ 

	Logger _log = Logger.getLogger(this.getClass());

	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory sessionFactory;

	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmSessionFactory;

	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@SuppressWarnings("unchecked")
	@Override
	public List<UserPeerInfoDTO> getPeerCompanyList(String userId, String applicableToDate, String applicableFromDate) {
		_log.info("userId "+userId + " applicableDate "+applicableToDate +" applicableDate "+applicableFromDate);
		//String baseQuery="SELECT * FROM cm.user_peer_info_af WHERE user_id = :userId AND applicable_date = :applicableDate AND is_active=1";
		String baseQuery="SELECT * FROM cm.user_peer_info_af WHERE user_id = :userId and is_active=1 ";
		if(applicableToDate!=null && !applicableToDate.equals("") && applicableFromDate!=null && !applicableFromDate.equals("")) {
			baseQuery+=" AND applicable_date between :applicableFromDate and :applicableToDate ";
		}else if(applicableToDate!=null && !applicableToDate.equals("")) {
			baseQuery+=" AND applicable_date = :applicableToDate ";
		}
		baseQuery+=" order by applicable_date desc  ";
		_log.info(" query to get Peer company "+baseQuery);

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(UserPeerInfo.class);
		query.setParameter("userId", userId);

		if(applicableToDate!=null && !applicableToDate.equals("") && applicableFromDate!=null && !applicableFromDate.equals("")) {
			query.setParameter("applicableFromDate",applicableFromDate);
			query.setParameter("applicableToDate",applicableToDate);
		}else if(applicableToDate!=null && !applicableToDate.equals("")) {
			query.setParameter("applicableToDate",applicableToDate);
		}

		//query.setParameter("applicableDate",applicableToDate);

		List<UserPeerInfo> userPeerInfo = null;
		List<UserPeerInfoDTO> userPeerDTO=null;
		try {		
			userPeerInfo = query.list();	
			if(userPeerInfo!=null && userPeerInfo.size()>0) {
				userPeerDTO = DozerHelper.map(dozerBeanMapper, userPeerInfo, UserPeerInfoDTO.class);
			}
		} catch (Exception e) {
			throw e;
		}
		return userPeerDTO;
	}

}
