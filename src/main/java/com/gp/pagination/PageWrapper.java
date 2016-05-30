package com.gp.pagination;

import java.util.List;

/**
 * this class wrap the pagination and rows bean list
 * when send page search to service, use this class to 
 * wrap the above data together 
 * 
 * @author gary diao
 * @version 0.1 2014-12-12
 **/
public class PageWrapper<B>{

	private PaginationInfo pagination = null;
	
	private List<B> rows = null;

	/**
	 * Get the pagination information 
	 **/
	public PaginationInfo getPagination() {
		return pagination;
	}

	/**
	 * Set the pagination information 
	 **/
	public void setPagination(PaginationInfo pagination) {
		this.pagination = pagination;
	}

	/**
	 * Get the rows of data 
	 **/
	public List<B> getRows() {
		return rows;
	}

	/**
	 * Set the rows of data 
	 **/
	public void setRows(List<B> rows) {
		this.rows = rows;
	}	
}
