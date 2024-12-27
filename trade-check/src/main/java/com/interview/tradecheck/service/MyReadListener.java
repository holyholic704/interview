package com.interview.tradecheck.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.interview.tradecheck.bean.CheckData;
import com.interview.tradecheck.bean.Params;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiage
 */
public class MyReadListener implements ReadListener<HashMap<Integer, String>> {

    private final Params params;
    private final List<CheckData> checkDataList;

    public MyReadListener(Params params, List<CheckData> checkDataList) {
        this.params = params;
        this.checkDataList = checkDataList;
    }

    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        System.out.println(exception);
    }

    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        System.out.println(headMap);
    }

    @Override
    public void invoke(HashMap<Integer, String> data, AnalysisContext context) {
        try {
            checkDataList.add(ConvertService.convert(params, data));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    @Override
    public boolean hasNext(AnalysisContext context) {
        return true;
    }
}
