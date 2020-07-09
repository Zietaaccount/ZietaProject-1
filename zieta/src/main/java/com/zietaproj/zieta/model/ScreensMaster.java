package com.zietaproj.zieta.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table(name = "SCREENS_MASTER")
@EntityListeners(AuditingEntityListener.class)
@Data
public class ScreensMaster implements Serializable {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @NotBlank
	    @Column(name="screen_code")
	    private String screen_code;

	   
	    private String screen_category;

	    
	    private String screen_desc;
	    
	    
	    private String screen_title;

		
	    
}
