package com.pcompany.model;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.televisory.capitalmarket.dto.BenchmarkCompanyNewDTO;

public class BenchmarkCompanyResponse {
	
	private HttpStatus status;
	private String message;
	private List<BenchmarkCompanyNewDTO> data;
	
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<BenchmarkCompanyNewDTO> getData() {
		return data;
	}
	public void setData(List<BenchmarkCompanyNewDTO> data) {
		this.data = data;
	}
	
}
