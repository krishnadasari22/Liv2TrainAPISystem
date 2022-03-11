package com.pcompany.dao;

import java.util.Date;
import java.util.List;

import com.pcompany.dto.SegmentBusinesDTO;
import com.pcompany.dto.SegmentOperationalDTO;
import com.pcompany.dto.SegmentRegionDTO;

public interface SegmentDao {

	public List<SegmentBusinesDTO> getBusinessData(String fsimId,Date startDate, Date endDate, String currency);

	public List<SegmentRegionDTO> getRegionData(String fsimId, Date startDate,Date endDate, String currency);

	public List<SegmentOperationalDTO> getOperationalData(String fsimId,Date startDate, Date endDate);

	public List<SegmentOperationalDTO> getOperationalDataQuarter(String fsimId,Date startDate, Date endDate);

}
