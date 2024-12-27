package com.interview.tradecheck.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.interview.tradecheck.bean.CheckData;
import com.interview.tradecheck.bean.Params;
import com.interview.tradecheck.bean.PayHistory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author jiage
 */
@Service
public class CheckService {

    public void doCheck(Date date, String type, String merchant) throws ParseException {
        // 时间和 type 不能为空
        if (date == null || StrUtil.isEmpty(type)) {
            return;
        }

        List<Params> paramsList;
        if (StrUtil.isNotEmpty(merchant)) {
            // 通过 merchant 获取参数
            paramsList = Collections.singletonList(new Params());
        } else {
            // 获取该 type 的所有参数列表
            paramsList = new ArrayList<>();
        }

        if (CollUtil.isNotEmpty(paramsList)) {
            for (Params params : paramsList) {
                // 获取文件路径
                String filePath = getFilePath(params, date);

                List<CheckData> checkDataList = new ArrayList<>();

                // 读取文件
                // 枚举判断
                if ("xls".equals(params.getFormat())) {
                    EasyExcel.read(filePath, new MyReadListener(params, checkDataList)).sheet()
                            .headRowNumber(params.getHead()).doRead();
                } else {
                    getTxtData(filePath, params, checkDataList);
                }

                if (CollUtil.isNotEmpty(checkDataList)) {
                    check(date, type, merchant, checkDataList);
                }
            }

        }
    }

    private void getTxtData(String filePath, Params params, List<CheckData> checkDataList) throws ParseException {
        FileReader fileReader = new FileReader(filePath, CharsetUtil.CHARSET_GBK);
        List<String> list = fileReader.readLines();
        if (CollUtil.isNotEmpty(list)) {
            for (String line : list) {
                ConvertService.convert(params, line);
            }
        }
    }

    /**
     * 对账，按文件对账，所以数据量还好
     *
     * @param date          日期
     * @param type          类型
     * @param merchant      商户号
     * @param checkDataList 待检查数据
     */
    private void check(Date date, String type, String merchant, List<CheckData> checkDataList) {
        List<PayHistory> payHistoryList;
        if (StrUtil.isNotEmpty(merchant)) {
            // 查询该日期该支付平台的该商户所有支付历史
            // 查询 客户-商户关联表 + 客户-车场关联表
            payHistoryList = new ArrayList<>();
        } else {
            // 查询该日期该支付平台的所有支付历史
            payHistoryList = new ArrayList<>();
        }

        Map<String, PayHistory> payHistoryMap = new HashMap<>();

        if (CollUtil.isNotEmpty(payHistoryList)) {
            // 将列表转换为 map
            for (CheckData checkData : checkDataList) {
                PayHistory payHistory = payHistoryMap.get(checkData.getSystemCode());
                if (payHistory != null) {
                    // 比较二者数据
                    // 金额能对的上就将状态改为成功
                    payHistoryMap.remove(checkData.getSystemCode());
                }
            }
        } else {
            // 保存对账数据，默认状态为失败
        }

        // 按车场、支付平台生成统计数据
    }

    private final static String MERCHANT = "商户号";

    /**
     * 获取文件路径
     *
     * @param params 参数
     * @param date   日期
     * @return 文件路径
     */
    private String getFilePath(Params params, Date date) {
        Map<String, String> map = new HashMap<>();
        map.put(MERCHANT, params.getMerchant());

        List<String> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.getFilePath().length(); i++) {
            char c = params.getFilePath().charAt(i);
            if (c == '{') {
                if (sb.length() > 0) {
                    list.add(sb.toString());
                }
                sb = new StringBuilder();
            } else if (c == '}') {
                String str = sb.toString();
                if (map.containsKey(str)) {
                    str = map.get(str);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat(str);
                    str = sdf.format(date);
                }
                list.add(str);
                sb = new StringBuilder();
            } else {
                sb.append(c);
            }
        }

        StringBuilder path = new StringBuilder("D://");

        for (String s : list) {
            path.append(s);
        }

        return path + "." + params.getFormat();
    }
}
