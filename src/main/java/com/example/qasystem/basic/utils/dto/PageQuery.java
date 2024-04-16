package com.example.qasystem.basic.utils.dto;

import lombok.Data;

@Data
public class PageQuery {
    //当前页
    private Integer currentPage = 1;
    //每页的条数
    private Integer pageSize=10;
    //定义一个方法来算出我当前要查询的数据要从第几条开始
    public Integer getPage(){
        return (this.currentPage-1)*this.pageSize;
    }
}
