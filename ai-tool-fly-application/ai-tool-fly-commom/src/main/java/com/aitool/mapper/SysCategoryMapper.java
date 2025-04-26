package com.aitool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aitool.vo.article.CategoryListVo;
import com.aitool.entity.SysCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 分类 Mapper接口
 */
@Mapper
public interface SysCategoryMapper extends BaseMapper<SysCategory> {
    List<CategoryListVo> getArticleCategories();

}