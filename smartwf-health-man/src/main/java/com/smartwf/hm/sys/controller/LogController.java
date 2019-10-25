package com.smartwf.hm.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.smartwf.common.dto.QueryPojo;
import com.smartwf.common.pojo.Page;
import com.smartwf.common.pojo.Result;
import com.smartwf.hm.sys.service.LogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Date: 2019-10-25 15:04:26
 * @Description: 日志控制器
 */
@RestController
@RequestMapping("log")
@Slf4j
@Api(description = "日志控制器")
public class LogController {


    @Autowired
    private LogService logService;


    /**
     * 分页查询日志
     *
     * @param page
     * @return
     */
    @GetMapping("selectLogByPage")
    @ApiOperation(value = "查询日志", notes = "分页查询日志")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "startTime", value = "开始时间", dataType = "Date"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间", dataType = "Date"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "要查看的页码，默认是1", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "每页查询数量，默认是10", dataType = "int")
    })
    public ResponseEntity<Result> selectLogByPage(Page page, QueryPojo queryPojo) {
        try {
            Result result = this.logService.selectLogByPage(page, queryPojo);
            log.info("返回结果{}",JSONArray.toJSONString(result));
            if (result != null) {
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            log.error("分页查询日志信息错误！{}", e.getMessage(), e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.msg("分页查询日志信息错误！"));
    }
}
