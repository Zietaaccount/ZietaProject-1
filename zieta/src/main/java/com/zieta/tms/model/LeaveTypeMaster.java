package com.zieta.tms.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "leave_type_master")
@Data
public class LeaveTypeMaster extends BaseEntity implements Serializable {

	
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	   
	    @Column(name="client_id")
	    private Long clientId;
	    
	    @Column(name="leave_type")
	    private String leaveType;
}
