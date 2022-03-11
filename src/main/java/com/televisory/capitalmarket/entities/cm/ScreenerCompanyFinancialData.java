package com.televisory.capitalmarket.entities.cm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.televisory.capitalmarket.entities.cm.CMCompany;

//to store the data of company for screener
@Entity
@Table(name = "balance_model")
public class ScreenerCompanyFinancialData {

	@Id
	@Column(name = "id")
	private Integer id;
	
	private final static String unit="Million";
	
	
	/*@Column(name = "fsym_id", insertable = false)
	private String CompanyId;*/
	
	@OneToOne
	@JoinColumn(name="fsym_id",referencedColumnName="company_id")
	private CMCompany company;

	@Column(name = "financial_stmt_code", insertable = false)
	private String financialType;

	private static final Integer mandatory = 0;

	@Column(name = "display_order")
	private String displayOrder;

	@Column(name = "display_level")
	private String depthLevel;

	@Column(name = "field_name")
	private String fieldName;

	@Column(name = "description")
	private String itemName;

	@Column(name = "date", insertable = false)
	@Temporal(TemporalType.DATE)
	private Date period;

	@Column(name = "currency", insertable = false)
	private String currency;

	@Column(name = "data", insertable = false)
	private Double data;
	
	/*private String converted_currency;*/

	public CMCompany getCompany() {
		return company;
	}

	public void setCompany(CMCompany company) {
		this.company = company;
	}

	public String getFinancialType() {
		return financialType;
	}

	public void setFinancialType(String financialType) {
		this.financialType = financialType;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	/*public String getCompanyId() {
		return CompanyId;
	}

	public void setCompanyId(String companyId) {
		CompanyId = companyId;
	}*/

	public String getDepthLevel() {
		return depthLevel;
	}

	public void setDepthLevel(String depthLevel) {

		this.depthLevel = depthLevel;
	}

	public static Integer getMandatory() {
		return mandatory;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
/*
	public String getConverted_currency() {
		return converted_currency;
	}

	public void setConverted_currency(String converted_currency) {
		this.converted_currency = converted_currency;
	}*/

	@Override
	public String toString() {
		return "ScreenerCompanyFinancialData [id=" + id + ", company=" + company + ", financialType=" + financialType
				+ ", displayOrder=" + displayOrder + ", depthLevel=" + depthLevel + ", fieldName=" + fieldName
				+ ", itemName=" + itemName + ", period=" + period + ", currency=" + currency + ", data=" + data + "]";
	}

	/*@Override
	public String toString() {
		return "ScreenerCompanyFinancialData [id=" + id + ", company=" + company + ", financialType=" + financialType
				+ ", displayOrder=" + displayOrder + ", depthLevel=" + depthLevel + ", fieldName=" + fieldName
				+ ", itemName=" + itemName + ", period=" + period + ", currency=" + currency + ", data=" + data
				+ ", converted_currency=" + converted_currency + "]";
	}*/
	
	

	

	
	
	

	
	
}
