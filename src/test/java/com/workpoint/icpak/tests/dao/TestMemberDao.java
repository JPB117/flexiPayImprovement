package com.workpoint.icpak.tests.dao;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.MemberDaoHelper;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestMemberDao  extends AbstractDaoTest {

	@Inject MemberDaoHelper helper;
	
	@Ignore
	public void search(){
		List<MemberDto> list = helper.getAllMembers(0, 100, "", "mar");
		for(MemberDto dto: list){
			System.err.println(dto.getRefId()+" "+dto.getMemberId()+" "+ dto.getLastName()+" "+dto.getFirstName());
		}
		System.err.print("Size = "+list.size());
	}
}
