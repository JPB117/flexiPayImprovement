package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.QueryParam;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.StatementDao;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.trx.Statement;
import com.workpoint.icpak.shared.model.statement.SearchDto;
import com.workpoint.icpak.shared.model.statement.StatementDto;

/**
 * Created by wladek on 9/18/15.
 * Database transactions are carried out here
 */
@Transactional
public class StatementDaoHelper {
    @Inject StatementDao statementDao;


    public List<StatementDto> getAllStatements(String memberId,
    		Date startDate, 
			Date endDate,
			Integer offset, Integer limit){
    	
    	List<Statement> statementList = new ArrayList<>();
    	
    	if(memberId!=null && memberId.equals("ALL")){
    		statementList = statementDao.getAllStatements(startDate,endDate,offset, limit);
    	}else{
    		Member member = statementDao.findByRefId(memberId, Member.class);
        	if(member.getMemberNo()==null){
        		return new ArrayList<>();
        	}
        	statementList = statementDao.getAllStatements(member.getMemberNo(),startDate,endDate, offset, limit);
    	}
    	
        
        List<StatementDto> statementDtos = new ArrayList<>();

        for (Statement statement : statementList){
            StatementDto statementDto = new StatementDto();
            statementDto.setDueDate(statement.getDueDate());
            statementDto.setCustomerNo(statement.getCustomerNo());
            statementDto.setDescription(statement.getDescription());
            statementDto.setAmount(statement.getAmount());
            statementDto.setDocumentNo(statement.getDocumentNo());
            statementDto.setDocumentType(statement.getDocumentType());
            statementDto.setPostingDate(statement.getPostingDate());
            statementDto.setEntryType(statement.getEntryType());
            statementDto.setCustLedgerEntryNo(statement.getCustLedgerEntryNo());
            statementDto.setEntryNo(statement.getEntryNo());
            statementDto.setRefId(statement.getRefId());

            statementDtos.add(statementDto);
        }

        return statementDtos;
    }
    
    public Integer getCount(String memberId, Date startDate, Date endDate) {
    	if(memberId!=null && memberId.equals("ALL")){
    		
    		return statementDao.getStatementCount(null, startDate, endDate);
    	}else{
    		Member member = statementDao.findByRefId(memberId, Member.class);
        	if(member.getMemberNo()==null){
        		return 0;
        	}
        	return statementDao.getStatementCount(member.getMemberNo(), startDate,endDate);
    	}
    	
		
	}

    public StatementDto createStatement(StatementDto statementDto){

        assert statementDto.getRefId() == null;
        Statement statement = new Statement();
        statement.setRefId(IDUtils.generateId());
       // statementDto.setRefId(statement.getRefId());
        statement.copyFrom(statementDto);
        statementDao.save(statement);
        assert statement.getId()!=null;

        return statement.toStatementDto();
    }

    public StatementDto getByStatementId(String statementId){
        Statement statementInDb = statementDao.getByStatementId(statementId);
        return statementInDb.toStatementDto();
    }

    public void updateStatement(String statementId , StatementDto statementDto){
        assert statementDto.getRefId()!=null;
        Statement poStatementInDb = statementDao.getByStatementId(statementId);
        poStatementInDb.copyFrom(statementDto);
        statementDao.save(poStatementInDb);
    }

    public void delete(String statementId){
        Statement poStatementInDb = statementDao.getByStatementId(statementId);
        statementDao.delete(poStatementInDb);
    }
}