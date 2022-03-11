package com.televisory.capitalmarket.entities.factset;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ff_v3_ff_basic_cf")
public class FFBasicCf {

	@Id
	@Column(name="id")
	private Integer id;
	
	@Column(name="fsym_id")
	private String companyId;
	
	@Column(name="ff_mkt_val")
	private Double marketCap;
	
	@Column(name="ff_mkt_val_date")
	private Date marketCapDate;
	
	@Column(name="ff_com_shs_out")
	private Double shareOutStanding;
	
	@Column(name="ff_com_shs_out_date")
	private Date shareOutStandingDate;

	@Column(name="ff_shs_float")
	private Double shareFreeFloat;
	
	@Column(name="ff_shs_float_date")
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
		return "FFBasicCf [id=" + id + ", companyId=" + companyId + ", marketCap=" + marketCap + ", marketCapDate="
				+ marketCapDate + ", shareOutStanding=" + shareOutStanding + ", shareOutStandingDate="
				+ shareOutStandingDate + ", shareFreeFloat=" + shareFreeFloat + ", shareFreeFloatDate="
				+ shareFreeFloatDate + "]";
	}
	
}
