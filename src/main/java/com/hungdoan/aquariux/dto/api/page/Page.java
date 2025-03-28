package com.hungdoan.aquariux.dto.api.page;

import java.util.List;

public class Page<T> {
    private List<T> content;
    private String lastId;
    private int size;
    private long totalElements;
    private int totalPages;

    public Page() {
        
    }

    public Page(List<T> content, String lastId, int size, long totalElements) {
        this.content = content;
        this.lastId = lastId;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
    }

    public List<T> getContent() {
        return content;
    }

    public String getLastId() {
        return lastId;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
