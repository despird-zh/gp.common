package com.gp.pagination;

import java.util.ArrayList;
import java.util.List;

/**
 * Pagination information to be sent to front-end in json format,
 * JS script use this json data to presents the pagination button.
 * 
 * @author gary diao
 * @version 0.1 2014-12-12
 **/
public class PaginationInfo {

	private boolean previous;
	
	private boolean next;
	
	private int currentPage;
	
	private int previousPage;
	
	private int nextPage;
	
	private int totalRows;
	
	private int pageStartRow;
	
	private int pageEndRow;
	
	private int totalPages;
		
	public PaginationInfo(int[] pageMenuNums){
		pages = new ArrayList<PageInfo>();
		for(int i = 0 ; i< pageMenuNums.length; i++){
			PageInfo pageinfo = new PageInfo();
			pageinfo.setPageNumber(pageMenuNums[i]);
			pages.add(pageinfo);
		}
	}
	
	public void setPreviousNextInfo(boolean hasPrev, boolean hasNext, int currentPage, int totalPages){
		
		if(previous = hasPrev){
			
			previousPage = currentPage -1;
		}
		if(next = hasNext){
			
			nextPage = currentPage + 1;
		}
		
		this.currentPage = currentPage;
		
		for(PageInfo pageinfo : pages){
			if(pageinfo.pageNumber == currentPage){
				
				pageinfo.current = true;
				break;
			}
		}
		
		this.totalPages = totalPages;
	}
	
	public void setRowInfo(int totalRows, int pageStartRow, int pageEndRow){
		
		this.totalRows = totalRows;
		this.pageStartRow = pageStartRow;
		this.pageEndRow = pageEndRow;
	}
	
	private List<PageInfo> pages;	
	
	public boolean getPrevious() {
		return previous;
	}

	public void setPrevious(boolean previous) {
		this.previous = previous;
	}

	public boolean getNext() {
		return next;
	}

	public void setNext(boolean next) {
		this.next = next;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(int previousPage) {
		this.previousPage = previousPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public List<PageInfo> getPages() {
		return pages;
	}

	public void setPages(List<PageInfo> pages) {
		this.pages = pages;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getPageStartRow() {
		return pageStartRow;
	}

	public void setPageStartRow(int pageStartRow) {
		this.pageStartRow = pageStartRow;
	}

	public int getPageEndRow() {
		return pageEndRow;
	}

	public void setPageEndRow(int pageEndRow) {
		this.pageEndRow = pageEndRow;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public static class PageInfo {
		
		private int pageNumber = 1;
		
		private boolean current = false;

		public int getPageNumber() {
			return pageNumber;
		}

		public void setPageNumber(int pageNumber) {
			this.pageNumber = pageNumber;
		}

		public boolean getCurrent() {
			return current;
		}

		public void setCurrent(boolean current) {
			this.current = current;
		}	
		
	}
}
