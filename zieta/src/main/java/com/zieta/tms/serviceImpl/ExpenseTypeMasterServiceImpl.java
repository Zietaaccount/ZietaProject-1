package com.zieta.tms.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zieta.tms.model.ClientInfo;
import com.zieta.tms.model.ExpenseTypeMaster;
import com.zieta.tms.repository.ClientInfoRepository;
import com.zieta.tms.repository.ExpenseTypeMasterRepository;
import com.zieta.tms.response.ExpenseTypeMasterResponse;
import com.zieta.tms.service.ExpenseTypeMasterService;

@Service
public class ExpenseTypeMasterServiceImpl implements ExpenseTypeMasterService {
	
	@Autowired
	ExpenseTypeMasterRepository expenseTypeMasterRepository;
	
	@Autowired
	ClientInfoRepository clientInfoRepo;
	
	@Autowired
	ModelMapper modelMapper; 
	
	@Override
	public List<ExpenseTypeMasterResponse> findExpenseTypeByClientId(long clientId) {
		List<ExpenseTypeMaster> expenseTypeMasterList = expenseTypeMasterRepository.findByClientId(clientId);
		return transformExpenseData(expenseTypeMasterList);
		
	}

	private List<ExpenseTypeMasterResponse> transformExpenseData(
			List<ExpenseTypeMaster> expenseTypeMasterList) {
		ExpenseTypeMasterResponse expenseTypeMasterResponse = null;
		List<ExpenseTypeMasterResponse> expenseTypeMasterResponseList = new ArrayList<>();
		for (ExpenseTypeMaster expenseTypeMaster : expenseTypeMasterList) {
			expenseTypeMasterResponse = modelMapper.map(expenseTypeMaster, ExpenseTypeMasterResponse.class);
			ClientInfo clientInfo = clientInfoRepo.findById(expenseTypeMaster.getClientId()).get();
			expenseTypeMasterResponse.setClientCode(clientInfo.getClientCode());
			expenseTypeMasterResponse.setClientDescription(clientInfo.getClientName());
			expenseTypeMasterResponseList.add(expenseTypeMasterResponse);
		}
		return expenseTypeMasterResponseList;
	}

	@Override
	public List<ExpenseTypeMasterResponse> getAllExpenseTypes() {
		return transformExpenseData(expenseTypeMasterRepository.findAll());
	}

	@Override
	public void addExpenseType(ExpenseTypeMaster expenseTypeMaster) {
		
		expenseTypeMasterRepository.save(expenseTypeMaster);
		
	}

	@Override
	public void deleteExpenseType(String expenseType) {
		expenseTypeMasterRepository.deleteByExpenseType(expenseType);
	}

	@Override
	public void editExpenseType(ExpenseTypeMaster expenseTypeMaster) {
		expenseTypeMasterRepository.save(expenseTypeMaster);
		
	}

	@Override
	public void deleteByExpenseTypeId(long expesneId) {

		expenseTypeMasterRepository.deleteById(expesneId);
	}

	
}
