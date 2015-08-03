package com.workpoint.icpak.client.ui.component;



public class PagingConfig {

	public static int PAGE_LIMIT=10;
	private Integer offset;
	private Integer limit;
	private Integer total;//total count
	
	public PagingConfig(Integer offSet, Integer total) {
		this(offSet, PAGE_LIMIT, total);
	}
	
	public PagingConfig(Integer offSet, Integer limit, Integer total) {
		if(offSet==null) offSet=0;
		
		if(limit==null || limit==0) limit = PAGE_LIMIT;
		
		if(total==null){
			total = 0;
		}
		
		this.offset = offSet;
		this.limit = limit;
		this.total = total;
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

	public int getTotal() {
		return total;
	}
	
	private boolean isMultipage() {
		return total/limit>1 || total%limit>0;
	}

	private boolean isFirstPage(){
		return offset<limit;
	}
	
	private boolean isLastPage(){
		return offset+limit>=total;
	}
	
	public int next(){
		if(hasNext()){
			offset = (offset+limit);
		}
		
		return offset;
	}
	
	public int last(){
		if(isMultipage() && !isLastPage()){
			//Last
			int lastPageSize = total%limit;
			if(lastPageSize==0){
				lastPageSize = limit;
			}
			
			offset=(total-lastPageSize);
		}
		
		return offset;
	}
	
	public int previous(){
		if(!hasPrevious()){
			return offset;
		}
		
		offset = (offset-limit);
		return offset;
	}
	
	public boolean hasNext(){
		return isMultipage() && !isLastPage();
	}
	
	public boolean hasPrevious(){
		return isMultipage() && !isFirstPage();
	}

	public int getPageEnd() {
		
		if((offset+limit) > total){
			return total;
		}
		
		return offset+limit;
	}
	
	public int getPages(){
		int size = total/limit;
		
		if(total%limit>0){
			size = size+1;
		}
		
		return size;
	}
	
	public int getCurrentPage(){
		if(offset==0){
			return 0;
		}
		
		return (offset/limit);
	}

	public void setPage(int idx) {
		offset = idx*limit;
	}

}
