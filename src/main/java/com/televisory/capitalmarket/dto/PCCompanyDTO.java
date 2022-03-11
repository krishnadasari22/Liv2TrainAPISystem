package com.televisory.capitalmarket.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.dozer.Mapping;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author vinay
 *
 */

public class PCCompanyDTO<T> implements Serializable{

	private static final long serialVersionUID = -6003856656417771060L;

	private T id;
	
	
	private String securityId;
	private String factSetEntityId;
	
	private Integer rowNo;;
	private String name;

	private String security_code;
	private String status;
	private String ff_industry;
	private String description;
	
	private String domicileCountryCode;
	
	private String ticsIndustryCode;
	
	private String entityType;
	
	private int is_Active;

	@JsonIgnore
	private Date created_at;
	@JsonIgnore
	private String created_by;
	@JsonIgnore
	private Date last_modified_at;
	@JsonIgnore
	private String last_modified_by;
	
	private Set<StockPriceDTO> stockPrice;

	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}
	
	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}

	
	public String getName() {
		return name;
	}

	

	public String getSecurity_code() {
		return security_code;
	}

	public String getStatus() {
		return status;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public int getIs_Active() {
		return is_Active;
	}

	public String getCreated_by() {
		return created_by;
	}

	public Date getLast_modified_at() {
		return last_modified_at;
	}

	public String getLast_modified_by() {
		return last_modified_by;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSecurity_code(String security_code) {
		this.security_code = security_code;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getFf_industry() {
		return ff_industry;
	}

	public void setFf_industry(String ff_industry) {
		this.ff_industry = ff_industry;
	}
	
	public String getDomicileCountryCode() {
		return domicileCountryCode;
	}

	public void setDomicileCountryCode(String domicileCountryCode) {
		this.domicileCountryCode = domicileCountryCode;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public void setIs_Active(int is_Active) {
		this.is_Active = is_Active;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public void setLast_modified_at(Date last_modified_at) {
		this.last_modified_at = last_modified_at;
	}

	public void setLast_modified_by(String last_modified_by) {
		this.last_modified_by = last_modified_by;
	}

	public Set<StockPriceDTO> getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(Set<StockPriceDTO> stockPrice) {
		this.stockPrice = stockPrice;
	}
	

	public Integer getRowNo() {
		return rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}
	
	public String getTicsIndustryCode() {
		return ticsIndustryCode;
	}

	public void setTicsIndustryCode(String ticsIndustryCode) {
		this.ticsIndustryCode = ticsIndustryCode;
	}

	public String getFactSetEntityId() {
		return factSetEntityId;
	}

	public void setFactSetEntityId(String factSetEntityId) {
		this.factSetEntityId = factSetEntityId;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	@Override
	public String toString() {
		return "CompanyDTO [id=" + id + ", securityId="
				+ securityId + ", factSetEntityId=" + factSetEntityId
				+ ", rowNo=" + rowNo + ", name=" + name + ", security_code="
				+ security_code + ", status=" + status + ", ff_industry="
				+ ff_industry + ", description=" + description
				+ ", domicileCountryCode="
				+ domicileCountryCode + ", ticsIndustryCode=" + ticsIndustryCode
				+ ", entityType=" + entityType
				+ ", is_Active=" + is_Active + ", created_at=" + created_at
				+ ", created_by=" + created_by + ", last_modified_at="
				+ last_modified_at + ", last_modified_by=" + last_modified_by
				+ ", stockPrice=" + stockPrice + "]";
	}
}
