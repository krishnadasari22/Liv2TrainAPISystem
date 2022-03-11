package com.televisory.bond.Model;

import java.util.List;

import com.televisory.bond.dto.FIBalanceModelDTO;
import com.televisory.bond.dto.FIHistoricalDTO;
import com.televisory.bond.dto.FILatestDataDTO;
import com.televisory.bond.dto.FINameDTO;

public class FIResponseModel {
	
	List<String> category;
	List<String> currency;
	List<FIBalanceModelDTO> balanceModel;
	List<FINameDTO> bondNameList;
	FILatestDataDTO bondLatestData;
	List<FIHistoricalDTO> bondHistoricalData;
	public List<String> getCategory() {
		return category;
	}
	public void setCategory(List<String> category) {
		this.category = category;
	}
	public List<String> getCurrency() {
		return currency;
	}
	public void setCurrency(List<String> currency) {
		this.currency = currency;
	}
	public List<FIBalanceModelDTO> getBalanceModel() {
		return balanceModel;
	}
	public void setBalanceModel(List<FIBalanceModelDTO> balanceModel) {
		this.balanceModel = balanceModel;
	}
	public List<FINameDTO> getBondNameList() {
		return bondNameList;
	}
	public void setBondNameList(List<FINameDTO> bondNameList) {
		this.bondNameList = bondNameList;
	}
	public FILatestDataDTO getBondLatestData() {
		return bondLatestData;
	}
	public void setBondLatestData(FILatestDataDTO bondLatestData) {
		this.bondLatestData = bondLatestData;
	}
	public List<FIHistoricalDTO> getBondHistoricalData() {
		return bondHistoricalData;
	}
	public void setBondHistoricalData(List<FIHistoricalDTO> bondHistoricalData) {
		this.bondHistoricalData = bondHistoricalData;
	}
	
	@Override
	public String toString() {
		return "FIResponseModel [category=" + category + ", currency=" + currency + ", balanceModel=" + balanceModel
				+ ", bondNameList=" + bondNameList + ", bondLatestData=" + bondLatestData + ", bondHistoricalData="
				+ bondHistoricalData + "]";
	}
}
