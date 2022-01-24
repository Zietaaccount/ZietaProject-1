package com.zieta.tms.response;

import java.sql.Time;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ActivitiesByTaskResponse {

	Long taskId;
    private Long clientId;
    private Long activityId;
  //  private String activityCode;
    private String activityDesc;
    private String userName;
    private Long userId;
	private String startDate;
	private String endDate;
	private Time plannedHrs;//float to time
	private Time actualHrs;
	
	 
}
