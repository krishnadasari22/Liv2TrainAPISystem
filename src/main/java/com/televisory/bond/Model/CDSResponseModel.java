package com.televisory.bond.Model;

import java.util.List;

import com.televisory.bond.dto.CDSBalanceModelDTO;
import com.televisory.bond.dto.CDSDataLatestDTO;
import com.televisory.bond.dto.CDSHistoricalDataDTO;
import com.televisory.bond.dto.CDSNameDTO;

public class CDSResponseModel {
	
	List<String> sector;
	List<String> currency;
	List<CDSBalanceModelDTO> balanceModel;
	List<CDSNameDTO> cdsNameList;
	CDSDataLatestDTO cdsLatestData;
	List<CDSHistoricalDataDTO> cdsHistoricalData;

	public List<String> getSector() {
		return sector;
	}

	public void setSector(List<String> sector) {
		this.sector = sector;
	}

	public List<String> getCurrency() {
		return currency;
	}

	public void setCurrency(List<String> currency) {
		this.currency = currency;
	}

	public List<CDSBalanceModelDTO> getBalanceModel() {
		return balanceModel;
	}

	public void setBalanceModel(List<CDSBalanceModelDTO> balanceModel) {
		this.balanceModel = balanceModel;
	}

	public List<CDSNameDTO> getCdsNameList() {
		return cdsNameList;
	}

	public void setCdsNameList(List<CDSNameDTO> cdsNameList) {
		this.cdsNameList = cdsNameList;
	}

	public CDSDataLatestDTO getCdsLatestData() {
		return cdsLatestData;
	}

	public void setCdsLatestData(CDSDataLatestDTO cdsLatestData) {
		this.cdsLatestData = cdsLatestData;
	}
	
	public List<CDSHistoricalDataDTO> getCdsHistoricalData() {
		return cdsHistoricalData;
	}

	public void setCdsHistoricalData(List<CDSHistoricalDataDTO> cdsHistoricalData) {
		this.cdsHistoricalData = cdsHistoricalData;
	}

	@Override
	public String toString() {
		return "CDSResponseModel [sector=" + sector + ", currency=" + currency + ", balanceModel=" + balanceModel
				+ ", cdsNameList=" + cdsNameList + ", cdsLatestData=" + cdsLatestData + ", cdsHistoricalData="
				+ cdsHistoricalData + "]";
	}
}
