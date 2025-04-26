package com.aitool.service.impl;

import com.aitool.vo.dashboard.IndexVo;
import com.aitool.service.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 10100
 */
@Service
@RequiredArgsConstructor
public class IndexServiceImpl implements IndexService {

    @Override
    public IndexVo index() {

        return IndexVo.builder().build();
    }

    @Override
    public List<Map<String, Integer>> getCategories() {
        return new ArrayList<>();
    }
}
