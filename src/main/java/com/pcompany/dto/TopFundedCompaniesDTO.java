package com.pcompany.dto;

import java.util.Date;

public class TopFundedCompaniesDTO {

	private String entityName;
	private String entityId;
	private String entityType;
	private String finType;
	private Integer rounds;
	private Date inceptionDate;
	private Double totalValuation;
	private Double percent;
	private String country;
	private String industryName;
	private String companyId;

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

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
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
		return "TopFundedCompaniesDTO [entityName=" + entityName + ", entityId=" + entityId + ", entityType="
				+ entityType + ", finType=" + finType + ", rounds=" + rounds + ", inceptionDate=" + inceptionDate
				+ ", totalValuation=" + totalValuation + ", percent=" + percent + ", country=" + country
				+ ", industryName=" + industryName + ", companyId=" + companyId + "]";
	}

}
