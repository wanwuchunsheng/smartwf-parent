package com.smartwf.hm.sys.service.impl;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.smartwf.common.constant.Constants;
import com.smartwf.common.dto.LogDTO;
import com.smartwf.common.dto.QueryPojo;
import com.smartwf.common.pojo.Page;
import com.smartwf.common.pojo.Result;
import com.smartwf.hm.sys.dao.LogDao;
import com.smartwf.hm.sys.pojo.Log;
import com.smartwf.hm.sys.service.LogService;

import tk.mybatis.mapper.entity.Example;

/**
 * @Date: 2018/12/18 15:44
 * @Description: 日志业务层实现
 */
@Service
public class LogServiceImpl implements LogService {

	@Autowired
    private MongoTemplate mongoTemplate;
	
    @Autowired
    private LogDao logDao;
 

    /**
     * 保存日志
     * @param logDTOList
     * @return
     */
    @Transactional
    @Override
    public Integer saveLog(List<LogDTO> logDTOList) {
        return this.logDao.saveLog(logDTOList);
    }


    /**
     * 分页查询日志
     * @param page
     * @param queryPojo
     * @return
     */
    @Override
    public Result selectLogByPage(Page page, QueryPojo queryPojo) {
    	com.github.pagehelper.Page<Object> objectPage = PageHelper.startPage(page.getPage(), page.getLimit());
        Example example = new Example(Log.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(queryPojo.getOrderNo())) {
            criteria.orLike("logUser", Constants.PER_CENT + queryPojo.getName() + Constants.PER_CENT);
        }
        if (queryPojo.getStartTime() != null && queryPojo.getEndTime() != null) {
            criteria.orBetween("oprationTime", queryPojo.getStartTime(), queryPojo.getEndTime());
        }
        List<Log> logs = this.logDao.selectByExample(example);
       //
		System.out.println(JSONArray.toJSONString(queryPojo));
		queryPojo.setName("aaaaaaa");
		//添加
		mongoTemplate.save(queryPojo);
		
		//查询
		 Query query=new Query(Criteria.where("name").is("aaaaaaa"));
		 QueryPojo mgt =  mongoTemplate.findOne(query , QueryPojo.class);
		 System.out.println(JSONArray.toJSONString(mgt));
        
        return Result.data(objectPage.getTotal(), logs);
    }
}
