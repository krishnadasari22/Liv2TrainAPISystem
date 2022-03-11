package com.televisory.capitalmarket.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ff_v3_ff_balance_model")
public class ScreenerFinancialRespModel {

	@Id
	@Column(name = "id")
	private Integer id;
	
	
	@Column(name = "fsym_id", insertable = false)
	private String CompanyId;

	@Column(name = "field_name")
	private String fieldName;

	@Column(name = "date", insertable = false)
	@Temporal(TemporalType.DATE)
	private Date period;

	@Column(name = "currency", insertable = false)
	private String currency;

	@Column(name = "data", insertable = false)
	private Double data;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompanyId() {
		return CompanyId;
	}

	public void setCompanyId(String companyId) {
		CompanyId = companyId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

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

	@Override
	public String toString() {
		return "ScreenerFinancialRespModel [id=" + id + ", CompanyId=" + CompanyId + ", fieldName=" + fieldName
				+ ", period=" + period + ", currency=" + currency + ", data=" + data + "]";
	}

	
	
}

