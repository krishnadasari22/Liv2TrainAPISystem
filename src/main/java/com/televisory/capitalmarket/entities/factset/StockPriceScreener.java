package com.televisory.capitalmarket.entities.factset;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.televisory.capitalmarket.entities.cm.CMCompany;

@Entity
@Table(name = "fp_v2_fp_basic_prices")
public class StockPriceScreener {
	
	@Id
	@Column(name="id")
	private Integer id;
	
	/*@OneToOne
	@JoinColumn(name="fsym_id",referencedColumnName="company_id")
	private CMCompany company;*/

	@Column(name="p_date")
	private Date period;
	
	@Column(name="price_date")
	private Date priceDate;
	
	@Column(name="currency")
	private String currency;
	
	@Column(name="p_price")
	private Double data;
	
	@Column(name="p_price_open")
	private Double open;
	
	@Column(name="p_price_high")
	private Double high;
	
	@Column(name="p_price_low")
	private Double low;
	
	@Column(name="p_volume")
	private Double volume;
	
	@Column(name="company_id")
	private String companyId;
	
	@Column(name="company_name")
	private String companyName;
	
	@Column(name="country_id")
	private String countryId;
	
	@Column(name="country_name")
	private String countryName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/*public CMCompany getCompany() {
		return company;
	}

	public void setCompany(CMCompany company) {
		this.company = company;
	}*/

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}
	
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}

	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Date getPriceDate() {
		return priceDate;
	}

	public void setPriceDate(Date priceDate) {
		this.priceDate = priceDate;
	}

	@Override
	public String toString() {
		return "StockPriceScreener [id=" + id + ", period=" + period + ", priceDate=" + priceDate + ", currency="
				+ currency + ", data=" + data + ", open=" + open + ", high=" + high + ", low=" + low + ", volume="
				+ volume + ", companyId=" + companyId + ", companyName=" + companyName + ", countryId=" + countryId
				+ ", countryName=" + countryName + "]";
	}

	
	

	

}
