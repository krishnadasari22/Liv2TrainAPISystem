package com.pcompany.dao;

import java.util.List;

import com.pcompany.dto.DebtCapitalStructureDetailsDTO;

public interface DebtStructureDao {

	List<DebtCapitalStructureDetailsDTO> getFactsetEntityDebt(String entityId);

	List<DebtCapitalStructureDetailsDTO> getBorrowingLimit(String entityId);

	List<DebtCapitalStructureDetailsDTO> getCapitalCharges(String entityId);

	List<DebtCapitalStructureDetailsDTO> getEntityMaturity(String entityId);

	List<DebtCapitalStructureDetailsDTO> getFactsetEntityDebtDownload(String entityId);

}
