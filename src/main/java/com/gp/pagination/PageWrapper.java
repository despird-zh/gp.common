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

	public PaginationInfo getPagination() {
		return pagination;
	}

	public void setPagination(PaginationInfo pagination) {
		this.pagination = pagination;
	}

	public List<B> getRows() {
		return rows;
	}

	public void setRows(List<B> rows) {
		this.rows = rows;
	}	
}
