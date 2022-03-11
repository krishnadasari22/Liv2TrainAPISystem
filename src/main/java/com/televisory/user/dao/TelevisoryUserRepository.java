package com.televisory.user.dao;

import java.util.List;

import com.televisory.user.dto.UserDataScoringCommentDto;
import com.televisory.user.dto.UserPeerInfoDTO;

public interface TelevisoryUserRepository {

	List<UserPeerInfoDTO> getPeerCompanyList(String userId, String applicableToDate, String applicableFromDate);


}
