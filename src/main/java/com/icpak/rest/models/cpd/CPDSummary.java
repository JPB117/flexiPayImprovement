package com.icpak.rest.models.cpd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * Simple class that represents CPD Objects
 */

@ApiModel(value="CPD Summary Model", description="A CPD summary model collects summarized CPD information "
		+ "for a member generated from the server")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CPDSummary {

	private int hours;
	private int target;
	
	public CPDSummary() {
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}
	
	
}
