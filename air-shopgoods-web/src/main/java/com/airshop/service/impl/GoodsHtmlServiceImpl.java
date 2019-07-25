package com.airshop.service.impl;

import com.airshop.service.GoodsHtmlService;
import com.airshop.service.GoodsService;
import com.airshop.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;

/**
 * @Author ouyanggang
 * @Date 2019/7/20 - 22:22
 */
@Service
public class GoodsHtmlServiceImpl implements GoodsHtmlService {

    private static final Logger log = LoggerFactory.getLogger(GoodsHtmlService.class);
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private TemplateEngine templateEngine;
    @Override
    public void createHtml(Long spuId) {

        PrintWriter writer = null;
        //获取页面数据
        Map<String, Object> spuMap = this.goodsService.loadModel(spuId);
        //创建Thymeleaf上下文对象
        Context context = new Context();
        //把数据放入上下文对象
        context.setVariables(spuMap);

        //创建输出流
        File file = new File("D:\\server\\nginx-1.17.1\\nginx-1.17.1\\html\\item\\" +spuId+".html");

        try{
            writer = new PrintWriter(file);
            //执行页面静态化方法
            templateEngine.process("item", context, writer);
        }catch (FileNotFoundException e){
            log.error("页面静态化出错：{}"+e,spuId);
        }finally {
            Optional.ofNullable(writer).ifPresent(w-> w.close());
        }

    }

    @Override
    public void asyncExecute(Long spuId) {
        ThreadUtils.execute(() ->createHtml(spuId));
    }

}
