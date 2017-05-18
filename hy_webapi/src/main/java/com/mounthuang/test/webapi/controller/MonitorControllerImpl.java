package com.mounthuang.test.webapi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: mounthuang
 * Data: 2017/4/27.
 */
@RestController
@RequestMapping(value = "/v1/monitor")
@Api(value = "Monitor", description = "monitor apis")
public class MonitorControllerImpl implements MonitorController {

    @ApiOperation(value = "状态监控接口")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful response",
            response = boolean.class)})
    @RequestMapping(value = "/ping", method = RequestMethod.GET)
    @ResponseBody
    public String monitor() {
        return "status ok";
    }
}

