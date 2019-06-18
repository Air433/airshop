package com.airshop.item.service;

import com.airshop.myexception.MyException;

import java.util.List;
import java.util.Locale;

/**
 * @Author ouyanggang
 * @Date 2019/6/18 - 22:23
 */
public interface CategoryService {

    /**
     * 根据id查询分类
     * @param pid
     * @return
     */
    List<Locale.Category> queryCategoryByPid(Long pid) throws MyException;
}
