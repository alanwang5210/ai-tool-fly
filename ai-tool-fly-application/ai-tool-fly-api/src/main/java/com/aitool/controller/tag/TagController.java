package com.aitool.controller.tag;

import com.aitool.service.TagService;
import com.aitool.vo.tag.TagListVo;
import com.aitool.common.Result;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api/tag")
@RequiredArgsConstructor
@Api(tags = "门户-标签管理")
public class TagController {

    private final TagService tagService;

    @GetMapping("/list")
    public Result<List<TagListVo>> getTagsApi() {
        return Result.success(tagService.getTagsApi());
    }


}
