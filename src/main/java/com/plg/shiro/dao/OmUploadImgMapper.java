package com.plg.shiro.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.plg.shiro.entity.OmUploadImg;
import com.plg.shiro.entity.OmUploadImgExample;

public interface OmUploadImgMapper {
    long countByExample(OmUploadImgExample example);

    int deleteByExample(OmUploadImgExample example);

    int deleteByPrimaryKey(String id);

    int insert(OmUploadImg record);

    int insertSelective(OmUploadImg record);

    List<OmUploadImg> selectByExample(OmUploadImgExample example);

    OmUploadImg selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") OmUploadImg record, @Param("example") OmUploadImgExample example);

    int updateByExample(@Param("record") OmUploadImg record, @Param("example") OmUploadImgExample example);

    int updateByPrimaryKeySelective(OmUploadImg record);

    int updateByPrimaryKey(OmUploadImg record);
}