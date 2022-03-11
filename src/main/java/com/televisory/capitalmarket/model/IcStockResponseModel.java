package com.televisory.capitalmarket.model;

import java.util.List;

import com.televisory.capitalmarket.dto.CompanyFinancialDTO;
import com.televisory.capitalmarket.dto.CompanyFinancialMINDTO;
import com.televisory.capitalmarket.dto.StockPriceDTO;

/**
 * 
 * @author vinay
 *
 */
public class IcStockResponseModel {

	private String companyId;
	private String dataType;
	private List<String> fieldName;

	private List<CompanyFinancialMINDTO> financialDataList;
	private List<StockPriceDTO> stockDataList;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public List<String> getFieldName() {
		return fieldName;
	}

	public void setFieldName(List<String> fieldName) {
		this.fieldName = fieldName;
	}

	public List<CompanyFinancialMINDTO> getFinancialDataList() {
		return financialDataList;
	}

	public void setFinancialDataList(List<CompanyFinancialMINDTO> financialDataList) {
		this.financialDataList = financialDataList;
	}

	public List<StockPriceDTO> getStockDataList() {
		return stockDataList;
	}

	public void setStockDataList(List<StockPriceDTO> stockDataList) {
		this.stockDataList = stockDataList;
	}

	@Override
	public String toString() {
		return "IcStockResponseModel [companyId=" + companyId + ", dataType=" + dataType + ", fieldName=" + fieldName
				+ ", financialDataList=" + financialDataList + ", stockDataList=" + stockDataList + "]";
	}

}
