package com.pcompany.dao;

import com.pcompany.entity.CompanyDocumentMetadata;
import com.pcompany.entity.FactSetOnDemandSessionInfo;

public interface FactSetOnDemandDao {

	CompanyDocumentMetadata getCompanyDocumentMetadata(String recordKey);

	FactSetOnDemandSessionInfo getFactSetOndemandSesssionInfo(String environment, String accessLevel);

	void updateSessionInfo(FactSetOnDemandSessionInfo factSetOnDemandSessionInfo);

}
