package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.RolesDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.exceptions.ServiceException;
import com.workpoint.icpak.shared.model.MemberDto;

@Transactional
public class MemberDaoHelper {
	
	@Inject MemberDao memberDao;
	@Inject UsersDao userDao;
	@Inject RolesDao roleDao;
	
	public MemberDto createMember(MemberDto memberDto){
		if(memberDto.getRefId()!=null){
			return updateMember(memberDto.getRefId(), memberDto);
		}
		
		Member member = new Member();
		member.copyFrom(memberDto);
		
		memberDao.createMember(member);
		return member.toDto();
	}
	
	public MemberDto updateMember(String memberId, MemberDto dto){
		Member po = memberDao.findByRefId(memberId, Member.class);
		po.copyFrom(dto);
		memberDao.updateMember(po);
		
		return po.toDto();
	}
	
	public void deleteMember(String memberId){
		Member member = memberDao.findByRefId(memberId, Member.class);
		memberDao.delete(member);
	}
	
	public List<MemberDto> getAllMembers(Integer offset, Integer limit,
			String uriInfo, String searchTerm) {
		List<Member> members = memberDao.getAllMembers(offset, limit, searchTerm);
		
		List<MemberDto> rtn = new ArrayList<>();
		for(Member member: members){
			MemberDto dto = member.toDto(); 
			
			if(member.getUserRefId()!=null){
				User user = userDao.findByUserId(member.getUserRefId());
				setMemberValues(dto, user);
			}
			rtn.add(dto);
		}
		
		return rtn;
	}
	
	private void setMemberValues(MemberDto dto, User user) {
		dto.setEmail(user.getEmail());
		dto.setFirstName(user.getUserData().getFirstName());
		dto.setLastName(user.getUserData().getLastName());
	}

	public Member getMemberById(String memberId) {
		Member member = memberDao.findByRefId(memberId, Member.class);
		
		if(member==null){
			throw new ServiceException(ErrorCodes.NOTFOUND,"'"+memberId+"'");
		}
		
		return member;
	}
	
}