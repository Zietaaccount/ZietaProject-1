package com.zieta.tms.dto;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TSInfoDTO {

    private Long id;
	private Long clientId;
	private Long tsId;
	private Long projectId;
	private Long activityId;
	private Long taskActivityId;
	private Long userId;
	private Long taskId;
	private Long statusId;
	private boolean plannedActivity;	
	private String tsDate;
	private String submitDate;
	private float tsTotalSubmittedTime;//convert float to DATE 24012022
	private float tsTotalApprovedTime;//convert float to DATE 24012022	
	private String createdBy;
	private String modifiedBy;	
	private short isDelete;	
	
}
