package com.workpoint.icpak.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MemberStanding implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int standing=0;//0= Not in good standing, 1= in good standing
	private List<String> reasons = new ArrayList<String>();
	
	public MemberStanding() {
	}

	public int getStanding() {
		return standing;
	}

	public void setStanding(int standing) {
		this.standing = standing;
	}

	public List<String> getReasons() {
		return reasons;
	}

	public void setReasons(List<String> reasons) {
		this.reasons = reasons;
	}
	
	
}
