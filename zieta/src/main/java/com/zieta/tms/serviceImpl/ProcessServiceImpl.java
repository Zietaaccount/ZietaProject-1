package com.zieta.tms.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.math.NumberUtils;
//import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zieta.tms.dto.ProcessConfigDTO;
import com.zieta.tms.dto.ProcessMasterDTO;
import com.zieta.tms.dto.ProcessStepsDTO;
import com.zieta.tms.model.ProcessConfig;
import com.zieta.tms.model.ProcessMaster;
import com.zieta.tms.model.ProcessSteps;
import com.zieta.tms.model.ProjectInfo;
import com.zieta.tms.model.TaskInfo;
import com.zieta.tms.repository.ClientInfoRepository;
import com.zieta.tms.repository.ProcessConfigRepository;
import com.zieta.tms.repository.ProcessMasterRepository;
import com.zieta.tms.repository.ProcessStepsRepository;
import com.zieta.tms.repository.ProjectInfoRepository;
import com.zieta.tms.repository.TaskInfoRepository;
import com.zieta.tms.repository.UserInfoRepository;
import com.zieta.tms.service.ProcessService;
import com.zieta.tms.util.TSMUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ProcessServiceImpl implements ProcessService {
	
	@Autowired
	ProcessMasterRepository processMasterRepository;
	
	@Autowired
	ProcessStepsRepository processStepsRepository;
	
	@Autowired
	ProcessConfigRepository processConfigRepository;
	
	@Autowired
	ProjectInfoRepository projectInfoRepository;
	
	@Autowired
	ClientInfoRepository clientInfoRepository;
	
	@Autowired
	TaskInfoRepository taskInfoRepository;
	
	@Autowired
	UserInfoRepository userInfoRepository;

	@Autowired
	ModelMapper modelMapper;
	
	
	
public List<ProcessMasterDTO> getAllProcess() {
		
		List<ProcessMaster> processmasters = processMasterRepository.findAll();
		List<ProcessMasterDTO> processmasterDTOs = new ArrayList<ProcessMasterDTO>();
		ProcessMasterDTO processMasterDTO = null;
		for (ProcessMaster processmaster : processmasters) {
			processMasterDTO = modelMapper.map(processmaster,ProcessMasterDTO.class);
			processmasterDTOs.add(processMasterDTO);
	}
	
		return processmasterDTOs;
}
	@Override
	public void addProcess(ProcessMaster processmaster) {
		processMasterRepository.save(processmaster);
	}


	public void editProcessById(@Valid ProcessMasterDTO processmasterDto) throws Exception {
		
		Optional<ProcessMaster> processMasterEntity = processMasterRepository.findById(processmasterDto.getId());
		if(processMasterEntity.isPresent()) {
			ProcessMaster processMaster = modelMapper.map(processmasterDto, ProcessMaster.class);
			processMasterRepository.save(processMaster);
			
		}else {
			throw new Exception("Process not found with the provided ID : "+processmasterDto.getId());
		}
		
	}
	
	
	public void deleteProcessById(Long id) throws Exception {
		
		Optional<ProcessMaster> processmaster = processMasterRepository.findById(id);
		if (processmaster.isPresent()) {
			processMasterRepository.deleteById(id);

		}else {
			log.info("No Process found with the provided ID{} in the DB",id);
			throw new Exception("No Process found with the provided ID in the DB :"+id);
		}
		
		
	}
	
	
	public List<ProcessStepsDTO> getAllProcessSteps(Integer pageNo, Integer pageSize) throws Exception {

		Pageable paging = PageRequest.of(pageNo, pageSize);
		
		Page<ProcessSteps> processsteps = processStepsRepository.findAll(paging);
		List<ProcessStepsDTO> processstepsDTOs = buildProcessSteps(processsteps);

		return processstepsDTOs;

	}
	
	public List<ProcessStepsDTO> getProcessStepsByClientIdByProjectId(long clientId, long projectId, Integer pageNo, Integer pageSize) throws Exception {

		Pageable pageable = PageRequest.of(pageNo, pageSize);
		
		Page<ProcessSteps> processsteps = processStepsRepository.findByClientIdAndProjectIdOrderByStepId(clientId, projectId, pageable);
		List<ProcessStepsDTO> processstepsDTOs = buildProcessSteps(processsteps);

		return processstepsDTOs;

	}
	
	private List<ProcessStepsDTO> buildProcessSteps(Page<ProcessSteps> processsteps) {
		List<ProcessStepsDTO> processstepsDTOs = new ArrayList<ProcessStepsDTO>();
		ProcessStepsDTO processStepDTO = null;

		for (ProcessSteps processstep : processsteps) {
			processStepDTO = modelMapper.map(processstep, ProcessStepsDTO.class);
			processStepDTO
					.setClientCode(clientInfoRepository.findById(processstep.getClientId()).get().getClientCode());
			processStepDTO.setClientDescription(
					clientInfoRepository.findById(processstep.getClientId()).get().getClientName());
		//	processStepDTO
		//			.setProjectCode(projectInfoRepository.findById(processstep.getProjectId()).get().getProjectCode());
			processStepDTO.setProjectDescription(
					projectInfoRepository.findById(processstep.getProjectId()).get().getProjectName());
		//	processStepDTO.setTaskCode(taskInfoRepository.findById(processstep.getProjectTaskId()).get().getTaskCode());
			processStepDTO.setTaskDescription(
					taskInfoRepository.findById(processstep.getProjectTaskId()).get().getTaskDescription());
			processStepDTO.setProcessDescription(
					processMasterRepository.findById(processstep.getTemplateId()).get().getProcessName());
			String[] approverIds = null;
			if (processstep.getApproverId() != null && !processstep.getApproverId().isEmpty()) {

				approverIds = processstep.getApproverId().split("\\|");
			}
			List<String> approverNames = new ArrayList<String>();
			for (String approverId : approverIds) {
				Long stepApproverId = null;
				try {
					stepApproverId = NumberUtils.createLong(approverId);
					approverNames.add(TSMUtil.getFullName(userInfoRepository.findById(stepApproverId).get()));
				} catch (NumberFormatException  e) {
					log.error("Exception in parsing the approverIds",e);
				}catch(Exception e) {
					log.error("Exception in getAllProcessSteps",e);
				}
				
			}

			String allApproverNames = String.join(" or ", approverNames);

			processStepDTO.setApproverName(allApproverNames);
			processstepsDTOs.add(processStepDTO);
		
	}
		return processstepsDTOs;
	}
	
	
	@Override
	public void addProcessSteps(ProcessSteps processstep) {
		processStepsRepository.save(processstep);
	}


	public void editProcessStepsById(@Valid ProcessStepsDTO processstepsDto) throws Exception {
		
		Optional<ProcessSteps> processStepsEntity = processStepsRepository.findById(processstepsDto.getId());
		if(processStepsEntity.isPresent()) {
			ProcessSteps processStep = modelMapper.map(processstepsDto, ProcessSteps.class);
			processStepsRepository.save(processStep);
			
		}else {
			throw new Exception("ProcessStep not found with the provided ID : "+processstepsDto.getId());
		}
		
	}
	
	
	public void deleteProcessStepsById(Long id) throws Exception {
		
		Optional<ProcessSteps> processstep = processStepsRepository.findById(id);
		if (processstep.isPresent()) {
			processStepsRepository.deleteById(id);

		}else {
			log.info("No ProcessStep found with the provided ID{} in the DB",id);
			throw new Exception("No ProcessStep found with the provided ID in the DB :"+id);
		}
		
	}
	
	//Implementation for ProcessConfig API
	
	public List<ProcessConfigDTO> getAllProcessConfig() {

		List<ProcessConfig> processconfigs = processConfigRepository.findAll();
		List<ProcessConfigDTO> processconfigDTOs = new ArrayList<ProcessConfigDTO>();
		ProcessConfigDTO processConfigDTO = null;
		for (ProcessConfig processconfig : processconfigs) {
			processConfigDTO = modelMapper.map(processconfig, ProcessConfigDTO.class);
			processconfigDTOs.add(processConfigDTO);
		}

		return processconfigDTOs;
	}
	
	
	public List<ProcessSteps> createProcessSteps(TaskInfo taskInfo, ProjectInfo projectInfo) {
		List<ProcessConfig> processConfigList = processConfigRepository.findByTemplateId(projectInfo.getTemplateId());
		
		//construct process-steps entries based on the template-id used by project
		
		List<ProcessSteps> processStepsList = new ArrayList<ProcessSteps>();
		ProcessSteps processSteps = null;
		for (ProcessConfig processConfig : processConfigList) {
			processSteps = new  ProcessSteps();
			processSteps.setClientId(taskInfo.getClientId());
			processSteps.setProjectId(taskInfo.getProjectId());
			//processSteps.setTemplateId(taskInfo.getProjectId());
			processSteps.setTemplateId(projectInfo.getTemplateId());
			processSteps.setProjectTaskId(taskInfo.getTaskInfoId());
			processSteps.setStepId(processConfig.getStepId());
			String approverId = TSMUtil.getApproverId(taskInfo, projectInfo, processConfig);
			processSteps.setApproverId(approverId);
			processStepsList.add(processSteps);
		}
		return processStepsList;
	}
	
	
	
	private static String convertStringArrayToString(String[] strArr, String delimiter) {
		StringBuilder sb = new StringBuilder();
		for (String str : strArr)
			sb.append(str).append(delimiter);
		return sb.substring(0, sb.length() - 1);
	}
	
}