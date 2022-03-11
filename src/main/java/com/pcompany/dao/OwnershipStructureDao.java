package com.pcompany.dao;

import java.util.List;

import com.pcompany.dto.OwnershipEmployeeJobDTO;
import com.pcompany.dto.OwnershipManagementInfoDTO;
import com.pcompany.dto.OwnershipPeopleInfoDTO;

public interface OwnershipStructureDao {

	List<OwnershipManagementInfoDTO> getOwnershipManagementInfo(String entityId, String companyName);

	List<OwnershipPeopleInfoDTO> getPeopleProfile(String personId);

	List<OwnershipEmployeeJobDTO> getEmploymentHistory(String personId);

	List<OwnershipEmployeeJobDTO> getDirectorship(String personId);

	List<OwnershipEmployeeJobDTO> getPeopleHoldings(String comapanyId, String personId);
}
