package com.project.network.converter;

import com.project.network.dto.GoodDto;
import com.project.network.dto.GroupDto;
import com.project.network.dto.RequestGoodDto;


public class GoodDTOConverter
{
    public static RequestGoodDto convert(final GoodDto goodDto) {
        RequestGoodDto requestGoodDto = new RequestGoodDto();
        requestGoodDto.setId(goodDto.getId());
        requestGoodDto.setName(goodDto.getName());
        if (goodDto.getGroup() != null)
        {
            requestGoodDto.setGroupId(goodDto.getGroup().getId().toString());
        }
        requestGoodDto.setDescription(goodDto.getDescription());
        requestGoodDto.setManufacturer(goodDto.getManufacturer());
        requestGoodDto.setNumber(goodDto.getNumber() == null ? 0 : goodDto.getNumber());
        requestGoodDto.setPrice(goodDto.getPrice());
        return requestGoodDto;
    }
}
