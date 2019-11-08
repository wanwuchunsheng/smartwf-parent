package com.smartwf.sso.service;

import com.smartwf.common.dto.LogDTO;
import com.smartwf.sso.dao.LogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Date: 2018/12/18 15:43
 * @Description: 日志业务层接口
 */
@Service
public class LogService {


    @Autowired
    private LogDao logDao;


    /**
     * 保存日志
     * @param logDTOList
     * @return
     */
    @Transactional
    public Integer saveLog(List<LogDTO> logDTOList) {
        return this.logDao.saveLog(logDTOList);
    }

}
