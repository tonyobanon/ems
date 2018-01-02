package com.ce.ems.base.classes;

import java.util.List;
import java.util.Map;

public class ListableContext {

	private String id;
	private IndexedNameType type;
	
	private Integer pageSize;
	private Integer currentPage;
	
	Map<Integer, List<String>> pages;

	public String getId() {
		return id;
	}

	public ListableContext setId(String id) {
		this.id = id;
		return this;
	}

	public IndexedNameType getType() {
		return type;
	}

	public ListableContext setType(IndexedNameType type) {
		this.type = type;
		return this;
	}
	
	public Integer getCurrentPage() {
		return currentPage;
	}

	public ListableContext setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
		return this;
	}

	public Map<Integer, List<String>> getPages() {
		return pages;
	}
	
	public List<String> getPage(Integer page){
		return pages.get(page);
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public ListableContext setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	public ListableContext setPages(Map<Integer, List<String>> pages) {
		this.pages = pages;
		return this;
	}
	
	public ListableContext addPage(Integer page, List<String> keys) {
		this.pages.put(page, keys);
		return this;
	}
	
}
