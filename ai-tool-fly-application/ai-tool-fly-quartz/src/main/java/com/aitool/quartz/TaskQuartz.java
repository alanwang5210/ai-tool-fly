package com.aitool.quartz;

import com.aitool.common.RedisConstants;
import com.aitool.entity.SysArticle;
import com.aitool.mapper.SysArticleMapper;
import com.aitool.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 10100
 */
@Component("task")
@RequiredArgsConstructor
public class TaskQuartz {

    private final RedisUtil redisUtil;

    private final SysArticleMapper articleMapper;

    /**
     * 定时同步阅读量
     */
    public void syncQuantity() {
        // 获取带阅读量的前缀key集合
        List<SysArticle> articles = new ArrayList<>();
        Map<Object, Object> map = redisUtil.hGetAll(RedisConstants.ARTICLE_QUANTITY);
        // 取出所有数据更新到数据库
        for (Map.Entry<Object, Object> stringEntry : map.entrySet()) {
            Object id = stringEntry.getKey();
            List<String> list = (List<String>) stringEntry.getValue();
            SysArticle article = SysArticle.builder()
                    .id(Long.parseLong(id.toString())).quantity(list.size())
                    .build();
            articles.add(article);
        }
        articleMapper.updateBatchQuantity(articles);
    }
}
