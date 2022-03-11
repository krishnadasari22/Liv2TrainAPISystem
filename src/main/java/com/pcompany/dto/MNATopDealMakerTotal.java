package com.pcompany.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ma_v1_ma_deal_info")
public class MNATopDealMakerTotal {
	
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private Integer id;
	
	@Column(name = "total_rows")
    private Integer totalRows;
	
	@Column(name = "total_deals")
    private Integer totalDeals;
	   
    @Column(name = "transact_tot_value")
    private Double totalValue;
   
    @Column(name = "transact_avg_value")
    private Double avgValue;
   
    @Column(name = "transact_max_value")
    private Double maxValue;
    
    @Column(name = "deal_currency")
    private String dealCurrency;
   
    @Column(name = "currency")
    private String currency;
   
    @Column(name = "unit")
    private String unit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}

	public Integer getTotalDeals() {
		return totalDeals;
	}

	public void setTotalDeals(Integer totalDeals) {
		this.totalDeals = totalDeals;
	}

	public Double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(Double totalValue) {
		this.totalValue = totalValue;
	}

	public Double getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(Double avgValue) {
		this.avgValue = avgValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public String getDealCurrency() {
		return dealCurrency;
	}

	public void setDealCurrency(String dealCurrency) {
		this.dealCurrency = dealCurrency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	

}
