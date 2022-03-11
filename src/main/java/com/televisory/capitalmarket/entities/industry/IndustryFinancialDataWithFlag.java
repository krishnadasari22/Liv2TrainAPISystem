package com.televisory.capitalmarket.entities.industry;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.televisory.capitalmarket.entities.cm.CMCompanyMIN;
import com.televisory.capitalmarket.entities.cm.TicsIndustryMIN;
import com.televisory.capitalmarket.entities.cm.TicsSector;


/**
 * @author Navankur
 *
 */
@Entity
@Table(name = "industry_balance_model")
public class IndustryFinancialDataWithFlag {

	@Id
	@Column(name = "id")
	private Integer id;

	private static final Integer mandatory = 0;

	@Column(name = "display_order")
	private String displayOrder;

	@Column(name = "display_level")
	private String depthLevel;

	@Column(name = "display_flag", insertable = false)
	private String displayFlag;

	@Column(name = "field_name")
	private String fieldName;

	@Column(name = "short_name")
	private String shortName;

	@Column(name = "description")
	private String itemName;

	@OneToOne
	@JoinColumn(name = "tics_sector_code")
	private TicsSector ticsSector;

	@OneToOne
	@JoinColumn(name="tics_industry_code")
	private TicsIndustryMIN ticsIndustry;

	@OneToOne
	@JoinColumn(name="company_id")
	private CMCompanyMIN company;

	@Column(name = "period_type", insertable = false)
	private String periodType;

	@Column(name = "company_count")
	private Integer companyCount;

	@OneToOne
	@JoinColumn(name="domicile_country_code",referencedColumnName="country_iso_code_3")
	private CountryListMIN domicileCountry;

	@Column(name = "date", insertable = false)
	@Temporal(TemporalType.DATE)
	private Date period;

	@Column(name = "applicable_date", insertable = false)
	@Temporal(TemporalType.DATE)
	private Date applicablePeriod;

	@Column(name = "unit", insertable = false)
	private String unit;

	@Column(name = "data", insertable = false)
	private Double data;

	@Column(name = "currency", insertable = false)
	private String currency;

	@Column(name = "no_data_flag")
	private Integer noDataFlag;

	@Column(name = "company_active_flag")
	private Integer companyActiveFlag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getDepthLevel() {
		return depthLevel;
	}

	public void setDepthLevel(String depthLevel) {
		this.depthLevel = depthLevel;
	}

	public String getDisplayFlag() {
		return displayFlag;
	}

	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
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

	public TicsSector getTicsSector() {
		return ticsSector;
	}

	public void setTicsSector(TicsSector ticsSector) {
		this.ticsSector = ticsSector;
	}

	public TicsIndustryMIN getTicsIndustry() {
		return ticsIndustry;
	}

	public void setTicsIndustry(TicsIndustryMIN ticsIndustry) {
		this.ticsIndustry = ticsIndustry;
	}

	public CMCompanyMIN getCompany() {
		return company;
	}

	public void setCompany(CMCompanyMIN company) {
		this.company = company;
	}

	public String getPeriodType() {
		return periodType;
	}

	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}

	public Integer getCompanyCount() {
		return companyCount;
	}

	public void setCompanyCount(Integer companyCount) {
		this.companyCount = companyCount;
	}

	public CountryListMIN getDomicileCountry() {
		return domicileCountry;
	}

	public void setDomicileCountry(CountryListMIN domicileCountry) {
		this.domicileCountry = domicileCountry;
	}

	public Date getPeriod() {
		return period;
	}

	public void setPeriod(Date period) {
		this.period = period;
	}

	public Date getApplicablePeriod() {
		return applicablePeriod;
	}

	public void setApplicablePeriod(Date applicablePeriod) {
		this.applicablePeriod = applicablePeriod;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getData() {
		return data;
	}

	public void setData(Double data) {
		this.data = data;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getNoDataFlag() {
		return noDataFlag;
	}

	public void setNoDataFlag(Integer noDataFlag) {
		this.noDataFlag = noDataFlag;
	}

	public Integer getCompanyActiveFlag() {
		return companyActiveFlag;
	}

	public void setCompanyActiveFlag(Integer companyActiveFlag) {
		this.companyActiveFlag = companyActiveFlag;
	}

	public static Integer getMandatory() {
		return mandatory;
	}

	@Override
	public String toString() {
		return "IndustryFinancialDataWithFlag [id=" + id + ", displayOrder="
				+ displayOrder + ", depthLevel=" + depthLevel
				+ ", displayFlag=" + displayFlag + ", fieldName=" + fieldName
				+ ", shortName=" + shortName + ", itemName=" + itemName
				+ ", ticsSector=" + ticsSector + ", ticsIndustry="
				+ ticsIndustry + ", company=" + company + ", periodType="
				+ periodType + ", companyCount=" + companyCount
				+ ", domicileCountry=" + domicileCountry + ", period=" + period
				+ ", applicablePeriod=" + applicablePeriod + ", unit=" + unit
				+ ", data=" + data + ", currency=" + currency + ", noDataFlag="
				+ noDataFlag + ", companyActiveFlag=" + companyActiveFlag + "]";
	}


}
