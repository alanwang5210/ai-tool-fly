package com.aitool.service.impl;

import com.aitool.service.MessageService;
import com.aitool.entity.SysMessage;
import com.aitool.mapper.SysMessageMapper;
import com.aitool.utils.IpUtil;
import com.aitool.utils.SensitiveUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final SysMessageMapper messageMapper;

    @Override
    public List<SysMessage> getMessageList() {
        return messageMapper.selectList(null);
    }

    @Override
    public Boolean add(SysMessage sysMessage) {
        String ip = IpUtil.getIp();
        sysMessage.setIp(ip);
        sysMessage.setSource(IpUtil.getIp2region(ip));
        sysMessage.setContent(SensitiveUtil.filter(sysMessage.getContent()));
        messageMapper.insert(sysMessage);
        return true;
    }
}
