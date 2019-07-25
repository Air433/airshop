package com.airshop.service;

/**
 * @Author ouyanggang
 * @Date 2019/7/20 - 22:22
 */
public interface GoodsHtmlService {

    void createHtml(Long spuId);

    void asyncExecute(Long spuId);
}
