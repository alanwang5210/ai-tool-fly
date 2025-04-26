package com.aitool.service;

import com.aitool.dto.article.ArticleQueryDto;
import com.aitool.entity.SysArticle;
import com.aitool.vo.article.ArticleListVo;
import com.aitool.vo.article.SysArticleDetailVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author 10100
 */
public interface SysArticleService extends IService<SysArticle> {

    /**
     * 分页查询
     *
     * @param articleQueryDto articleQueryDto
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.aitool.vo.article.ArticleListVo>
     */
    IPage<ArticleListVo> selectPage(ArticleQueryDto articleQueryDto);

    /**
     * 文章详情
     *
     * @param id id
     * @return com.aitool.vo.article.SysArticleDetailVo
     */
    SysArticleDetailVo detail(Integer id);

    /**
     * 新增
     *
     * @param sysArticle sysArticle
     * @return java.lang.Boolean
     */
    Boolean add(SysArticleDetailVo sysArticle);

    /**
     * 修改
     *
     * @param sysArticle sysArticle
     * @return java.lang.Boolean
     */
    Boolean update(SysArticleDetailVo sysArticle);


    /**
     * 删除
     *
     * @param ids ids
     * @return java.lang.Boolean
     */
    Boolean delete(List<Long> ids);

    /**
     * 爬取文章
     *
     * @param url url
     */
    void reptile(String url);
}
