package com.icpak.rest.models.base;

import java.util.List;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.icpak.rest.BaseResource;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.rest.models.cpd.CPDSummary;
import com.icpak.rest.models.event.Event;
import com.icpak.rest.models.membership.Member;

/**
 * Model for all Collection returns 
 * 
 * @author duggan
 *
 * @param <T>
 */
//@JsonInclude(Include.NON_NULL)
@JsonSerialize(include=Inclusion.NON_NULL)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({User.class,Member.class, Event.class, CPD.class, CPDSummary.class})
public class ResourceCollectionModel<T extends ResourceModel> extends ResourceModel{

	private Integer offset;
	private Integer limit;
	private Integer total;//total count
	private String first;//href to first page
	private String previous;//href to previouspage
	private String next; //href to next page
	private String last;//href to last page

	private List<T> items = null;
	
	public ResourceCollectionModel() {
	}

	public ResourceCollectionModel(Integer offSet, Integer limit, Integer total, UriInfo uriInfo) {
		if(offSet==null) offSet=0;
		if(limit==null) limit = BaseResource.PAGE_LIMIT;
		
		if(total==null){
			throw new ServiceException(ErrorCodes.SERVER_ERROR, ResourceCollectionModel.class.getCanonicalName(),
					"Collection Total Count is null for resource ("+uriInfo.getAbsolutePath()+")");
		}
		
		this.offset = offSet;
		this.limit = limit;
		this.total = total;
		setUrls(uriInfo);
	}

	public String getFirst() {
		return first;
	}

	public void setFirst(String urlFirstPage) {
		this.first = urlFirstPage;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String urlNextPage) {
		this.next = urlNextPage;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String urlLastPage) {
		this.last = urlLastPage;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		if(items==null || items.isEmpty()){
			this.items = null;
			return;
		}
		this.items = items;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	private void setUrls(UriInfo uriInfo) {
		assert limit>0;
		String uri = uriInfo.getAbsolutePath().toString();
		first=uri;
		if(total==0){
			//nothing to show
			return;
		}
		
		if(!isFirstPage()){
			//first
			first=uri+"?offset="+0+"&limit="+limit;
		}
		
		if(hasPrevious()){
			//previous
			previous = uri+"?offset="+(offset-limit)+"&limit="+limit;
		}
		
		if(hasNext()){
			//Next
			next = uri+"?offset="+(offset+limit)+"&limit="+limit;
		}
		
		if(isMultipage() && !isLastPage()){
			//Last
			int lastPageSize = total%limit;
			if(lastPageSize==0){
				lastPageSize = limit;
			}
			
			last=uri+"?offset="+(total-lastPageSize)+"&limit="+limit;
		}
		
	}
	
	private boolean isMultipage() {
		return total/limit>1 || total%limit>0;
	}

	public boolean isFirstPage(){
		return offset<limit;
	}
	
	public boolean isLastPage(){
		return offset+limit>=total;
	}
	
	public boolean hasNext(){
		return isMultipage() && !isLastPage();
	}
	
	public boolean hasPrevious(){
		return isMultipage() && !isFirstPage();
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}
}
