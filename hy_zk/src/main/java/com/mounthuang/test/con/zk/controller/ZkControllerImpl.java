package com.mounthuang.test.con.zk.controller;

import com.mounthuang.test.con.core.utils.godeye.GodEyeLog;
import com.mounthuang.test.con.zk.leaderselector.LeaderInfo;
import com.mounthuang.test.con.zk.service.LeaderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Author: mounthuang
 * Data: 2017/5/18.
 */
@RestController
@GodEyeLog
@RequestMapping(value = "/v1/zk", produces = {APPLICATION_JSON_VALUE})
@Api(value = "ZkControl", description = "zk api")
public class ZkControllerImpl implements ZkController {
    @Autowired
    private LeaderService leaderService;

    @ApiOperation(value = "查询Leader", response = LeaderInfo.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful response",
            response = LeaderInfo.class)})
    @RequestMapping(value = "/getLeader", method = RequestMethod.GET)
    @ResponseBody
    @Override
    public LeaderInfo getLeaderInfo() {
        return leaderService.getLeaderInfo();
    }
}
