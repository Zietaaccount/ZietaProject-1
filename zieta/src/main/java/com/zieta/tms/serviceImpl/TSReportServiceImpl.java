package com.zieta.tms.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zieta.tms.dto.TimeSheetReportDTO;
import com.zieta.tms.dto.TimeSheetSumReportDTO;
import com.zieta.tms.model.TSReport;
import com.zieta.tms.model.TSSumReport;
import com.zieta.tms.reports.TimeSheetReportHelper;
import com.zieta.tms.repository.TSReportRepository;
import com.zieta.tms.repository.TSSumReportRepository;
import com.zieta.tms.service.TSReportService;

@Service
public class TSReportServiceImpl implements TSReportService {
    @Autowired
	TSReportRepository tsReportRepository;
    
    @Autowired
   	TSSumReportRepository tsSumReportRepository;
    
    @Autowired
    TimeSheetReportHelper timeSheetReportHelper;
    
    @Autowired
	ModelMapper modelMapper;
    
	@Override
	public Page<TimeSheetReportDTO> getTsByDateRange(long client_id, String startDate, String endDate, Integer pageNo, Integer pageSize) {
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		
		List<TimeSheetReportDTO> tsReportList = getTSReportEntriesFromProcedure(client_id, startDate, endDate);
		int start =(int) pageable.getOffset();
		int end = (start + pageable.getPageSize()) > tsReportList.size() ? tsReportList.size() : (start + pageable.getPageSize());

		Page<TimeSheetReportDTO> page = new PageImpl<TimeSheetReportDTO>(tsReportList.subList(start, end), pageable, tsReportList.size());
		
		
		return page;
	}
	
	
	@Override
	public List<TimeSheetReportDTO> getTSReportEntriesFromProcedure(long client_id, String startDate, String endDate) {
		
		List<TSReport> tsReportList = tsReportRepository.getTsByDateRangeSP(client_id, startDate, endDate);
		List<TimeSheetReportDTO> timeSheetList = new ArrayList<>();
		
		for (TSReport tsReport : tsReportList) {
			TimeSheetReportDTO timeSheetReportDTO = modelMapper.map(tsReport, TimeSheetReportDTO.class);
			timeSheetList.add(timeSheetReportDTO);
		}
		return timeSheetList;
	}
	
	
	//////
	@Override
	public Page<TimeSheetSumReportDTO> getTsByDateRangeSum(long client_id, String startDate, String endDate, Integer pageNo, Integer pageSize) {
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		
		List<TimeSheetSumReportDTO> tsReportList = getTSReportSumEntriesFromProcedure(client_id, startDate, endDate);
		int start =(int) pageable.getOffset();
		int end = (start + pageable.getPageSize()) > tsReportList.size() ? tsReportList.size() : (start + pageable.getPageSize());

		Page<TimeSheetSumReportDTO> page = new PageImpl<TimeSheetSumReportDTO>(tsReportList.subList(start, end), pageable, tsReportList.size());
		
		
		return page;
	}
	
	
	@Override
	public List<TimeSheetSumReportDTO> getTSReportSumEntriesFromProcedure(long client_id, String startDate, String endDate) {
		
		List<TSSumReport> tsReportList = tsSumReportRepository.getTsByDateRangeSumSP(client_id, startDate, endDate);
		List<TimeSheetSumReportDTO> timeSheetList = new ArrayList<>();
		
		for (TSSumReport tsReport : tsReportList) {
			TimeSheetSumReportDTO timeSheetReportDTO = modelMapper.map(tsReport, TimeSheetSumReportDTO.class);
			timeSheetList.add(timeSheetReportDTO);
		}
		return timeSheetList;
	}
	
	///////
	@Override
	public ByteArrayInputStream downloadTimeSheetReport(HttpServletResponse response,long clientId,
			String startDate, String endDate) throws IOException {
		
		List<TimeSheetReportDTO> timeSheetReportList = getTSReportEntriesFromProcedure(clientId, startDate, endDate);
		return timeSheetReportHelper.downloadReport(response, timeSheetReportList);
		
	}

	
	////////downoad for sum
	
	@Override
	public ByteArrayInputStream downloadTimeSheetSumReport(HttpServletResponse response,long clientId,
			String startDate, String endDate) throws IOException {
		
		List<TimeSheetSumReportDTO> timeSheetReportList = getTSReportSumEntriesFromProcedure(clientId, startDate, endDate);
		return timeSheetReportHelper.downloadSumReport(response, timeSheetReportList);
		
	}

	
	
}

