package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.RolesDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.base.ExpandTokens;
import com.icpak.rest.models.base.ResourceCollectionModel;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.membership.MemberType;

@Transactional
public class MemberDaoHelper {
	
	@Inject MemberDao memberDao;
	@Inject UsersDao userDao;
	@Inject RolesDao roleDao;
	
	public void createMember(Member member){
		if(member.getRefId()!=null){
			updateMember(member.getRefId(), member);
			return;
		}
		
		if(member.getUserId()==null){
			throw new ServiceException(ErrorCodes.ILLEGAL_ARGUMENT, "userId","Null");
		}else{
			member.setUser(userDao.findByUserId(member.getUserId()));
			member.setRefId(member.getUserId());
		}
		
		memberDao.createMember(member);
		assert member.getId()!=null;
	}
	
	public void updateMember(String memberId, Member member){
		Member po = memberDao.findByMemberId(memberId,true);
		po.setStatus(member.getStatus());
		po.setHasConvictions(member.getHasConvictions());
		po.setPin(member.getPin());
		po.setMemberType(member.getMemberType());
		
		memberDao.updateMember(po);
	}
	
	public void deleteMember(String memberId){
		Member member = memberDao.findByMemberId(memberId);
		memberDao.delete(member);
	}
	public ResourceCollectionModel<Member> getAllMembers(Integer offset, Integer limit,
			UriInfo uriInfo) {
		int total = memberDao.getMemberCount();
		
		ResourceCollectionModel<Member> collection = new ResourceCollectionModel<>(offset,limit,total, uriInfo);
		List<Member> members = memberDao.getAllMembers(offset, limit);
		
		List<Member> rtn = new ArrayList<>();
		for(Member member: members){
			member.setUri(uriInfo.getAbsolutePath().toString()+"/"+member.getRefId());
			rtn.add(member.clone(ExpandTokens.DETAIL.toString()));
		}
		
		collection.setItems(rtn);
		return collection;
	}
	
	public Member getMemberById(String memberId) {
		Member member = memberDao.findByMemberId(memberId);
		if(member==null){
			throw new ServiceException(ErrorCodes.NOTFOUND,"'"+memberId+"'");
		}
		
		return member;
	}

	
}
