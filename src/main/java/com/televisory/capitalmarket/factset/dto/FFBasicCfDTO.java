package com.televisory.capitalmarket.factset.dto;

import java.util.Date;

public class FFBasicCfDTO {

	private Integer id;
	
	private String companyId;

	private Double marketCap;
	
	private Date marketCapDate;
	
	private Double shareOutStanding;
	
	private Date shareOutStandingDate;
	
	private Double shareFreeFloat;
	
	private Date shareFreeFloatDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public Double getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(Double marketCap) {
		this.marketCap = marketCap;
	}

	public Date getMarketCapDate() {
		return marketCapDate;
	}

	public void setMarketCapDate(Date marketCapDate) {
		this.marketCapDate = marketCapDate;
	}

	public Double getShareOutStanding() {
		return shareOutStanding;
	}

	public void setShareOutStanding(Double shareOutStanding) {
		this.shareOutStanding = shareOutStanding;
	}

	public Date getShareOutStandingDate() {
		return shareOutStandingDate;
	}

	public void setShareOutStandingDate(Date shareOutStandingDate) {
		this.shareOutStandingDate = shareOutStandingDate;
	}

	public Double getShareFreeFloat() {
		return shareFreeFloat;
	}

	public void setShareFreeFloat(Double shareFreeFloat) {
		this.shareFreeFloat = shareFreeFloat;
	}

	public Date getShareFreeFloatDate() {
		return shareFreeFloatDate;
	}

	public void setShareFreeFloatDate(Date shareFreeFloatDate) {
		this.shareFreeFloatDate = shareFreeFloatDate;
	}

	@Override
	public String toString() {
		return "FFBasicCfDTO [id=" + id + ", companyId=" + companyId + ", marketCap=" + marketCap + ", marketCapDate="
				+ marketCapDate + ", shareOutStanding=" + shareOutStanding + ", shareOutStandingDate="
				+ shareOutStandingDate + ", shareFreeFloat=" + shareFreeFloat + ", shareFreeFloatDate="
				+ shareFreeFloatDate + "]";
	}
	
}
