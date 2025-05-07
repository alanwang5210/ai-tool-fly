package com.aitool.service.impl;

import com.aitool.entity.FunctionConfig;
import com.aitool.entity.UserPoint;
import com.aitool.entity.VipMembership;
import com.aitool.exception.BusinessException;
import com.aitool.service.FunctionConfigService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FunctionConfigServiceImpl extends ServiceImpl<FunctionConfigMapper, FunctionConfig> implements FunctionConfigService {
    
    @Autowired
    private VipMembershipService vipMembershipService;
    
    @Autowired
    private UserPointService userPointService;
    
    @Override
    @Transactional
    public void createFunction(FunctionConfig function) {
        // 检查功能代码是否已存在
        if (getFunction(function.getFunctionCode()) != null) {
            throw new BusinessException("功能代码已存在");
        }
        
        function.setCreateTime(LocalDateTime.now());
        function.setUpdateTime(LocalDateTime.now());
        save(function);
    }
    
    @Override
    @Transactional
    public void updateFunction(FunctionConfig function) {
        FunctionConfig existing = getById(function.getId());
        if (existing == null) {
            throw new BusinessException("功能不存在");
        }
        
        function.setUpdateTime(LocalDateTime.now());
        updateById(function);
    }
    
    @Override
    @Transactional
    public void deleteFunction(Long id) {
        removeById(id);
    }
    
    @Override
    public FunctionConfig getFunction(String functionCode) {
        return getOne(new QueryWrapper<FunctionConfig>().eq("function_code", functionCode));
    }
    
    @Override
    public IPage<FunctionConfig> pageFunctions(Integer pageNum, Integer pageSize, String keyword) {
        QueryWrapper<FunctionConfig> wrapper = new QueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("function_name", keyword)
                  .or()
                  .like("function_code", keyword)
                  .or()
                  .like("description", keyword);
        }
        wrapper.orderByAsc("sort");
        return page(new Page<>(pageNum, pageSize), wrapper);
    }
    
    @Override
    public List<FunctionConfig> listEnabledFunctions() {
        return list(new QueryWrapper<FunctionConfig>()
            .eq("enabled", true)
            .orderByAsc("sort"));
    }
    
    @Override
    public boolean hasPermission(Long userId, String functionCode) {
        FunctionConfig function = getFunction(functionCode);
        if (function == null || !function.getEnabled()) {
            return false;
        }
        
        // 检查角色权限
        String requiredRole = function.getRequiredRole();
        if (requiredRole != null && !requiredRole.isEmpty()) {
            // TODO: 实现角色检查逻辑
            return false;
        }
        
        // 检查具体权限
        String permission = function.getPermission();
        if (permission != null && !permission.isEmpty()) {
            // TODO: 实现权限检查逻辑
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean isFunctionEnabled(String functionCode) {
        FunctionConfig function = getFunction(functionCode);
        return function != null && function.getEnabled();
    }
    
    @Override
    public boolean isDailyLimitExceeded(Long userId, String functionCode) {
        FunctionConfig function = getFunction(functionCode);
        if (function == null || function.getDailyLimit() == null) {
            return false;
        }
        
        // TODO: 实现每日使用次数检查逻辑
        return false;
    }
    
    @Override
    public List<String> listCategories() {
        return list().stream()
            .map(FunctionConfig::getCategory)
            .distinct()
            .collect(Collectors.toList());
    }
    
    @Override
    public List<FunctionConfig> listFunctionsByCategory(String category) {
        return list(new QueryWrapper<FunctionConfig>()
            .eq("category", category)
            .eq("enabled", true)
            .orderByAsc("sort"));
    }
} 