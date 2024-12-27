package com.interview.tradecheck.service;

import cn.hutool.core.map.MapUtil;
import com.interview.tradecheck.bean.CheckData;
import com.interview.tradecheck.bean.PayHistory;

import java.util.Map;

/**
 * @author jiage
 */
public class CheckService {

    public static void check(Map<String, PayHistory> payHistoryMap, CheckData checkData) {
        if (MapUtil.isNotEmpty(payHistoryMap)) {
            // 将列表转换为 map
            PayHistory payHistory = payHistoryMap.get(checkData.getSystemCode());
            if (payHistory != null) {
                // 比较二者数据
                // 金额能对的上就将状态改为成功
                checkData.setStatus(true);
                payHistoryMap.remove(checkData.getSystemCode());
            }
        } else {
            // 保存对账数据，默认状态为失败
            checkData.setStatus(false);
        }

        // 按车场、支付平台生成统计数据
    }
}
