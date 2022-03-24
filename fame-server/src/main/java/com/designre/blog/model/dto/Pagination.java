package com.designre.blog.model.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.designre.blog.util.FameUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Pagination<T> {
    private long pageNum;
    private long pageSize;
    private long total;
    private long pages;
    private String orderBy;
    private List<T> list;

    public static <S> Pagination<S> of(IPage<S> page) {
        return new Pagination<>(
                page.getCurrent(),
                page.getSize(),
                page.getTotal(),
                page.getPages(),
                FameUtils.objectToJson(page.orders()),
                page.getRecords()
        );
    }
}
