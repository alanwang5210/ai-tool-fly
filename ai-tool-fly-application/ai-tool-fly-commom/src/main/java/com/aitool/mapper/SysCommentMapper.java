package com.aitool.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aitool.vo.comment.CommentListVo;
import com.aitool.entity.SysComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aitool.vo.comment.SysCommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论 Mapper接口
 */
@Mapper
public interface SysCommentMapper extends BaseMapper<SysComment> {

    IPage<CommentListVo> getComments(@Param("page") Page<Object> page, @Param("articleId") Integer articleId,
                                     @Param("sortType") String sortType);

    List<CommentListVo> getChildrenComment(Integer id);

    Page<SysCommentVO> selectPage(Page<SysCommentVO> page);

    IPage<CommentListVo> selectMyComment(@Param("page") Page<Object> page,@Param("userId")  long userId);

    IPage<CommentListVo> getMyReply(@Param("page") Page<Object> page,@Param("userId")  long userId);

}
