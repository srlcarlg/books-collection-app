package com.project.bkcollection.api.dtos;

import java.util.List;

import org.springframework.data.domain.Page;

/*
 * https://stackoverflow.com/questions/63087074/pagingandsortingrepository-custom-pageable-response-structure
 */
public class CustomPage<T> {
	
	List<T> content;
	CustomPageable pageable;
	
	public CustomPage(Page<T> page) {
	   this.content = page.getContent();
	   this.pageable = new CustomPageable(
			   page.getPageable().getPageNumber(),
			   page.getPageable().getPageSize(),
			   page.getTotalElements(),
			   page.getTotalPages());
	}
	  	 
	public List<T> getContent() {
		return content;
	}
	public CustomPageable getPageable() {
		return pageable;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
	public void setPageable(CustomPageable pageable) {
		this.pageable = pageable;
	}

	class CustomPageable {
		
	    int pageNumber;
	    int pageSize;
	    long totalElements;
	    long totalPages;
	
	    public CustomPageable(int pageNumber, int pageSize, long totalElements, long totalPages) {
	
	      this.pageNumber = pageNumber;
	      this.pageSize = pageSize;
	      this.totalElements = totalElements;
	      this.totalPages = totalPages;
	    }
	
		public int getPageNumber() {
			return pageNumber;
		}
		public int getPageSize() {
			return pageSize;
		}
		public long getTotalElements() {
			return totalElements;
		}
		public long getTotalPages() {
			return totalPages;
		}
	
		public void setPageNumber(int pageNumber) {
			this.pageNumber = pageNumber;
		}
		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}
		public void setTotalElements(long totalElements) {
			this.totalElements = totalElements;
		}
		public void setTotalPages(long totalPages) {
			this.totalPages = totalPages;
		}
	}
}
