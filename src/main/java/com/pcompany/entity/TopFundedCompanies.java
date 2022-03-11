package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sym_v1_sym_entity")
public class TopFundedCompanies {

	@Id
	@Column(name = "factset_portco_entity_id")
	private String entityId;

	@Column(name = "entity_proper_name")
	private String entityName;

	@Column(name = "portco_fin_type")
	private String finType;

	@Column(name = "rounds")
	private Integer rounds;

	@Column(name = "inception_date")
	private Date inceptionDate;

	@Column(name = "country")
	private String country;

	@Column(name = "tics_industry_name")
	private String industryName;

	@Column(name = "total_valuation_fx")
	private Double totalValuation;

	@Column(name = "company_id")
	private String companyId;

	@Column(name = "entity_type")
	private String entityType;

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Integer getRounds() {
		return rounds;
	}

	public void setRounds(Integer rounds) {
		this.rounds = rounds;
	}

	public Date getInceptionDate() {
		return inceptionDate;
	}

	public void setInceptionDate(Date inceptionDate) {
		this.inceptionDate = inceptionDate;
	}

	public Double getTotalValuation() {
		return totalValuation;
	}

	public void setTotalValuation(Double totalValuation) {
		this.totalValuation = totalValuation;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getFinType() {
		return finType;
	}

	public void setFinType(String finType) {
		this.finType = finType;
	}

	@Override
	public String toString() {
		return "TopFundedCompanies [entityId=" + entityId + ", entityName=" + entityName + ", finType=" + finType
				+ ", rounds=" + rounds + ", inceptionDate=" + inceptionDate + ", country=" + country + ", industryName="
				+ industryName + ", totalValuation=" + totalValuation + ", companyId=" + companyId + ", entityType="
				+ entityType + "]";
	}

}
