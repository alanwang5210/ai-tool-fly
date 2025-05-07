package com.aitool.service;

import com.aitool.entity.FunctionConfig;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

public interface FunctionConfigService {
    // 创建功能配置
    void createFunction(FunctionConfig function);
    
    // 更新功能配置
    void updateFunction(FunctionConfig function);
    
    // 删除功能配置
    void deleteFunction(Long id);
    
    // 获取功能配置
    FunctionConfig getFunction(String functionCode);
    
    // 分页查询功能配置
    IPage<FunctionConfig> pageFunctions(Integer pageNum, Integer pageSize, String keyword);
    
    // 获取所有启用的功能
    List<FunctionConfig> listEnabledFunctions();
    
    // 检查用户是否有权限使用功能
    boolean hasPermission(Long userId, String functionCode);
    
    // 检查功能是否可用
    boolean isFunctionEnabled(String functionCode);
    
    // 检查用户今日使用次数是否超限
    boolean isDailyLimitExceeded(Long userId, String functionCode);
    
    // 获取功能分类列表
    List<String> listCategories();
    
    // 按分类获取功能列表
    List<FunctionConfig> listFunctionsByCategory(String category);
} 