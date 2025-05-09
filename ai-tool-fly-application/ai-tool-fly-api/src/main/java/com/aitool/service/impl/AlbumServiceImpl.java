package com.aitool.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.aitool.entity.SysAlbum;
import com.aitool.entity.SysPhoto;
import com.aitool.exception.ServiceException;
import com.aitool.mapper.SysAlbumMapper;
import com.aitool.mapper.SysPhotoMapper;
import com.aitool.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author quequnlong
 * @date 2025/2/7
 * @description
 */
@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final SysAlbumMapper baseMapper;

    private final SysPhotoMapper photoMapper;

    @Override
    public List<SysAlbum> getAlbumList() {
        return baseMapper.getAlbumList();
    }

    @Override
    public List<SysPhoto> getPhotos(Long albumId) {
        return photoMapper.selectList(new LambdaQueryWrapper<SysPhoto>()
                .eq(SysPhoto::getAlbumId, albumId)
                .orderByAsc(SysPhoto::getSort)
                .orderByDesc(SysPhoto::getRecordTime));
    }

    @Override
    public Boolean verify(Long id, String password) {
        SysAlbum album = baseMapper.selectById(id);
        if (album == null) {
            throw new ServiceException("相册不存在!");
        }
        return BCrypt.checkpw(password, album.getPassword());
    }

    @Override
    public SysAlbum detail(Long id) {
        SysAlbum sysAlbum = baseMapper.selectById(id);
        if (sysAlbum == null) {
            throw new ServiceException("相册不存在!");
        }
        sysAlbum.setPassword(null);
        return sysAlbum;
    }
}