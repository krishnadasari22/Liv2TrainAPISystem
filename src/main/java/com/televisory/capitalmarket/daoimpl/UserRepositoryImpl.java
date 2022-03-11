package com.televisory.capitalmarket.daoimpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.televisory.capitalmarket.dao.UserRepository;
import com.televisory.capitalmarket.entities.user.UserDataScoringComment;
import com.televisory.capitalmarket.util.DozerHelper;
import com.televisory.user.UserModel;
import com.televisory.user.UserModelDTO;
import com.televisory.user.dto.UserDataScoringCommentDto;
@Repository
@Transactional
@SuppressWarnings("rawtypes")
public class UserRepositoryImpl implements UserRepository{
	Logger _log = Logger.getLogger(this.getClass());


	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory sessionFactory;

	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmSessionFactory;

	@Autowired
	DozerBeanMapper dozerBeanMapper;

	public List<UserModelDTO> getUserFinancial(String userId, String financialType, String periodType,
			Date startDate, Date endDate, String currency) {
		_log.info("getting financial Data for userId : "+ userId +", financial Type: "+ financialType +", period Type: "+ periodType +", start Date: "+ startDate +", end Date: "+ endDate +", currency: "+ currency );

		String procedureQuery = "CALL cm.get_user_records(:in_user_id,:in_period_type,:in_financial_data_type,:in_start_date,:in_end_date,:in_target_currency)";


		Query financialDataQuery = cmSessionFactory.getCurrentSession()
				.createSQLQuery(procedureQuery)
				.addEntity(UserModel.class);

		String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);

		String stDate = null;
		String enDate = null;

		if(startDate!=null)
			stDate=df.format(startDate);

		if(endDate!=null)
			enDate=df.format(endDate);

		financialDataQuery.setParameter("in_user_id",userId);
		financialDataQuery.setParameter("in_period_type",periodType);
		financialDataQuery.setParameter("in_financial_data_type",financialType);
		financialDataQuery.setParameter("in_start_date",stDate);
		financialDataQuery.setParameter("in_end_date",enDate);
		financialDataQuery.setParameter("in_target_currency",currency);

		List<UserModel> userModel = null;
		List<UserModelDTO> userModelDTO=null;

		try {		
			userModel = financialDataQuery.list();	
			userModelDTO = DozerHelper.map(dozerBeanMapper, userModel, UserModelDTO.class);
			userModelDTO.sort(Comparator.comparing(UserModelDTO::getApplicablePeriod).reversed());
		} catch (Exception e) {
			throw e;
		}

