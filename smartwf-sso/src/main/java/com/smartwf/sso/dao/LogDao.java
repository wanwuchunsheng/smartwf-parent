package com.smartwf.sso.dao;

import com.smartwf.common.dto.LogDTO;
import com.smartwf.sso.pojo.Log;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Date: 2018/12/18 15:42
 * @Description: 日志持久层接口
 */
@Repository
public interface LogDao extends Mapper<Log> {


    /**
     * 保存日志
     * @param logDTOList
     * @return
     */
    Integer saveLog(@Param("logDTOList") List<LogDTO> logDTOList);
}
