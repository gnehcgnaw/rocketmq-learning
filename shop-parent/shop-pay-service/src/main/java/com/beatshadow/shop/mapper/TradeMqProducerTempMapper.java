package com.beatshadow.shop.mapper;

import com.beatshadow.shop.pojo.TradeMqProducerTemp;
import com.beatshadow.shop.pojo.TradeMqProducerTempExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author gnehcgnaw
 */
@Mapper
public interface TradeMqProducerTempMapper {
    int countByExample(TradeMqProducerTempExample example);

    int deleteByExample(TradeMqProducerTempExample example);

    int deleteByPrimaryKey(String id);

    int insert(TradeMqProducerTemp record);

    int insertSelective(TradeMqProducerTemp record);

    List<TradeMqProducerTemp> selectByExample(TradeMqProducerTempExample example);

    TradeMqProducerTemp selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TradeMqProducerTemp record, @Param("example") TradeMqProducerTempExample example);

    int updateByExample(@Param("record") TradeMqProducerTemp record, @Param("example") TradeMqProducerTempExample example);

    int updateByPrimaryKeySelective(TradeMqProducerTemp record);

    int updateByPrimaryKey(TradeMqProducerTemp record);
}