package com.televisory.bond.Model;

import java.util.List;

import com.televisory.bond.dto.DerivativeBalanceModelDTO;
import com.televisory.bond.dto.DerivativeHistoricalDataDTO;
import com.televisory.bond.dto.DerivativeLatestDTO;

public class DerivativeResponseModel {
	
	List<DerivativeBalanceModelDTO> balanceModel;
 
	DerivativeLatestDTO latestData;
	
	List<DerivativeHistoricalDataDTO> historicalData;

	public List<DerivativeBalanceModelDTO> getBalanceModel() {
		return balanceModel;
	}

	public void setBalanceModel(List<DerivativeBalanceModelDTO> balanceModel) {
		this.balanceModel = balanceModel;
	}

	public DerivativeLatestDTO getLatestData() {
		return latestData;
	}

	public void setLatestData(DerivativeLatestDTO latestData) {
		this.latestData = latestData;
	}

	public List<DerivativeHistoricalDataDTO> getHistoricalData() {
		return historicalData;
	}

	public void setHistoricalData(List<DerivativeHistoricalDataDTO> historicalData) {
		this.historicalData = historicalData;
	}

	@Override
	public String toString() {
		return "DerivativeResponseModel [balanceModel=" + balanceModel + ", latestData=" + latestData
				+ ", historicalData=" + historicalData + "]";
	}
		
}
