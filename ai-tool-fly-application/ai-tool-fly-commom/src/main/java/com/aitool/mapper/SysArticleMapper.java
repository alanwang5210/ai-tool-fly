package com.aitool.mapper;

import com.aitool.dto.article.ArticleQueryDto;
import com.aitool.entity.SysArticle;
import com.aitool.vo.article.ArticleDetailVo;
import com.aitool.vo.article.ArticleListVo;
import com.aitool.vo.dashboard.ContributionData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 文章 Mapper接口
 *
 * @author 10100
 */
@Mapper
public interface SysArticleMapper extends BaseMapper<SysArticle> {

    /**
     * 门户-获取文章列表
     *
     * @param page       page
     * @param tagId      tagId
     * @param categoryId categoryId
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.aitool.vo.article.ArticleListVo>
     */
    IPage<ArticleListVo> getArticleListApi(@Param("page") Page<Object> page, @Param("tagId") Integer tagId,
                                           @Param("categoryId") Integer categoryId, @Param("keyword") String keyword);

    /**
     * 获取文章详情
     *
     * @param id id
     * @return com.aitool.vo.article.ArticleDetailVo
     */
    ArticleDetailVo getArticleDetail(Long id);

    /**
     * 获取文章归档
     *
     * @return java.util.List<java.lang.Integer>
     */
    List<Integer> getArticleArchive();

    /**
     * 获取文章列表按年分组
     *
     * @param year year
     * @return java.util.List<com.aitool.vo.article.ArticleListVo>
     */
    List<ArticleListVo> getArticleByYear(Integer year);

    /**
     * 自定义分页查询
     *
     * @param page            page
     * @param articleQueryDto articleQueryDto
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.aitool.vo.article.ArticleListVo>
     */
    IPage<ArticleListVo> selectPageList(@Param("page") Page<Object> page, @Param("query") ArticleQueryDto articleQueryDto);

    /**
     * 获取今年贡献度
     *
     * @return java.util.List<com.aitool.vo.dashboard.ContributionData>
     */
    List<ContributionData> getThisYearContributionData();

    /**
     * 获取用户是否文章点赞
     *
     * @param articleId articleId
     * @param userId    userId
     * @return java.lang.Boolean
     */
    Boolean getUserIsLike(@Param("articleId") Long articleId, @Param("userId") int userId);

    /**
     * 文章取消点赞
     *
     * @param articleId articleId
     * @param userId    userId
     */
    void unLike(@Param("articleId") Long articleId, @Param("userId") int userId);

    /**
     * 文章点赞
     *
     * @param articleId articleId
     * @param userId    userId
     */
    void like(@Param("articleId") Long articleId, @Param("userId") int userId);

    @MapKey("name")
    List<Map<String, Integer>> selectCountByCategory();

    IPage<ArticleListVo> selectMyLike(@Param("page") Page<Object> page, @Param("userId") long userId);

    IPage<ArticleListVo> selectMyArticle(@Param("page") Page<Object> page, @Param("article") SysArticle article);

    void updateBatchQuantity(@Param("articles") List<SysArticle> articles);
}