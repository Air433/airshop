package com.airshop.item.mapper;

import com.airshop.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;


/**
 * @Author ouyanggang
 * @Date 2019/7/4 - 16:21
 */
public interface BrandMapper extends Mapper<Brand> {

    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    @Delete("DELETE FROM tb_category_brand WHERE brand_id = #{brandId}")
    void deleteByBrandIdInCategoryBrand(@Param("brandId") Long brandId);
}
