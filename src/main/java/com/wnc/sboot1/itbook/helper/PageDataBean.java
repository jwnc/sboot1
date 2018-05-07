package com.wnc.sboot1.itbook.helper;

import java.util.ArrayList;
import java.util.List;

import com.wnc.basic.BasicNumberUtil;

public class PageDataBean<E> extends ArrayList<E> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pageNum;
	private int pages;
	private int pageSize;
	private int total;

	public PageDataBean(List<E> list, int page, int size, int totalRows) {
		this.addAll(list);
		this.setPageNum(page);
		this.setPages(BasicNumberUtil.getDivSplitPage(totalRows, size));
		this.setPageSize(size);
		this.setTotal(totalRows);
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

}
