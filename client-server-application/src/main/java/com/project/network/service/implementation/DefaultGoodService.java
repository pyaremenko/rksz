package com.project.network.service.implementation;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.project.network.converter.GoodDTOConverter;
import com.project.network.converter.RequestGoodDtoConverter;
import com.project.network.dao.GoodDao;
import com.project.network.dto.RequestGoodDto;
import com.project.network.exception.ResponseErrorException;
import com.project.network.dao.implementation.DefaultGoodDao;
import com.project.network.dto.GoodDto;
import com.project.network.service.GoodService;

public class DefaultGoodService implements GoodService {

    private GoodDao goodDao = new DefaultGoodDao();

    @Override
    public GoodDto getGoodById(final String stringId) {
        try {
            final int id = Integer.valueOf(stringId);
            Optional<GoodDto> goodDto = goodDao.getGoodById(id);
            if (goodDto.isPresent()) {
                return goodDto.get();
            } else {
                throw new ResponseErrorException(404, "No good with such id");
            }
        } catch (NumberFormatException e) {
            throw new ResponseErrorException(400, "ID is not an integer number");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseErrorException(500);
        }
    }

    @Override
    public GoodDto createNewGood(final RequestGoodDto requestGoodDto) {
        try {
            if (requestGoodDto.getName() == null || requestGoodDto.getGroupId() == null
                    || requestGoodDto.getPrice() == null) {
                throw new ResponseErrorException(409, "Not all required fields");
            }
            if (requestGoodDto.getPrice().compareTo(new BigDecimal(0)) <= 0) {
                throw new ResponseErrorException(409, "Invalid price");
            }
            if (requestGoodDto.getNumber() < 0) {
                throw new ResponseErrorException(409, "Invalid number");
            }
            if (goodDao.getGoodByName(requestGoodDto.getName()).isPresent()) {
                throw new ResponseErrorException(409, "Group with such name already exists");
            }
            int rowCount = goodDao.insertNewGood(RequestGoodDtoConverter.convert(requestGoodDto));
            if (rowCount == 1) {
                return goodDao.getGoodByName(requestGoodDto.getName()).orElseThrow();
            } else {
                throw new ResponseErrorException(500);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseErrorException(500);
        }
    }

    @Override
    public void updateGoodById(final String stringId, final RequestGoodDto requestGoodDto) {
        try {
            final int id = Integer.valueOf(stringId);

            if (requestGoodDto.getName() == null || requestGoodDto.getGroupId() == null
                    || requestGoodDto.getPrice() == null) {
                throw new ResponseErrorException(409, "Not all required fields");
            }
            if (requestGoodDto.getPrice().compareTo(new BigDecimal(0)) <= 0) {
                throw new ResponseErrorException(409, "Invalid price");
            }
            if (requestGoodDto.getNumber() < 0) {
                throw new ResponseErrorException(409, "Invalid number");
            }
            Optional<GoodDto> existed = goodDao.getGoodById(id);
            Optional<GoodDto> existedName = goodDao.getGoodByName(requestGoodDto.getName());
            if (existed.isPresent()) {
                if (existedName.isPresent() && !existed.equals(existedName)) {
                    throw new ResponseErrorException(409, "Name is already in use");
                }
                requestGoodDto.setId(id);
                GoodDto goodDto = RequestGoodDtoConverter.convert(requestGoodDto);
                if (!goodDto.equals(existed.get())) {
                    goodDao.updateGoodInfoById(goodDto);
                }
            } else {
                throw new ResponseErrorException(404, "Group with such id is not found");
            }
        } catch (NumberFormatException e) {
            throw new ResponseErrorException(400, "ID is not an integer number");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseErrorException(500);
        }
    }

    @Override
    public int updateGoodNumberById(final String id, Integer number)
    {
        GoodDto oldGoodDto = getGoodById(id);
        Integer oldNumber = oldGoodDto.getNumber();

        RequestGoodDto requestGoodDto = GoodDTOConverter.convert(oldGoodDto);

        if (number < oldNumber)
        {
            number = oldNumber - number;
        }
        else
        {
            number = oldNumber + number;
        }
        if (number >= 0)
        {
            requestGoodDto.setNumber(number);
            updateGoodById(id , requestGoodDto);
            return number;
        }
        throw new ResponseErrorException(409, "Invalid number");
    }

    @Override
    public void deleteGoodById(final String stringId) {
        try {
            final int id = Integer.valueOf(stringId);
            int rowCount = goodDao.deleteGoodById(id);
            if (rowCount == 0) {
                throw new ResponseErrorException(404, "No good with such id");
            }
        } catch (NumberFormatException e) {
            throw new ResponseErrorException(400, "ID is not an integer number");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseErrorException(500);
        }
    }

    @Override
    public List<GoodDto> getAllGoods() {
        try {
            List<GoodDto> result = goodDao.getAllGoods();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseErrorException(500);
        }
    }

}
