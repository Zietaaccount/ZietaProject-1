package com.zietaproj.zieta.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "accesstype_user_mapping")
@Data
@EqualsAndHashCode(callSuper=false)
public class UserAccessType extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "CLIENT_ID")
	private Long clientId;
	
	@Column( name = "USER_ID")
	private Long userId;
	
	@Column( name = "ACCESS_TYPE_ID")
	private Long accessTypeId;
	
	
	
	

}
