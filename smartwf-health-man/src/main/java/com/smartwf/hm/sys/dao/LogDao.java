package com.smartwf.hm.sys.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.smartwf.common.dto.LogDTO;
import com.smartwf.hm.sys.pojo.Log;

import tk.mybatis.mapper.common.Mapper;

/**
 * @Date: 2019-10-25 16:05:30
 * @Description: 日志持久层接口
 */
@Repository
public interface LogDao extends Mapper<Log> {


    /**
     * 保存日志
     * @param logDTOList
     * @return
     */
    Integer saveLog(@Param("saveLog") List<LogDTO> logDTOList);
}
