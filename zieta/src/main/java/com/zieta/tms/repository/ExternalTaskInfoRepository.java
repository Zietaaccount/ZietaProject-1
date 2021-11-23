package com.zieta.tms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zieta.tms.model.ExtTaskInfo;

public interface ExternalTaskInfoRepository extends JpaRepository<ExtTaskInfo, Long> {

}
