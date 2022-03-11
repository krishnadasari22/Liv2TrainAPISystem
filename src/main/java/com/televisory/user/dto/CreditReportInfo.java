package com.televisory.user.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreditReportInfo  implements Serializable {

	private static final long serialVersionUID = 2746771994428928310L;
	
	private String requestType;
	
	private String industryName;
	
	private String userId;
	
	private String applicableToYear;
	
	private String applicableFromYear;
	
	private String companyId;
	
	private String dateOfIncorporation;
	
	private String userCurrency;
	
	private String userUnit;
	
	private BasicInfoDto basicInfo;
	
	private CollateralDetailsDto collateralDetailInfo;
	
	private List<keyManagementInfoDto> keyMgmntInfoList;
	
	private List<OwnershipStructureDto> ownershipStructureList;
	
	private List<PromoterGroupOwnershipStructureDto> promoterGroupOwnershipStructure;
	
	private List<DataRequestDocDto> dataRequestDoc;
	
	List<BankUITableDto> bankUITableDtos = new ArrayList<BankUITableDto>();

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getApplicableToYear() {
		return applicableToYear;
	}

	public void setApplicableToYear(String applicableToYear) {
		this.applicableToYear = applicableToYear;
	}

	public String getApplicableFromYear() {
		return applicableFromYear;
	}

	public void setApplicableFromYear(String applicableFromYear) {
		this.applicableFromYear = applicableFromYear;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getDateOfIncorporation() {
		return dateOfIncorporation;
	}

	public void setDateOfIncorporation(String dateOfIncorporation) {
		this.dateOfIncorporation = dateOfIncorporation;
	}

	public String getUserCurrency() {
		return userCurrency;
	}

	public void setUserCurrency(String userCurrency) {
		this.userCurrency = userCurrency;
	}

	public String getUserUnit() {
		return userUnit;
	}

	public void setUserUnit(String userUnit) {
		this.userUnit = userUnit;
	}

	public BasicInfoDto getBasicInfo() {
		return basicInfo;
	}

	public void setBasicInfo(BasicInfoDto basicInfo) {
		this.basicInfo = basicInfo;
	}

	public CollateralDetailsDto getCollateralDetailInfo() {
		return collateralDetailInfo;
	}

	public void setCollateralDetailInfo(CollateralDetailsDto collateralDetailInfo) {
		this.collateralDetailInfo = collateralDetailInfo;
	}

	public List<keyManagementInfoDto> getKeyMgmntInfoList() {
		return keyMgmntInfoList;
	}

	public void setKeyMgmntInfoList(List<keyManagementInfoDto> keyMgmntInfoList) {
		this.keyMgmntInfoList = keyMgmntInfoList;
	}

	public List<OwnershipStructureDto> getOwnershipStructureList() {
		return ownershipStructureList;
	}

	public void setOwnershipStructureList(
			List<OwnershipStructureDto> ownershipStructureList) {
		this.ownershipStructureList = ownershipStructureList;
	}

	public List<PromoterGroupOwnershipStructureDto> getPromoterGroupOwnershipStructure() {
		return promoterGroupOwnershipStructure;
	}

	public void setPromoterGroupOwnershipStructure(
			List<PromoterGroupOwnershipStructureDto> promoterGroupOwnershipStructure) {
		this.promoterGroupOwnershipStructure = promoterGroupOwnershipStructure;
	}

	public List<DataRequestDocDto> getDataRequestDoc() {
		return dataRequestDoc;
	}

	public void setDataRequestDoc(List<DataRequestDocDto> dataRequestDoc) {
		this.dataRequestDoc = dataRequestDoc;
	}

	public List<BankUITableDto> getBankUITableDtos() {
		return bankUITableDtos;
	}

	public void setBankUITableDtos(List<BankUITableDto> bankUITableDtos) {
		this.bankUITableDtos = bankUITableDtos;
	}

	@Override
	public String toString() {
		return "CreditReportInfo [requestType=" + requestType
				+ ", industryName=" + industryName + ", userId=" + userId
				+ ", applicableToYear=" + applicableToYear
				+ ", applicableFromYear=" + applicableFromYear + ", companyId="
				+ companyId + ", dateOfIncorporation=" + dateOfIncorporation
				+ ", userCurrency=" + userCurrency + ", userUnit=" + userUnit
				+ ", basicInfo=" + basicInfo + ", collateralDetailInfo="
				+ collateralDetailInfo + ", keyMgmntInfoList="
				+ keyMgmntInfoList + ", ownershipStructureList="
				+ ownershipStructureList + ", promoterGroupOwnershipStructure="
				+ promoterGroupOwnershipStructure + ", dataRequestDoc="
				+ dataRequestDoc + ", bankUITableDtos=" + bankUITableDtos + "]";
	}

}