		return userModelDTO;
	}
	
	
	public List<UserModelDTO> getCompanyFinancial(String userId, String financialType, String periodType,
			Date startDate, Date endDate, String currency) {
		_log.info("getting financial Data for userId : "+ userId +", financial Type: "+ financialType +", period Type: "+ periodType +", start Date: "+ startDate +", end Date: "+ endDate +", currency: "+ currency );

		String procedureQuery = "CALL cm.get_user_records(:in_user_id,:in_period_type,:in_financial_data_type,:in_start_date,:in_end_date,:in_target_currency)";


		Query financialDataQuery = cmSessionFactory.getCurrentSession()
				.createSQLQuery(procedureQuery)
				.addEntity(UserModel.class);

		String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);

		String stDate = null;
		String enDate = null;

		if(startDate!=null)
			stDate=df.format(startDate);

		if(endDate!=null)
			enDate=df.format(endDate);

		financialDataQuery.setParameter("in_user_id",userId);
		financialDataQuery.setParameter("in_period_type",periodType);
		financialDataQuery.setParameter("in_financial_data_type",financialType);
		financialDataQuery.setParameter("in_start_date",stDate);
		financialDataQuery.setParameter("in_end_date",enDate);
		financialDataQuery.setParameter("in_target_currency",currency);

		List<UserModel> userModel = null;
		List<UserModelDTO> userModelDTO=null;

		try {		
			userModel = financialDataQuery.list();	
			userModelDTO = DozerHelper.map(dozerBeanMapper, userModel, UserModelDTO.class);
			userModelDTO.sort(Comparator.comparing(UserModelDTO::getApplicablePeriod).reversed());
		} catch (Exception e) {
			throw e;
		}

		return userModelDTO;
	}
	
	

	public List<UserModelDTO> getUserSensitivityAnalysis(String userId, String financialType, String periodType,
			Date startDate, Date endDate, String currency) {
		_log.info("getting tax sensitivity Data for userId : "+ userId +", financial Type: "+ financialType +", period Type: "+ periodType +", start Date: "+ startDate +", end Date: "+ endDate +", currency: "+ currency );

		String procedureQuery = "CALL cm.get_user_ts_records(:in_user_id,:in_period_type,:in_financial_data_type,:in_start_date,:in_end_date,:in_target_currency)";


		Query financialDataQuery = cmSessionFactory.getCurrentSession()
				.createSQLQuery(procedureQuery)
				.addEntity(UserModel.class);

		String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);

		String stDate = null;
		String enDate = null;

		if(startDate!=null)
			stDate=df.format(startDate);

		if(endDate!=null)
			enDate=df.format(endDate);

		financialDataQuery.setParameter("in_user_id",userId);
		financialDataQuery.setParameter("in_period_type",periodType);
		financialDataQuery.setParameter("in_financial_data_type",financialType);
		financialDataQuery.setParameter("in_start_date",stDate);
		financialDataQuery.setParameter("in_end_date",enDate);
		financialDataQuery.setParameter("in_target_currency",currency);

		List<UserModel> userModel = null;
		List<UserModelDTO> userModelDTO=null;

		try {		
			userModel = financialDataQuery.list();	
			userModelDTO = DozerHelper.map(dozerBeanMapper, userModel, UserModelDTO.class);
			userModelDTO.sort(Comparator.comparing(UserModelDTO::getApplicablePeriod).reversed());
		} catch (Exception e) {
			throw e;
		}

		return userModelDTO;
	}

	public List<UserModelDTO> getUserDebtTrap(String userId, String financialType, String periodType,
			Date startDate, Date endDate, String currency) {
		_log.info("getting debt trap anlysis Data for userId : "+ userId +", financial Type: "+ financialType +", period Type: "+ periodType +", start Date: "+ startDate +", end Date: "+ endDate +", currency: "+ currency );

		String procedureQuery = "CALL cm.get_user_dta_records(:in_user_id,:in_period_type,:in_financial_data_type,:in_start_date,:in_end_date,:in_target_currency)";


		Query financialDataQuery = cmSessionFactory.getCurrentSession()
				.createSQLQuery(procedureQuery)
				.addEntity(UserModel.class);

		String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);

		String stDate = null;
		String enDate = null;

		if(startDate!=null)
			stDate=df.format(startDate);

		if(endDate!=null)
			enDate=df.format(endDate);

		financialDataQuery.setParameter("in_user_id",userId);
		financialDataQuery.setParameter("in_period_type",periodType);
		financialDataQuery.setParameter("in_financial_data_type",financialType);
		financialDataQuery.setParameter("in_start_date",stDate);
		financialDataQuery.setParameter("in_end_date",enDate);
		financialDataQuery.setParameter("in_target_currency",currency);

		List<UserModel> userModel = null;
		List<UserModelDTO> userModelDTO=null;

		try {		
			userModel = financialDataQuery.list();	
			userModelDTO = DozerHelper.map(dozerBeanMapper, userModel, UserModelDTO.class);
			userModelDTO.sort(Comparator.comparing(UserModelDTO::getApplicablePeriod).reversed());
		} catch (Exception e) {
			throw e;
		}

		return userModelDTO;
	}

	public List<UserModelDTO> getUserDebtSizing(String userId, String financialType, String periodType,
			Date startDate, Date endDate, String currency) {
		_log.info("getting debt sizing Data for userId : "+ userId +", financial Type: "+ financialType +", period Type: "+ periodType +", start Date: "+ startDate +", end Date: "+ endDate +", currency: "+ currency );

		String procedureQuery = "CALL cm.get_user_ds_records(:in_user_id,:in_period_type,:in_financial_data_type,:in_start_date,:in_end_date,:in_target_currency)";


		Query financialDataQuery = cmSessionFactory.getCurrentSession()
				.createSQLQuery(procedureQuery)
				.addEntity(UserModel.class);

		String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);

		String stDate = null;
		String enDate = null;

		if(startDate!=null)
			stDate=df.format(startDate);

		if(endDate!=null)
			enDate=df.format(endDate);

		financialDataQuery.setParameter("in_user_id",userId);
		financialDataQuery.setParameter("in_period_type",periodType);
		financialDataQuery.setParameter("in_financial_data_type",financialType);
		financialDataQuery.setParameter("in_start_date",stDate);
		financialDataQuery.setParameter("in_end_date",enDate);
		financialDataQuery.setParameter("in_target_currency",currency);

		List<UserModel> userModel = null;
		List<UserModelDTO> userModelDTO=null;

		try {		
			userModel = financialDataQuery.list();	
			userModelDTO = DozerHelper.map(dozerBeanMapper, userModel, UserModelDTO.class);
			userModelDTO.sort(Comparator.comparing(UserModelDTO::getApplicablePeriod).reversed());
		} catch (Exception e) {
			throw e;
		}

		return userModelDTO;
	}
	public List<UserModelDTO> getPeerData(String userId, String financialType, String periodType,
			Date startDate, Date endDate, String currency, String peerCalculation, String parameterName) {

		_log.info("getting PeerFinancial Data for userId : "+ userId +", financial Type: "+ financialType +", period Type: "+ periodType +", start Date: "+ startDate +", end Date: "+ endDate +", currency: "+ currency +" parameterName :"+parameterName+" peercalculation : "+peerCalculation);

		String procedureQuery = "CALL cm.get_user_peer_records(:in_user_id,:in_period_type,:in_financial_data_type,:in_start_date,:in_end_date,:in_target_currency,:in_peer_calculation,:in_params)";


		Query financialDataQuery = cmSessionFactory.getCurrentSession()
				.createSQLQuery(procedureQuery)
				.addEntity(UserModel.class);

		String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);

		String stDate = null;
		String enDate = null;

		if(startDate!=null)
			stDate=df.format(startDate);

		if(endDate!=null)
			enDate=df.format(endDate);

		financialDataQuery.setParameter("in_user_id",userId);
		financialDataQuery.setParameter("in_period_type",periodType);
		financialDataQuery.setParameter("in_financial_data_type",financialType);
		financialDataQuery.setParameter("in_start_date",stDate);
		financialDataQuery.setParameter("in_end_date",enDate);
		financialDataQuery.setParameter("in_target_currency",currency);
		financialDataQuery.setParameter("in_peer_calculation",peerCalculation);
		financialDataQuery.setParameter("in_params",parameterName);
		List<UserModel> userModel = null;
		List<UserModelDTO> userModelDTO=null;

		try {		
			userModel = financialDataQuery.list();	
			userModelDTO = DozerHelper.map(dozerBeanMapper, userModel, UserModelDTO.class);
			if(userModelDTO!=null){
				userModelDTO.sort(Comparator.comparing(UserModelDTO::getApplicablePeriod).reversed());
			}
		} catch (Exception e) {
			throw e;
		}

		return userModelDTO;
	}
	public List<UserModelDTO> getScore(String userId, String financialType, String periodType,
			Date startDate, Date endDate, String peerCalculation, String parameterName) {
		_log.info("getting scoring Data for userId : "+ userId +", financial Type: "+ financialType +", period Type: "+ periodType +", start Date: "+ startDate +", end Date: "+ endDate +" parameterName :"+parameterName+" peercalculation : "+peerCalculation);

		String procedureQuery = "CALL cm.get_user_scoring_records(:in_user_id,:in_period_type,:in_financial_data_type,:in_start_date,:in_end_date,:in_peer_calculation,:in_params)";


		Query financialDataQuery = cmSessionFactory.getCurrentSession()
				.createSQLQuery(procedureQuery)
				.addEntity(UserModel.class);

		String pattern = "yyyy-MM-dd";
		DateFormat df = new SimpleDateFormat(pattern);

		String stDate = null;
		String enDate = null;

		if(startDate!=null)
			stDate=df.format(startDate);

		if(endDate!=null)
			enDate=df.format(endDate);

		financialDataQuery.setParameter("in_user_id",userId);
		financialDataQuery.setParameter("in_period_type",periodType);
		financialDataQuery.setParameter("in_financial_data_type",financialType);
		financialDataQuery.setParameter("in_start_date",stDate);
		financialDataQuery.setParameter("in_end_date",enDate);
		financialDataQuery.setParameter("in_peer_calculation",peerCalculation);
		financialDataQuery.setParameter("in_params",parameterName);

		List<UserModel> userModel = null;
		List<UserModelDTO> userModelDTO=null;

		try {		
			userModel = financialDataQuery.list();	
			userModelDTO = DozerHelper.map(dozerBeanMapper, userModel, UserModelDTO.class);
			userModelDTO.sort(Comparator.comparing(UserModelDTO::getApplicablePeriod).reversed());
		} catch (Exception e) {
			throw e;
		}

		return userModelDTO;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserDataScoringCommentDto> getUserScoreComment(String userId, List<String> fieldName, Date period) {
		_log.info("Getting the user score comment for ::::::: userId "+userId + " fieldName "+ fieldName + " period "+period);
		String baseQuery="SELECT * FROM cm.user_data_scoring_comment_af WHERE user_id = :userId and applicable_date =:applicableDate";
		// baseQuery+=" and fieldName in(:fieldName)";
		//baseQuery+=" order by applicable_date desc  ";
		_log.info(" query to get Peer company "+baseQuery);

		Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(UserDataScoringComment.class);
		query.setParameter("userId", userId);
		query.setParameter("applicableDate",period);
		//query.setParameter("fieldName",fieldName);
		List<UserDataScoringComment> userDataScoringComment = null;
		List<UserDataScoringCommentDto> userDataScoringCommentDto = null;
		try {		
			userDataScoringComment = query.list();	
			if(userDataScoringComment!=null && userDataScoringComment.size()>0) {
				userDataScoringCommentDto = DozerHelper.map(dozerBeanMapper, userDataScoringComment, UserDataScoringCommentDto.class);
			}

		} catch (Exception e) {
			throw e;
		}
		return userDataScoringCommentDto;
	}
	
	


}
