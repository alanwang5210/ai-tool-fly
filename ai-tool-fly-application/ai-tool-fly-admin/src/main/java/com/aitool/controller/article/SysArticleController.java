package com.aitool.controller.article;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aitool.common.Result;
import com.aitool.dto.article.ArticleQueryDto;
import com.aitool.entity.SysArticle;
import com.aitool.service.SysArticleService;
import com.aitool.vo.article.ArticleListVo;
import com.aitool.vo.article.SysArticleDetailVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 10100
 */
@RestController
@Api(tags = "文章管理")
@RequestMapping("/sys/article")
@RequiredArgsConstructor
public class SysArticleController {

    private final SysArticleService sysArticleService;

    @GetMapping("/list")
    @ApiOperation(value = "文章列表")
    @SaCheckPermission("sys:article:list")
    public Result<IPage<ArticleListVo>> list(ArticleQueryDto articleQueryDto) {
        return Result.success(sysArticleService.selectPage(articleQueryDto));
    }

    @GetMapping("/detail/{id}")
    @ApiOperation(value = "文章详情")
    public Result<SysArticleDetailVo> detail(@PathVariable Integer id) {
        return Result.success(sysArticleService.detail(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增文章")
    @SaCheckPermission("sys:article:add")
    public Result<Boolean> add(@RequestBody SysArticleDetailVo sysArticle) {
        return Result.success(sysArticleService.add(sysArticle));
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改文章")
    @SaCheckPermission("sys:article:update")
    public Result<Boolean> update(@RequestBody SysArticleDetailVo sysArticle) {
        return Result.success(sysArticleService.update(sysArticle));
    }

    @PutMapping("/updateStatus")
    @ApiOperation(value = "修改状态")
    @SaCheckPermission("sys:article:updateStatus")
    public Result<Boolean> updateStatus(@RequestBody SysArticle sysArticle) {
        return Result.success(sysArticleService.updateById(sysArticle));
    }

    @DeleteMapping("/delete/{ids}")
    @ApiOperation(value = "删除文章")
    @SaCheckPermission("sys:article:delete")
    public Result<Boolean> delete(@PathVariable List<Long> ids) {
        return Result.success(sysArticleService.delete(ids));
    }

    @GetMapping("/reptile")
    @ApiOperation(value = "爬取文章")
    @SaCheckPermission("sys:article:reptile")
    public Result<Void> reptile(String url) {
        sysArticleService.reptile(url);
        return Result.success();
    }
}
