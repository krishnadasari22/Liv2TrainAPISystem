package com.televisory.user.dto;

import java.io.Serializable;
import java.util.List;

public class CollateralDetailsDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private LoanInfoDto loanInfoDto;
	
	private List<FacilityDetailDto> facilityDetailDtos;
	
	private List<AssetDetailDto> assetDetailDtos;
	
	private List<LoanBankDto> loanBankDtos;

	public LoanInfoDto getLoanInfoDto() {
		return loanInfoDto;
	}

	public void setLoanInfoDto(LoanInfoDto loanInfoDto) {
		this.loanInfoDto = loanInfoDto;
	}

	public List<FacilityDetailDto> getFacilityDetailDtos() {
		return facilityDetailDtos;
	}

	public void setFacilityDetailDtos(List<FacilityDetailDto> facilityDetailDtos) {
		this.facilityDetailDtos = facilityDetailDtos;
	}

	public List<AssetDetailDto> getAssetDetailDtos() {
		return assetDetailDtos;
	}

	public void setAssetDetailDtos(List<AssetDetailDto> assetDetailDtos) {
		this.assetDetailDtos = assetDetailDtos;
	}
	
	public List<LoanBankDto> getLoanBankDtos() {
		return loanBankDtos;
	}

	public void setLoanBankDtos(List<LoanBankDto> loanBankDtos) {
		this.loanBankDtos = loanBankDtos;
	}

	@Override
	public String toString() {
		return "CollateralDetailsDto [loanInfoDto=" + loanInfoDto + ", facilityDetailDtos=" + facilityDetailDtos
				+ ", assetDetailDtos=" + assetDetailDtos + ", loanBankDtos=" + loanBankDtos + "]";
	}

}
