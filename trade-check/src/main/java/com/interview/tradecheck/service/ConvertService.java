package com.interview.tradecheck.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.interview.tradecheck.bean.AmountUnit;
import com.interview.tradecheck.bean.CheckData;
import com.interview.tradecheck.bean.Params;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jiage
 */
public class ConvertService {

    /**
     * 日期格式化
     */
    private static final DateTimeFormatter DAY_FORMAT = DateTimeFormatter.ofPattern(DatePattern.PURE_DATETIME_PATTERN);

    public static CheckData convert(Params params, String strData) throws ParseException {
        Map<Integer, String> dataMap = new HashMap<>();
        int index = 0;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < strData.length(); i++) {
            char c = strData.charAt(i);

            if (c == '|') {
                dataMap.put(index++, sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }

        if (sb.length() > 0) {
            dataMap.put(index, sb.toString());
        }

        return convert(params, dataMap);
    }

    public static CheckData convert(Params params, Map<Integer, String> dataMap) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(params.getDateFormat());
        Date date = sdf.parse(dataMap.get(params.getDate()));
        String dayTime = DAY_FORMAT.format(date.toInstant());
        String unit = params.getUnit();

        String fileAmount = dataMap.get(params.getAmount());
        String fileFee = dataMap.get(params.getFee());
        long amount = AmountUnit.getUnit(unit).getConvert().apply(fileAmount);
        long fee = AmountUnit.getUnit(unit).getConvert().apply(fileFee);

        JSONObject resourceMap = getResourceMap(params);

        String resource = resourceMap.getStr(dataMap.get(params.getResource()));

        return new CheckData()
                .setPayment(params.getPayment())
                .setMerchant(params.getMerchant())
                .setDayTime(dayTime)
                .setCode(dataMap.get(params.getCode()))
                .setSystemCode(dataMap.get(params.getSystemCode()))
                .setAmount(amount)
                .setFee(fee)
                .setResource(resource);
    }

    private static JSONObject getResourceMap(Params params) {
        JSONObject resourceMap = JSONUtil.parseObj(params.getResource());
        resourceMap.set("wx", "WEIXIN");
        resourceMap.set("zfb", "ZHIFUBAO");
        return resourceMap;
    }
}

