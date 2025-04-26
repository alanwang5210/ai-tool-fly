package com.aitool.service;

import com.aitool.entity.SysCategory;
import com.aitool.vo.article.ArchiveListVo;
import com.aitool.vo.article.ArticleDetailVo;
import com.aitool.vo.article.ArticleListVo;
import com.aitool.vo.article.CategoryListVo;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

/**
 * @author 10100
 */
public interface ArticleService {
    /**
     * 获取文章列表
     *
     * @param tagId      tagId
     * @param categoryId categoryId
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.aitool.vo.article.ArticleListVo>
     */
    IPage<ArticleListVo> getArticleList(Integer tagId, Integer categoryId, String keyword);

    /**
     * 获取文章详情
     *
     * @param id 文章id
     * @return com.aitool.vo.article.ArticleDetailVo
     */
    ArticleDetailVo getArticleDetail(Long id);


    /**
     * 获取文章归档
     *
     * @return java.util.List<com.aitool.vo.article.ArchiveListVo>
     */
    List<ArchiveListVo> getArticleArchive();

    /**
     * 获取分类
     *
     * @return java.util.List<com.aitool.vo.article.CategoryListVo>
     */
    List<CategoryListVo> getArticleCategories();

    /**
     * 获取轮播文章
     *
     * @return java.util.List<com.aitool.vo.article.ArticleListVo>
     */
    List<ArticleListVo> getCarouselArticle();

    /**
     * 获取推荐文章
     *
     * @return java.util.List<com.aitool.vo.article.ArticleListVo>
     */
    List<ArticleListVo> getRecommendArticle();

    /**
     * 点赞文章
     *
     * @param id id
     * @return java.lang.Boolean
     */
    Boolean like(Long id);

    /**
     * XXXX
     *
     * @return java.util.List<com.aitool.entity.SysCategory>
     * @author 10100
     * @date 2025-04-26 17:36
     **/
    List<SysCategory> getCategoryAll();

}
