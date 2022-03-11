package com.pcompany.dto;

import java.util.List;

public class MNADealTermSynopsis {

	private String synopsis;
	
	private List<MNABalanceModelDTO> balanceModel;
	
	private MNADealMetaDataDTO dealMetaData;
	
	private List<MNADealAdvisorDTO> dealAdvisors;
	
	List<MNADealTermDTO> dealTerms;

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public List<MNABalanceModelDTO> getBalanceModel() {
		return balanceModel;
	}

	public void setBalanceModel(List<MNABalanceModelDTO> balanceModel) {
		this.balanceModel = balanceModel;
	}

	public MNADealMetaDataDTO getDealMetaData() {
		return dealMetaData;
	}

	public void setDealMetaData(MNADealMetaDataDTO dealMetaData) {
		this.dealMetaData = dealMetaData;
	}

	public List<MNADealAdvisorDTO> getDealAdvisors() {
		return dealAdvisors;
	}

	public void setDealAdvisors(List<MNADealAdvisorDTO> dealAdvisors) {
		this.dealAdvisors = dealAdvisors;
	}

	public List<MNADealTermDTO> getDealTerms() {
		return dealTerms;
	}

	public void setDealTerms(List<MNADealTermDTO> dealTerms) {
		this.dealTerms = dealTerms;
	}

	@Override
	public String toString() {
		return "MNADealTermSynopsis [synopsis=" + synopsis + ", balanceModel=" + balanceModel + ", dealMetaData="
				+ dealMetaData + ", dealAdvisors=" + dealAdvisors + ", dealTerms=" + dealTerms + "]";
	}
	
}
