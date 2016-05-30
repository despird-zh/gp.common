package com.gp.pagination;

/**
 * wrap information for pagination query 
 * 
 * @author gary diao
 * @version 0.1 2014-12-12
 * 
 **/
public class PageQuery {

	private int pageSize;
	
	private int pageNumber;

	private boolean totalCountEnable = false;
	
	/**
	 * constructor with pageSize and pageNumber
	 * the page number start at 1 
	 * 
	 * @exception IllegalArgumentException
	 **/
	public PageQuery(int pageSize, int pageNumber){
		
		if(pageNumber < 1)
			throw new IllegalArgumentException("page number must be bigger than 1");
		
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}
	
	/**
	 * constructor with pageSize and pageNumber
	 * the page number start at 1 
	 * 
	 * @exception IllegalArgumentException
	 **/
	public PageQuery(int pageSize, int pageNumber, boolean totalCountEnable){
		
		if(pageNumber < 1)
			throw new IllegalArgumentException("page number must be bigger than 1");
		
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalCountEnable = totalCountEnable;
	}
	
	/**
	 * Query total count enabled or not. 
	 **/
	public boolean isTotalCountEnable(){
		return this.totalCountEnable;
	}
	
	/**
	 * reset the query total count enable.
	 **/
	public void setTotalCountEnable(boolean totalCountEnable){
		
		this.totalCountEnable = totalCountEnable;
	}
	
	/**
	 * get the page size 
	 **/
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * set the page size 
	 **/
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * get the page number
	 **/
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * set the page number 
	 **/
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}	
	
}
