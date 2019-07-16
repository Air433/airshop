package com.airshop.item.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author ouyanggang
 * @Date 2019/7/15 - 20:58
 */
@RequestMapping("spec")
public interface SpecApi {

    @GetMapping("{id}")
    ResponseEntity<String> querySpecificationByCategoryId(@PathVariable("id")Long id);
}
