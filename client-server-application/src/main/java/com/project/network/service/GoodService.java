package com.project.network.service;

import java.util.List;

import com.project.network.dto.RequestGoodDto;
import com.project.network.dto.GoodDto;


public interface GoodService {

    List<GoodDto> getAllGoods();

    GoodDto getGoodById(final String id);

    GoodDto createNewGood(final RequestGoodDto goodDto);

    public void updateGoodById(final String id, final RequestGoodDto goodDto);

    public int updateGoodNumberById(final String id, final Integer number);

    public void deleteGoodById(final String id);
}

