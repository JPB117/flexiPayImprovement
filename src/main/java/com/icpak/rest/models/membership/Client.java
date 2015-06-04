package com.icpak.rest.models.membership;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;


@ApiModel(description="Client counts for the firm per sector")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Member.class})

@Entity
@Table(name="client")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Client extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IndustrySector sector;
	private int count;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="practiceid")
	private Practice practice;

	public IndustrySector getSector() {
		return sector;
	}

	public void setSector(IndustrySector sector) {
		this.sector = sector;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Practice getPractice() {
		return practice;
	}

	public void setPractice(Practice practice) {
		this.practice = practice;
	}
}
