package com.interview.tradecheck.controller;

import cn.hutool.core.util.ZipUtil;
import com.interview.tradecheck.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.ParseException;
import java.util.Date;

/**
 * @author jiage
 */
@Component
public class TestController {

    @Autowired
    private CheckService checkService;

    public void check(Date date, String type, String merchant) throws ParseException {
        checkService.doCheck(date, type, merchant);
    }

    public static void main(String[] args) {
        ZipUtil.unzip("D://weixin_pay/9988/woc.zip");
    }
}
