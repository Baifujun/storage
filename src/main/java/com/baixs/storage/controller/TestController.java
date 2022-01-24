package com.baixs.storage.controller;

import com.baixs.storage.entity.Qry;
import com.baixs.storage.entity.TestVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author baifujun
 * @date 2021-09-17
 * @time 17:44
 */
@RestController
public class TestController {
    @GetMapping("/test")
    @ApiOperation("test")
    public TestVO test(Qry qry) {
        Date date = qry.getDate();
        TestVO testVO = new TestVO();
        testVO.setDate(new Date());
        return testVO;
    }
}
