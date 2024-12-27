package com.interview.tradecheck.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.interview.tradecheck.bean.CheckData;
import com.interview.tradecheck.bean.Params;
import com.interview.tradecheck.bean.PayHistory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiage
 */
public class MyReadListener implements ReadListener<HashMap<Integer, String>> {

    private final Params params;
    private final Map<String, PayHistory> payHistoryMap;

    private final static Integer BATCH_INSERT_SIZE = 1000;
    private List<CheckData> savedList = new ArrayList<>();

    public MyReadListener(Params params, Map<String, PayHistory> payHistoryMap) {
        this.params = params;
        this.payHistoryMap = payHistoryMap;
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        exception.printStackTrace();
    }

    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        System.out.println(headMap);
    }

    @Override
    public void invoke(HashMap<Integer, String> data, AnalysisContext context) {
        try {
            CheckData checkData = ConvertService.convert(params, data);
            CheckService.check(payHistoryMap, checkData);

            savedList.add(checkData);

            if (savedList.size() >= BATCH_INSERT_SIZE) {
                saveData();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return true;
    }

    private void saveData() {
        // 保存数据
        savedList = new ArrayList<>();
    }
}
