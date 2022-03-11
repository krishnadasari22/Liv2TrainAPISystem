package com.pcompany.dao;

import java.util.List;

import com.pcompany.dto.InstitutionalShareholdingOwnershipDetailsDTO;
import com.pcompany.dto.ShareholdingOwnershipDetailsDTO;

public interface ShareholdingDao {

	List<ShareholdingOwnershipDetailsDTO> getInsiderDetailsRecent(
			String securityId);

	List<ShareholdingOwnershipDetailsDTO> getInsiderDetails(String securityId);

	List<InstitutionalShareholdingOwnershipDetailsDTO> getInstitutionalOwnershipDetails(
			String securityId);

	List<InstitutionalShareholdingOwnershipDetailsDTO> getInstitutionalOwnershipRecent(
			String securityId);
}