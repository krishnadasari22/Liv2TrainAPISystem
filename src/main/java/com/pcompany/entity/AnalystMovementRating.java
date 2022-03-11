package com.pcompany.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fe_v4_fe_basic_conh_rec")
public class AnalystMovementRating {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="fsym_id")
	private String companyId;
	
	@Column(name = "fe_item")
	private String feItem;
	
	@Column(name = "netmovement")
	private Integer netmovement;
	/*@Column(name = "upgrade", insertable = false,updatable=false)
	private Integer upgrade;
	
	@Column(name = "downgrade", insertable = false)
	private Integer downgrade;
	
	@Column(name = "no_change", insertable = false)
	private Integer nochange;*/
	
	@Column(name = "date", insertable = false)
	private Date startDate;

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

	public String getFeItem() {
		return feItem;
	}

	public void setFeItem(String feItem) {
		this.feItem = feItem;
	}

	/*public Integer getUpgrade() {
		return upgrade;
	}

	public void setUpgrade(Integer upgrade) {
		this.upgrade = upgrade;
	}

	public Integer getDowngrade() {
		return downgrade;
	}

	public void setDowngrade(Integer downgrade) {
		this.downgrade = downgrade;
	}
*/
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	

	public Integer getNetmovement() {
		return netmovement;
	}

	public void setNetmovement(Integer netmovement) {
		this.netmovement = netmovement;
	}

	@Override
	public String toString() {
		return "AnalystMovementRating [id=" + id + ", companyId=" + companyId + ", feItem=" + feItem + ", netmovement="
				+ netmovement + ", startDate=" + startDate + "]";
	}

	/*public Integer getNochange() {
		return nochange;
	}

	public void setNoChange(Integer noChange) {
		this.nochange = noChange;
	}*/
	
	


	

	
	
	
	
}
