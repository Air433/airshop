package com.airshop.controller;

import com.airshop.service.GoodsHtmlService;
import com.airshop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Author ouyanggang
 * @Date 2019/7/16 - 14:23
 */
@Controller
@RequestMapping("item")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @GetMapping("{id}.html")
    public String toItemPage(Model model, @PathVariable("id")Long id){
        Map<String, Object> modelMap = this.goodsService.loadModel(id);
        model.addAllAttributes(modelMap);
        //页面静态化
        this.goodsHtmlService.asyncExecute(id);
        return "item";
    }
}
