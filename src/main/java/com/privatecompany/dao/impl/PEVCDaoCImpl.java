package com.privatecompany.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.pcompany.dto.PEVCFundingDTO;
import com.pcompany.dto.PEVCFundingInvestmentDTO;
import com.pcompany.entity.PEVCFundingDetail;
import com.pcompany.entity.PEVCFundingInvestment;
import com.privatecompany.dao.PEVCDaoC;
import com.televisory.capitalmarket.util.DozerHelper;
import com.televisory.utils.PEVCFundingDetailAdvancedSerchedQueries;
import com.televisory.utils.PEVCFundingDetailQueries;

@Repository
@Transactional
public class PEVCDaoCImpl implements PEVCDaoC {
	
	Logger _log = Logger.getLogger(PEVCDaoCImpl.class);

	@Autowired
	@Qualifier(value="factSetSessionFactory")
	private SessionFactory factSetSessionFactory;
	
	@Autowired
	@Qualifier(value="cmSessionFactory")
	private SessionFactory cmSessionFactory;
	
	@Autowired
	DozerBeanMapper dozerBeanMapper;

	@Override
	public List<PEVCFundingDTO> getFundingDetailList(String country,
			Date startDate, Date endDate, String industry,
			String currency, String financingType, String entityId,
			Integer rowOffset, Integer rowCount, String sortingColumn, String sortingType) {
		_log.info("getting pevc funding detail list for country:"+country+" startDate:"+startDate+" endDate:"+endDate
				+" industry:"+industry+" currency:"+currency+" financingType:"+financingType+" entityId:"+entityId+" rowOffset:"
				+rowOffset+" rowCount:"+rowCount+" sortingColumn:"+sortingColumn+" sortingType:"+sortingType);
		
		boolean selectCountry = false;
	 	boolean selectIndustry = false;
	 	boolean selectCompany = false;
	    String baseQuery = null;
	    
	    /*if((entityId!=null && !"".equals(entityId) && !"null".equals(entityId)) || 
	    		(financingType==null || "".equals(financingType) || "null".equals(financingType) || "all".equalsIgnoreCase(financingType))){
	    	baseQuery=PEVCFundingDetailQueries.pevcFundDetailFinTypeNotSelected;
	    }*/if(financingType==null || "".equals(financingType) || "null".equals(financingType) || "all".equalsIgnoreCase(financingType)){
	    	baseQuery=PEVCFundingDetailQueries.pevcFundDetailFinTypeNotSelected;
	    }else{
	    	if("VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailQueries.pevcFundDetailFinTypeVC;
		    }else if("PE".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailQueries.pevcFundDetailFinTypePE;
		    }else if("OT".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailQueries.pevcFundDetailFinTypeOT;
		    }else if("VC,PE".equalsIgnoreCase(financingType) || "PE,VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailQueries.pevcFundDetailFinTypeVCPE;
		    }else {
		    	return null;
		    }	    	
	    }
	    
	    String countryIndustryEntityFilter = "";
	    String entityIdFilter = "";
	    if(entityId==null || "".equals(entityId) || "null".equals(entityId)){
	    	if(country!=null && !"World".equalsIgnoreCase(country) && !"Global".equalsIgnoreCase(country) && !"".equals(country) && !"null".equals(country)){
				countryIndustryEntityFilter+="AND c.country_iso_code_3 = :country ";
		    	selectCountry = true;
		    }
	    	if(industry!=null && !"All".equals(industry) && !"".equals(industry) && !"null".equals(industry)){
	    		countryIndustryEntityFilter+="AND f.tics_industry_code = :industry ";
		    	selectIndustry = true;
		    }/*else{
		    	countryIndustryEntityFilter+="AND f.factset_industry like '%Industrial%' ";
		    }*/
	    }else{
	    	countryIndustryEntityFilter+="AND a.factset_portco_entity_id = :entityId ";
	    	//countryIndustryEntityFilter+="AND f.factset_industry like '%Industrial%' ";
	    	entityIdFilter+="WHERE factset_entity_id = :entityId ";
	    	selectCompany = true;	    	
	    }
	    
	    baseQuery = baseQuery.replace("#country_industry_entity_filter_template", countryIndustryEntityFilter);
	    baseQuery = baseQuery.replace("#entityIdFilter", entityIdFilter);
	    
	    if(sortingColumn!=null && !"".equals(sortingColumn) && sortingType!=null && !"".equals(sortingType)){
	    	baseQuery = baseQuery.replace("#sorting_template", sortingColumn+" "+sortingType);
	    }else{
	    	baseQuery = baseQuery.replace("#sorting_template", "inception_date desc");
	    }
	    
	    Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(PEVCFundingDetail.class);
		if(selectCountry){
			query.setParameter("country", country);	
		}
		if(selectIndustry){
			query.setParameter("industry", industry);	
		}
		if(selectCompany){
			query.setParameter("entityId", entityId);	
		}
		query.setParameter("currency", currency);
		query.setDate("startDate", startDate);
		query.setDate("endDate", endDate);
		query.setParameter("rowOffset", rowOffset);
		query.setParameter("rowCount", rowCount);
	    
		@SuppressWarnings("unchecked")
		List<PEVCFundingDetail> data = (List<PEVCFundingDetail>) query.list();
		List<PEVCFundingDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, PEVCFundingDTO.class);	
		return dcsDTOs;
	}

	@Override
	public Long getFundingDetailCount(String country, Date startDate,
			Date endDate, String industry, String currency,
			String financingType, String entityId) {
		_log.info("getting pevc funding detail list count for country:"+country+" startDate:"+startDate+" endDate:"+endDate
				+" industry:"+industry+" currency:"+currency+" financingType:"+financingType+" entityId:"+entityId);
		
		boolean selectCountry = false;
	 	boolean selectIndustry = false;
	 	boolean selectCompany = false;
	    String baseQuery = null;
	    
	    /*if((entityId!=null && !"".equals(entityId) && !"null".equals(entityId)) || 
	    		(financingType==null || "".equals(financingType) || "null".equals(financingType) || "all".equalsIgnoreCase(financingType))){
	    	baseQuery=PEVCFundingDetailQueries.pevcFundDetailFinTypeNotSelectedCount;
	    }*/if(financingType==null || "".equals(financingType) || "null".equals(financingType) || "all".equalsIgnoreCase(financingType)){
	    	baseQuery=PEVCFundingDetailQueries.pevcFundDetailFinTypeNotSelectedCount;
	    }else{
	    	if("VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailQueries.pevcFundDetailFinTypeVCCount;
		    }else if("PE".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailQueries.pevcFundDetailFinTypePECount;
		    }else if("OT".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailQueries.pevcFundDetailFinTypeOTCount;
		    }else if("VC,PE".equalsIgnoreCase(financingType) || "PE,VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailQueries.pevcFundDetailFinTypeVCPECount;
		    }else {
		    	return null;
		    }	    	
	    }
	    
	    String countryIndustryEntityFilter = "";
	    if(entityId==null || "".equals(entityId) || "null".equals(entityId)){
	    	if(country!=null && !"World".equalsIgnoreCase(country) && !"Global".equalsIgnoreCase(country) && !"".equals(country) && !"null".equals(country)){
				countryIndustryEntityFilter+="AND c.country_iso_code_3 = :country ";
		    	selectCountry = true;
		    }
	    	if(industry!=null && !"All".equals(industry) && !"".equals(industry) && !"null".equals(industry)){
	    		countryIndustryEntityFilter+="AND f.tics_industry_code = :industry ";
		    	selectIndustry = true;
		    }/*else{
		    	countryIndustryEntityFilter+="AND f.factset_industry like '%Industrial%' ";
		    }*/
	    }else{
	    	countryIndustryEntityFilter+="AND a.factset_portco_entity_id = :entityId ";
	    	//countryIndustryEntityFilter+="AND f.factset_industry like '%Industrial%' ";
	    	selectCompany = true;	    	
	    }
	    
	    baseQuery = baseQuery.replace("#country_industry_entity_filter_template", countryIndustryEntityFilter);
	    
	    Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		if(selectCountry){
			query.setParameter("country", country);	
		}
		if(selectIndustry){
			query.setParameter("industry", industry);	
		}
		if(selectCompany){
			query.setParameter("entityId", entityId);	
		}
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
	    
		@SuppressWarnings("unchecked")
		BigInteger data = (BigInteger) query.uniqueResult();
		if(data != null){
			return data.longValue();	
		}else{
			return null; 
		}
	}

	@Override
	public List<PEVCFundingInvestmentDTO> getFundingInvestmentList(
			String entityId, Date startDate, Date endDate, String currency, String financingType) {
		_log.info("getting pevc funding investement list for entityId:"+entityId+" startDate:"+startDate+" endDate:"+endDate+" currency:"+currency+" financingType:"+financingType);
		
		boolean finTypeSelected = false;
		String baseQuery;
		if("VC,PE".equalsIgnoreCase(financingType) || "PE,VC".equalsIgnoreCase(financingType)){
	    	baseQuery=PEVCFundingDetailQueries.pevcFundingInvestmentListFinTypePEandVCQuery;
	    }else {
	    	baseQuery = PEVCFundingDetailQueries.pevcFundingInvestmentListQuery;
	    	finTypeSelected = true;
	    }
		
		Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(PEVCFundingInvestment.class);
		
		query.setParameter("entityId", entityId);
		query.setParameter("currency", currency);
		query.setDate("startDate", startDate);
		query.setDate("endDate", endDate);
		if(finTypeSelected){
			query.setParameter("financingType", financingType);	
		}
	    
		@SuppressWarnings("unchecked")
		List<PEVCFundingInvestment> data = (List<PEVCFundingInvestment>) query.list();
		List<PEVCFundingInvestmentDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, PEVCFundingInvestmentDTO.class);	
		return dcsDTOs;
	}

	@Override
	public List<PEVCFundingDTO> allFundingDetailAdvancedSearch(String country, Date startDate, Date endDate,
			String industry, String currency, String financingType, String entityId, String issueType,
			Double minAmount, Double maxAmount, Integer rowOffset, Integer rowCount, String sortingColumn,
			String sortingType) {

		_log.info("getting pevc funding detail list for country:"+country+" startDate:"+startDate+" endDate:"+endDate
				+" industry:"+industry+" currency:"+currency+" financingType:"+financingType+" entityId:"+entityId+" rowOffset:"
				+rowOffset+" rowCount:"+rowCount+" sortingColumn:"+sortingColumn+" sortingType:"+sortingType);
		
		boolean selectCountry = false;
	 	boolean selectIndustry = false;
	 	boolean selectCompany = false;
	 	boolean selectIssueType=false;
	    String baseQuery = null;
	    boolean isMultiPleFintype=false;
		List<String>multipleFinType=new ArrayList<String>();
	    
	    /*if((entityId!=null && !"".equals(entityId) && !"null".equals(entityId)) || 
	    		(financingType==null || "".equals(financingType) || "null".equals(financingType) || "all".equalsIgnoreCase(financingType))){
	    	baseQuery=PEVCFundingDetailQueries.pevcFundDetailFinTypeNotSelected;
	    }*/if(financingType==null || "".equals(financingType) || "null".equals(financingType) || "all".equalsIgnoreCase(financingType)){
	    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeNotSelected;
	    }else{
	    	if("VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeVC;
		    }else if("PE".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypePE;
		    }else if("OT".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeOT;
		    }else if("VC,PE".equalsIgnoreCase(financingType) || "PE,VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeVCPE;
		    }else {
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeMultiple;
		    	isMultiPleFintype=true;
		    	multipleFinType=Arrays.asList(financingType.split(","));
		    	
		    }	    	
	    }
	    
	    String countryIndustryEntityFilter = "";
	    String entityIdFilter = "";
	    String issueFilter="";
	    if(entityId==null || "".equals(entityId) || "null".equals(entityId)){
	    	if(country!=null && !"World".equalsIgnoreCase(country) && !"Global".equalsIgnoreCase(country) && !"".equals(country) && !"null".equals(country) && !Arrays.asList(country.split(",")).contains("Global")){
				countryIndustryEntityFilter+="AND c.country_iso_code_3 in (:country) ";
		    	selectCountry = true;
		    }
	    	if(industry!=null && !"All".equals(industry) && !"".equals(industry) && !"null".equals(industry) && !Arrays.asList(industry.split(",")).contains("All")){
	    		countryIndustryEntityFilter+="AND f.tics_industry_code in (:industry)";
		    	selectIndustry = true;
		    }/*else{
		    	countryIndustryEntityFilter+="AND f.factset_industry like '%Industrial%' ";
		    }*/
	    }else{
	    	countryIndustryEntityFilter+="AND a.factset_portco_entity_id in(:entityId) ";
	    	//countryIndustryEntityFilter+="AND f.factset_industry like '%Industrial%' ";
	    	entityIdFilter+="WHERE factset_entity_id in (:entityId) ";
	    	selectCompany = true;	    	
	    }
	    if(issueType!=null && !"All".equalsIgnoreCase(issueType) && !"".equals(issueType) && !"null".equals(issueType)  && !Arrays.asList(issueType.split(",")).contains("All")) {
	    	issueFilter="AND a.issue_type in (:issue_type) ";
	    	selectIssueType=true;
	    }
	    
	    baseQuery = baseQuery.replace("#country_industry_entity_filter_template", countryIndustryEntityFilter);
	    baseQuery = baseQuery.replace("#entityIdFilter", entityIdFilter);
	    baseQuery = baseQuery.replace("#issue-filter", issueFilter);
	    
	    if(sortingColumn!=null && !"".equals(sortingColumn) && sortingType!=null && !"".equals(sortingType)){
	    	baseQuery = baseQuery.replace("#sorting_template", sortingColumn+" "+sortingType);
	    }else{
	    	baseQuery = baseQuery.replace("#sorting_template", "inception_date desc");
	    }
	    
	    Query query = cmSessionFactory.getCurrentSession().createSQLQuery(baseQuery).addEntity(PEVCFundingDetail.class);
		if(selectCountry){
			query.setParameterList("country", Arrays.asList(country.split(",")));	
		}
		if(selectIndustry){
			query.setParameterList("industry", Arrays.asList(industry.split(",")));	
		}
		if(selectCompany){
			query.setParameterList("entityId", Arrays.asList(entityId.split(",")));	
		}
		if(selectIssueType) {
			query.setParameterList("issue_type", Arrays.asList(issueType.split(",")));	
		}
		
		 if(isMultiPleFintype) {
			 query.setParameterList("multipleFinType", multipleFinType);
		 }
		query.setParameter("currency", currency);
		query.setDate("startDate", startDate);
		query.setDate("endDate", endDate);
		query.setParameter("minAmount", minAmount);
		query.setParameter("maxAmount", maxAmount);
	    query.setParameter("rowOffset", rowOffset);
		query.setParameter("rowCount", rowCount);
	    
		@SuppressWarnings("unchecked")
		List<PEVCFundingDetail> data = (List<PEVCFundingDetail>) query.list();
		List<PEVCFundingDTO> dcsDTOs = DozerHelper.map(dozerBeanMapper, data, PEVCFundingDTO.class);	
		return dcsDTOs;
	
	}

	@Override
	public Long getFundingAdvancedSearchDetailCount(String country, Date startDate, Date endDate, String industry,
			String currency, String financingType, String entityId, String issueType, Double minAmount,
			Double maxAmount) {
		
		_log.info("getting pevc funding detail list count for country:"+country+" startDate:"+startDate+" endDate:"+endDate
				+" industry:"+industry+" currency:"+currency+" financingType:"+financingType+" entityId:"+entityId);
		
		boolean selectCountry = false;
	 	boolean selectIndustry = false;
	 	boolean selectCompany = false;
	 	boolean selectIssueType=false;
	    String baseQuery = null;
	    boolean isMultiPleFintype=false;
		List<String>multipleFinType=new ArrayList<String>();
	    
	    /*if((entityId!=null && !"".equals(entityId) && !"null".equals(entityId)) || 
	    		(financingType==null || "".equals(financingType) || "null".equals(financingType) || "all".equalsIgnoreCase(financingType))){
	    	baseQuery=PEVCFundingDetailQueries.pevcFundDetailFinTypeNotSelectedCount;
	    }*/if(financingType==null || "".equals(financingType) || "null".equals(financingType) || "all".equalsIgnoreCase(financingType)){
	    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeNotSelectedCount;
	    }else{
	    	if("VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeVCCount;
		    }else if("PE".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypePECount;
		    }else if("OT".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeOTCount;
		    }else if("VC,PE".equalsIgnoreCase(financingType) || "PE,VC".equalsIgnoreCase(financingType)){
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeVCPECount;
		    }else {
		    	baseQuery=PEVCFundingDetailAdvancedSerchedQueries.pevcFundDetailFinTypeMultiCount;
		    	isMultiPleFintype=true;
		    	 multipleFinType=Arrays.asList(financingType.split(","));
		    }	    	
	    }
	    
	    String countryIndustryEntityFilter = "";
	    String issueFilter="";
	    if(entityId==null || "".equals(entityId) || "null".equals(entityId)){
	    	if(country!=null && !"World".equalsIgnoreCase(country) && !"Global".equalsIgnoreCase(country) && !"".equals(country) && !"null".equals(country) && !Arrays.asList(country.split(",")).contains("Global")){
				countryIndustryEntityFilter+="AND c.country_iso_code_3 in (:country) ";
		    	selectCountry = true;
		    }
	    	if(industry!=null && !"All".equals(industry) && !"".equals(industry) && !"null".equals(industry)  && !Arrays.asList(industry.split(",")).contains("All")){
	    		countryIndustryEntityFilter+="AND f.tics_industry_code in (:industry) ";
		    	selectIndustry = true;
		    }/*else{
		    	countryIndustryEntityFilter+="AND f.factset_industry like '%Industrial%' ";
		    }*/
	    }else{
	    	countryIndustryEntityFilter+="AND a.factset_portco_entity_id in (:entityId) ";
	    	//countryIndustryEntityFilter+="AND f.factset_industry like '%Industrial%' ";
	    	selectCompany = true;	    	
	    }
	    
	    if(issueType!=null && !"All".equalsIgnoreCase(issueType) && !"".equals(issueType) && !"null".equals(issueType) && !Arrays.asList(issueType.split(",")).contains("All")) {
	    	issueFilter="AND a.issue_type in (:issueType) ";
	    	selectIssueType=true;
	    }
	    
	    baseQuery = baseQuery.replace("#country_industry_entity_filter_template", countryIndustryEntityFilter);
	    baseQuery = baseQuery.replace("#issue-filter", issueFilter);
	    
	    Query query = factSetSessionFactory.getCurrentSession().createSQLQuery(baseQuery);
		if(selectCountry){
			query.setParameterList("country", Arrays.asList(country.split(",")));	
		}
		if(selectIndustry){
			query.setParameterList("industry", Arrays.asList(industry.split(",")));	
		}
		if(selectCompany){
			query.setParameterList("entityId", Arrays.asList(entityId.split(",")));		
		}
		if(selectIssueType) {
			query.setParameterList("issueType", Arrays.asList(issueType.split(",")));
		}
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("minAmount", minAmount);	
		query.setParameter("maxAmount", maxAmount);	
		
		query.setParameter("currency", currency);
		
		if(isMultiPleFintype) {
			 query.setParameterList("multipleFinType", multipleFinType);
		 }
		@SuppressWarnings("unchecked")
		BigInteger data = (BigInteger) query.uniqueResult();
		if(data != null){
			return data.longValue();	
		}else{
			return null; 
		}
	
	}

}
