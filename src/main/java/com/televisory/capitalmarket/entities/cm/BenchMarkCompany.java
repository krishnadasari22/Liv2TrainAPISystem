package com.televisory.capitalmarket.entities.cm;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "company_list")
public class BenchMarkCompany {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="period_type")
	private String periodType;
	
	@Column(name="date")
	private Date date;
	
	@Column(name ="company_id")
    private String companyId;
	
	@Column(name = "company_name", insertable = false)
    private String companyName;
	
	@Column(name = "currency", insertable = false)
	private String currency;

	@Column(name = "field_name", insertable = false)
	private String fieldName;

	@Column(name = "data", insertable = false)
	private Double data;

	@Column(name = "short_name", insertable = false)
	private String shortName;
	
	@Column(name = "description")
	private String itemName;
	
	@Column(name = "unit", insertable = false)
	private String unit;

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

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "BenchMarkCompany [id=" + id + ", periodType=" + periodType + ", date=" + date + ", companyId="
				+ companyId + ", companyName=" + companyName + ", currency=" + currency + ", fieldName=" + fieldName
				+ ", data=" + data + ", shortName=" + shortName + ", itemName=" + itemName + ", unit=" + unit + "]";
	}

	

	


	
	
	

}
