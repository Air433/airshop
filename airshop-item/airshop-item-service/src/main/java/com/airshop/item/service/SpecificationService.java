package com.airshop.item.service;

import com.airshop.item.pojo.Specification;

/**
 * @Author ouyanggang
 * @Date 2019/7/8 - 11:06
 */
public interface SpecificationService {

    Specification queryById(Long id);

    void saveSpecification(Specification specification);

    void updateSpecification(Specification specificationService);
}
