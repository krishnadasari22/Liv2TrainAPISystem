package com.pcompany.model;

import java.io.Serializable;
import java.util.List;

import com.pcompany.dto.PEVCFundingDTO;


public class PEVCFundDetailResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long totalCount;

	private String currency;

	private String unit;
	
	List<PEVCFundingDTO> pevcFundingList;

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<PEVCFundingDTO> getPevcFundingList() {
		return pevcFundingList;
	}

	public void setPevcFundingList(List<PEVCFundingDTO> pevcFundingList) {
		this.pevcFundingList = pevcFundingList;
	}

}
