package com.icpak.rest.models.membership;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(description="Referres for a member's experience or training")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

@Entity
@Table(name="referees")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Referee extends PO{

	//private String memberid;
	
	private String startDate;
	private String endDate;
	private String registrationNo;//check this
	private String nameOfPartner;//check this
	private String firmName;//The firm you worked with the referee
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="trainingexpid")
	private TrainingAndExperience trainingAndExperience;
}
