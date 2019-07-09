package com.airshop.item.service.impl;

import com.airshop.item.mapper.CategoryMapper;
import com.airshop.item.pojo.Category;
import com.airshop.item.service.CategoryService;
import com.airshop.myexception.AirExceptionEnum;
import com.airshop.myexception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @Author ouyanggang
 * @Date 2019/6/18 - 22:23
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> queryCategoryByPid(Long pid) throws MyException {
        Example example = new Example(Category.class);
        example.createCriteria().andEqualTo("parentId", pid);
        List<Category> list = this.categoryMapper.selectByExample(example);

        Optional.ofNullable(list).filter(x->!CollectionUtils.isEmpty(x))
                .orElseThrow(()->new MyException(AirExceptionEnum.CATEGORY_NOT_FOUND));

        return list;
    }

    @Override
    public List<Category> queryByBrandId(Long brandId) {
        return this.categoryMapper.queryByBrandId(brandId);
    }

    @Override
    public List<String> queryNameByIds(List<Long> ids) {

        List<String> names = new ArrayList<>();
        if (ids!=null && ids.size()>0){
            for (Long id : ids) {
                names.add(this.categoryMapper.queryNameById(id));
            }
        }

        return names;
    }
}
