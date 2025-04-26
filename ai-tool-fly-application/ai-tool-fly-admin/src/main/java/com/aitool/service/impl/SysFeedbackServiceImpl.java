package com.aitool.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import com.aitool.common.Constants;
import com.aitool.dto.feedback.SysFeedbackQueryDto;
import com.aitool.vo.feedback.SysFeedbackVo;
import org.springframework.stereotype.Service;
import com.aitool.mapper.SysFeedbackMapper;
import com.aitool.entity.SysFeedback;
import com.aitool.service.SysFeedbackService;
import com.aitool.utils.PageUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;

/**
 * 反馈表 服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysFeedbackServiceImpl extends ServiceImpl<SysFeedbackMapper, SysFeedback> implements SysFeedbackService {

    /**
     * 查询反馈表分页列表
     */
    @Override
    public IPage<SysFeedbackVo> selectPage(SysFeedbackQueryDto feedbackQueryDto) {
        //如果是门户端的则只能看自己的反馈
        if (!Constants.ADMIN.equals(feedbackQueryDto.getSource())) {
            feedbackQueryDto.setUserId(StpUtil.getLoginIdAsLong());
        }
        return baseMapper.page(PageUtil.getPage(), feedbackQueryDto);
    }

    /**
     * 新增反馈表
     */
    @Override
    public boolean insert(SysFeedback sysFeedback) {
        sysFeedback.setUserId(StpUtil.getLoginIdAsLong());
        return save(sysFeedback);
    }

    /**
     * 修改反馈表
     */
    @Override
    public boolean update(SysFeedback sysFeedback) {
        return updateById(sysFeedback);
    }
}
