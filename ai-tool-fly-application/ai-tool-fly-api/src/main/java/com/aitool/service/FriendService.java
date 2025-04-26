package com.aitool.service;

import com.aitool.entity.SysFriend;

import java.util.List;

public interface FriendService {

    /**
     * 获取友情链接列表
     * @return
     */
    List<SysFriend> getFriendList();

    /**
     * 申请友链
     * @param sysFriend
     * @return
     */
    Boolean apply(SysFriend sysFriend);
}
