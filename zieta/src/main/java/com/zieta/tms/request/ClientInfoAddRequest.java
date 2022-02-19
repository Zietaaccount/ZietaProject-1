package com.zieta.tms.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientInfoAddRequest {
	
	


	private String clientCode;
	private String clientName;
	private Long clientStatus;
	private String clientComments;
	private String authToken;
	private String loginId;
	private String password;
	private String createdBy;
	private String modifiedBy;
	private boolean isDelete;
	private short superAdmin;



}
