package com.gp.pagination;

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
     * 构造函数。
     * 
     * @param totalRows
     *            总行数
     * @param currentPage
     *            当前页数
     */
    public PaginationHelper(int totalRows, int currentPage) {
        setPaginationSupport(totalRows, currentPage);
    }

    /**
     * general 构造函数。
     * 
     * @param totalRows
     *            总行数
     * @param currentPage
     *            当前页数
     * @param pageSize
     *            每页显示数量。
     */
    public PaginationHelper(int totalRows, int currentPage, int pageSize, int menuSize) {
        this.pageSize = pageSize;
        this.menuSize = menuSize;
        this.setPaginationSupport(totalRows, currentPage);
    }

    public int[] getPageNumMenus() {
        return pageNumMenus;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

	/**
     * 
     * 
     * @param currentPage
     * @return
     */
    private int[] getPageMenuNums(int currentPage) {
    	int[] pageNums = null;

        if (totalPages > menuSize) {// 总页数大于导航显示的页数

            pageNums = new int[menuSize];

            int lastMenuNum = totalPages - menuSize + 1;// 最后一列导航栏按钮

            int beginMumNum = menuSize;

            int x = menuSize - 1;// 导航显示系数

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

            // 分页数比总页数少
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
     * @param curPage
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

    public void setPaginationSupport(int totalRows, int currentPage) {
        // 
        setTotalRowsAmount(totalRows);
        setCurrentPage(currentPage);
        pageNumMenus = getPageMenuNums(currentPage);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public boolean hasPrevious() {
        return hasPrevious;
    }

    public int getNextPage() {
        return nextPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalRowsAmount() {
        return totalRowsAmount;
    }

    public int getPageStartRow() {
        return pageStartRow;
    }
    
    /**
     * 取得页的结束行索引，该行索引为排除项
     **/
    public int getPageEndRow() {
        return pageEndRow;
    }

    public int getMenuSize() {
        return menuSize;
    }

    public void setMenuSize(int menuSize) {
        this.menuSize = menuSize;
    }
    
    public PaginationInfo getPaginationInfo(){

    	PaginationInfo pinfo = new PaginationInfo(this.pageNumMenus);
    	pinfo.setPreviousNextInfo(this.hasPrevious, this.hasNext, this.currentPage, this.getTotalPages());
    	pinfo.setRowInfo(this.totalRowsAmount, this.pageStartRow, this.pageEndRow);
    	return pinfo;
    }
}
