package com.project.network.controller;

import java.util.List;

import com.project.network.annotation.PathParameter;
import com.project.network.annotation.RequestBody;
import com.project.network.annotation.RequestMapping;
import com.project.network.annotation.RequestMethod;
import com.project.network.annotation.RequestParameter;
import com.project.network.annotation.RestController;
import com.project.network.dto.GoodDto;
import com.project.network.dto.RequestGoodDto;
import com.project.network.service.GoodService;
import com.project.network.service.implementation.DefaultGoodService;


@RestController
public class GoodController
{

    private GoodService goodService = new DefaultGoodService();

    @RequestMapping(path = "/api/goods", method = RequestMethod.GET)
    public List<GoodDto> getGoodById()
    {
        return goodService.getAllGoods();
    }

    @RequestMapping(path = "/api/good/{id}", method = RequestMethod.GET)
    public GoodDto getGoodById(@PathParameter("id") String id)
    {
        return goodService.getGoodById(id);
    }

    @RequestMapping(path = "/api/good", method = RequestMethod.PUT)
    public GoodDto createNewGood(@RequestBody RequestGoodDto requestGoodDto)
    {
        return goodService.createNewGood(requestGoodDto);
    }

    @RequestMapping(path = "/api/good/{id}", method = RequestMethod.POST)
    public void updateGoodById(@PathParameter("id") String id,
                               @RequestBody RequestGoodDto requestGoodDto)
    {
        goodService.updateGoodById(id, requestGoodDto);
    }

    @RequestMapping(path = "/api/good/number/{id}", method = RequestMethod.POST)
    public int updateGoodNumberById(@PathParameter("id") String id,
                                    @RequestParameter("number") String number)
    {
        return goodService.updateGoodNumberById(id, Integer.valueOf(number));
    }

    @RequestMapping(path = "/api/good/{id}", method = RequestMethod.DELETE)
    public void deleteGoodById(@PathParameter("id") String id)
    {
        goodService.deleteGoodById(id);
    }

}
