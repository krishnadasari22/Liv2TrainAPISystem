package com.pcompany.dto;

import java.util.List;

import com.privatecompany.dto.PEVCInvestmentDetailsDTO;

public class PEVCFundingRoundDetailsDTO {
	List<PEVCInvestmentDetailsDTO>pevcFundingInvestmentDTOs;
	
	PEVCInvestmentHeaderDTO pevcInvestmentHeaderDTO;

	public List<PEVCInvestmentDetailsDTO> getPevcFundingInvestmentDTOs() {
		return pevcFundingInvestmentDTOs;
	}

	public void setPevcFundingInvestmentDTOs(List<PEVCInvestmentDetailsDTO> pevcFundingInvestmentDTOs) {
		this.pevcFundingInvestmentDTOs = pevcFundingInvestmentDTOs;
	}

	public PEVCInvestmentHeaderDTO getPevcInvestmentHeaderDTO() {
		return pevcInvestmentHeaderDTO;
	}

	public void setPevcInvestmentHeaderDTO(PEVCInvestmentHeaderDTO pevcInvestmentHeaderDTO) {
		this.pevcInvestmentHeaderDTO = pevcInvestmentHeaderDTO;
	}
	
	
	
	
}
