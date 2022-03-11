package com.pcompany.dao;

import java.util.List;

import com.pcompany.dto.AnalystCoverageChartDTO;
import com.pcompany.dto.AnalystCoverageDTO;
import com.pcompany.dto.AnalystGuidanceDeviationDTO;

public interface AnalystCoverageDao {
	 List<AnalystCoverageChartDTO> getTotalRecommendation(String companyId, String recType);
	 List<AnalystCoverageDTO> getRecommendation(String companyId, String recType);
	 List<AnalystCoverageDTO> getBasicConsensusYearly(String companyId, Boolean isDownload);
	 List<AnalystCoverageDTO> getBasicConsensusQuarterly(String companyId, Boolean isDownload);
	 List<AnalystCoverageDTO> getAdvancedConsensusYearly(String companyId);
	 List<AnalystCoverageDTO> getAdvancedConsensusQuarterly(String companyId);
	 List<AnalystCoverageDTO> getBasicManagementGuidanceYearly(String companyId);
	 List<AnalystCoverageDTO> getBasicManagementGuidanceQuarterly(String companyId);
	 List<AnalystCoverageDTO> getAdvancedManagementGuidanceYearly(String companyId);
	 List<AnalystCoverageDTO> getAdvancedManagementGuidanceQuarterly(String companyId);
	 List<AnalystCoverageDTO> getConsensusOperationYearly(String companyId);
	 List<AnalystCoverageDTO> getConsensusOperationQuarterly(String companyId);
	 List<AnalystGuidanceDeviationDTO> getGuidanceDeviationYearly(String companyId);
	 List<AnalystGuidanceDeviationDTO> getGuidanceDeviationQuarterly(String companyId);
	 List<AnalystCoverageChartDTO> getMovementRating(String companyId, String type);
	 
	 
}
