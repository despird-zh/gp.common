package com.gp.pagination;

/**
 * Pagination helper class facilitate to calculate the pagination setting.
 * 
 * @author garydiao
 * @version 0.1 2015-12-12
 **/
public class PaginationHelper {
	
	// total Rows
    private int totalRowsAmount;
    private int pageSize = 20;
    
    private int totalPages;
    // current page number
    private int currentPage = 1;
    // next page number
    private int nextPage;
    // previous page number
    private int previousPage;
    // is has next page
    private boolean hasNext;
    // is has previous page
    private boolean hasPrevious;
    // current page start row number
    private int pageStartRow = 0;
    // current page end row number
    private int pageEndRow;
    
    private int[] pageNumMenus;
    
    private int menuSize = 5;
    // Pagination values

    public PaginationHelper() {
    	
    }

    /**
     * Constructor with total rows and current page
     * default page size is 20 and menu size is 5.
     * @param totalRows total rows        
     * @param currentPage the current page number start from 1 ... n
     *            
     */
    public PaginationHelper(int totalRows, int currentPage) {
        setPaginationSupport(totalRows, currentPage);
    }

    /**
     * Constructor with totalRows and current page and detail setting
     * 
     * @param totalRows total rows        
     * @param currentPage the current page number start from 1 ... n
     * @param pageSize the rows of per page
     * @param menuSize the page menus
     */
    public PaginationHelper(int totalRows, int currentPage, int pageSize, int menuSize) {
        this.pageSize = pageSize;
        this.menuSize = menuSize;
        this.setPaginationSupport(totalRows, currentPage);
    }

    /**
     * Get the page number menus, [2,3,4,5,6] or [15,16,17] of current page
     **/
    public int[] getPageNumMenus() {
        return pageNumMenus;
    }

    /**
     * Set the size of page 
     **/
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

	/**
     * Get the Page number menus of specified page context.
     * 
     * @param currentPage
     * @return the page number menus
     */
    private int[] getPageNumMenus(int currentPage) {
    	int[] pageNums = null;

        if (totalPages > menuSize) {// Total page numbers greater than the visible menus

            pageNums = new int[menuSize];

            int lastMenuNum = totalPages - menuSize + 1;// the last page number menu

            int beginMumNum = menuSize;

            int x = menuSize - 1;// index of page number

            if ((currentPage < lastMenuNum) && (currentPage > beginMumNum)) {

                for (int i = 0; i < menuSize; i++) {
                    pageNums[i] = currentPage + i - x / 2;
                }

            } else if (currentPage > lastMenuNum) {
                for (int i = 0; i < menuSize; i++) {
                    pageNums[i] = lastMenuNum + i;
                }
            } else if (currentPage == lastMenuNum) {

                if ((lastMenuNum - x / 2) < 1) {
                    lastMenuNum = x / 2 + 1;
                }

                for (int i = 0; i < menuSize; i++) {
                    pageNums[i] = lastMenuNum + i - x / 2;
                }

            } else if (currentPage == beginMumNum) {
                for (int i = 0; i < menuSize; i++) {
                    pageNums[i] = 1 + i + x / 2;
                }
            } else {
                for (int i = 0; i < menuSize; i++) {
                    pageNums[i] = 1 + i;
                }
            }

        } else {// total pages less than the page tag amount
            pageNums = new int[totalPages];

            for (int i = 0; i < totalPages; i++) {
                pageNums[i] = i + 1;
            }
        }
        return pageNums;
    }

    /**
     * set total row count。
     * 
     * @param rows total row count
     */
    private void setTotalRowsAmount(int rows) {

        if (rows < 0) {
            totalRowsAmount = 0;
        } else {
            totalRowsAmount = rows;
        }

        if (totalRowsAmount % pageSize == 0) {
            totalPages = totalRowsAmount / pageSize;
        } else {
            totalPages = totalRowsAmount / pageSize + 1;
        }
    }

    /**
     * set current page 
     * 
     * @param curPage the page number, start from 1
     */
    private void setCurrentPage(int curPage) {

        if (curPage <= 0) {
            currentPage = 1;
        } else if (curPage > totalPages) {
            currentPage = totalPages;
        } else {
            currentPage = curPage;
        }

        if (currentPage > 1) {
            hasPrevious = true;
        } else {
            hasPrevious = false;
        }

        if (currentPage == totalPages || totalRowsAmount == 0) {
            hasNext = false;
        } else {
            hasNext = true;
        }

        if (totalPages != 0) {
            nextPage = currentPage + 1;
            previousPage = currentPage - 1;
            // current page start row and end row indices
            pageStartRow = (currentPage - 1) * pageSize + 1;
            // page row index is start from 0
            pageStartRow -= 1;
            pageEndRow = pageStartRow + pageSize;
        }
        
        pageEndRow = (pageEndRow > this.totalRowsAmount -1) ? (this.totalRowsAmount) : pageEndRow;
    }

    /**
     * Set the pagination parameter.
     * @param totalRows the rows total
     * @param currentPage the current page. 
     **/
    public void setPaginationSupport(int totalRows, int currentPage) {
        // 
        setTotalRowsAmount(totalRows);
        setCurrentPage(currentPage);
        pageNumMenus = getPageNumMenus(currentPage);
    }

    /**
     * Get the current page 
     **/
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * has next page or not 
     **/
    public boolean hasNext() {
        return hasNext;
    }

    /**
     * has previous or not 
     **/
    public boolean hasPrevious() {
        return hasPrevious;
    }

    /**
     * get next page number 
     **/
    public int getNextPage() {
        return nextPage;
    }

    /**
     * get the page size 
     **/
    public int getPageSize() {
        return pageSize;
    }

    /**
     * get previous page number 
     **/
    public int getPreviousPage() {
        return previousPage;
    }

    /**
     * get total page numbers 
     **/
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Get the total rows 
     **/
    public int getTotalRowsAmount() {
        return totalRowsAmount;
    }

    /**
     * Get the page start row 
     **/
    public int getPageStartRow() {
        return pageStartRow;
    }
    
    /**
     * Get the page end row
     **/
    public int getPageEndRow() {
        return pageEndRow;
    }

    /**
     * get the page menu size 
     **/
    public int getMenuSize() {
        return menuSize;
    }

    /**
     * set the page menu size 
     **/
    public void setMenuSize(int menuSize) {
        this.menuSize = menuSize;
    }
    
    /**
     * Get the pagination information 
     **/
    public PaginationInfo getPaginationInfo(){

    	PaginationInfo pinfo = new PaginationInfo(this.pageNumMenus);
    	pinfo.setPreviousNextInfo(this.hasPrevious, this.hasNext, this.currentPage, this.getTotalPages());
    	pinfo.setRowInfo(this.totalRowsAmount, this.pageStartRow, this.pageEndRow);
    	return pinfo;
    }
}
