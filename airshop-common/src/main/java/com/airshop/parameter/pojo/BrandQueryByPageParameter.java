package com.airshop.parameter.pojo;

/**
 * @Author ouyanggang
 * @Date 2019/7/4 - 16:35
 */
public class BrandQueryByPageParameter {
    /*
       *   - page：当前页，int
           - rows：每页大小，int
           - sortBy：排序字段，String
           - desc：是否为降序，boolean
           - key：搜索关键词，String
       * */
    private Integer page = 1;
    private Integer rows = 5;
    private String sortBy;
    private Boolean desc = false;
    private String key;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Boolean getDesc() {
        return desc;
    }

    public void setDesc(Boolean desc) {
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
