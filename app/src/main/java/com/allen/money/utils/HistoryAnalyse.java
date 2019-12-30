package com.allen.money.utils;

import android.util.Log;

import com.allen.money.bean.NetEasyHistory;
import com.allen.money.model.entity.DayInfo;
import com.allen.money.model.entity.HistoryRange;
import com.allen.money.model.entity.TodayDayInfo;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Else.
 * Date: 2019/11/27
 * Time: 11:43
 * Describe:
 */
public class HistoryAnalyse {
    private static final String TAG = "HistoryAnalyse";
    
    //波动
    int w_1=5;
    int w_2=10;
    int w_3=15;
    int m_1=20;
    int m_6=120;

    DayInfo dayInfoMax_1w=new DayInfo();
    DayInfo dayInfoMax_2w=new DayInfo();
    DayInfo dayInfoMax_3w=new DayInfo();
    DayInfo dayInfoMax_1m=new DayInfo();
    DayInfo dayInfoMax_6m=new DayInfo();
    DayInfo dayInfoMax_1y=new DayInfo();

    DayInfo dayInfoMin_1w=new DayInfo();
    DayInfo dayInfoMin_2w=new DayInfo();
    DayInfo dayInfoMin_3w=new DayInfo();
    DayInfo dayInfoMin_1m=new DayInfo();
    DayInfo dayInfoMin_6m=new DayInfo();
    DayInfo dayInfoMin_1y=new DayInfo();
    private int dayCount=0;

    //环比
    TodayDayInfo today;//当下最新时间
    DayInfo LastMonth;
    DayInfo initDay;//当年第一天
    int lastMonthD=0;
    double comparedLastMonth=0f;
    //往期
    HistoryRange historyRange;
    public void analyseHistory(NetEasyHistory netEasyHistory) {
        today=new TodayDayInfo();
        initDay=new DayInfo();
        LastMonth=new DayInfo();
        historyRange=new HistoryRange();
        dayCount=0;
        lastMonthD=0;
        comparedLastMonth=0f;
        double max=0;
        double min=Integer.MAX_VALUE;
        List<List<Object>> stringLists=netEasyHistory.getData();
        today.setTotalCount(stringLists.size());
        historyRange.setTotalCount(stringLists.size());
        for(int i=stringLists.size()-1;i>=0;i--){
            dayCount++;
            List<Object> dayValue=stringLists.get(i);

            //幅度比对

            //波动
            double tempMax=(double)dayValue.get(3);
            if(tempMax>max){
                max=tempMax;
                updataMax(dayValue);
            }
            double tempMin=(double)dayValue.get(4);
            if(tempMin<min){
                min=tempMin;
                updataMin(dayValue);
            }
            //环比
            if(dayCount==1){
                //20150105
                today.upValue(dayValue);
                today_value_o=today.getO();
                lastMonthD=lastMonthDay((String)dayValue.get(0));
            }

            if(lastMonthD>0&&comparedLastMonth==0f){

                if(Integer.parseInt((String)dayValue.get(0))<=lastMonthD){
                    LastMonth.upValue(dayValue);
                    //上月c
                    double l_c=LastMonth.getC();
                    //today o
                    double t_o=today.getO();
                    comparedLastMonth= t_o/l_c-1;
                }
            }

            //年初
            if(i==0){
                initDay.upValue(dayValue);
            }

            //对比往期
            compareHistory(dayValue);

        }

        Log.d(TAG, "analyseHistory: s----"+netEasyHistory.getSymbol());
        Log.d(TAG, "analyseHistory: n----"+netEasyHistory.getName());
        Log.d(TAG, "analyseHistory: mon----"+today.getO());
        Log.d(TAG, "analyseHistory: w----"+dayInfoMin_1w.getL()+"---"+dayInfoMax_1w.getH());
        Log.d(TAG, "analyseHistory: 2w----"+dayInfoMin_2w.getL()+"---"+dayInfoMax_2w.getH());
        Log.d(TAG, "analyseHistory: 3w----"+dayInfoMin_3w.getL()+"---"+dayInfoMax_3w.getH());
        Log.d(TAG, "analyseHistory: 4w----"+dayInfoMin_1m.getL()+"---"+dayInfoMax_1m.getH());
        Log.d(TAG, "analyseHistory: 24w----"+dayInfoMin_6m.getL()+"---"+dayInfoMax_6m.getH());
        Log.d(TAG, "analyseHistory: y----"+dayInfoMin_1y.getL()+"---"+dayInfoMax_1y.getH());

        double w=(double) today.getCountValueLower_c_w()/w_1;
        double m=(double) today.getCountValueLower_c_m()/m_1;
        double y=(double) today.getCountValueLower_c_y()/today.getTotalCount();
        Log.d(TAG, "analyseHistory: high----今日高于本周："+today.getCountValueLower_c_w()+"---"+w);
        Log.d(TAG, "analyseHistory: high----今日高于本月："+today.getCountValueLower_c_m()+"---"+m);
        Log.d(TAG, "analyseHistory: high----今日高于本年："+today.getCountValueLower_c_y()+"---"+y);

        if(comparedLastMonth!=0){
            Log.d(TAG, "analyseHistory: M_compare====="+format(comparedLastMonth*100)+"%----l:"+LastMonth.getC()+"----t:"+today.getO());
        }
        Log.d(TAG, "analyseHistory: xiang bi nian chu====="+format((today.getO()/initDay.getO()-1)*100)+"%----l:"+initDay.getO()+"----t:"+today.getO());

        Log.d(TAG,historyRange.toString());
    }


    private double today_value_o;
    /**往期*/
    private void compareHistory(List<Object> dayValue) {
        //今日于往昔
        double now_value_c=(double) dayValue.get(NetEasyHistory.DATA_INDEX_C);
        if(now_value_c<today_value_o){
             updataLower();
        }else{
            updataHigher();
        }
        //历史涨跌幅统计
        historyRange.addToCount((double) dayValue.get(NetEasyHistory.DATA_INDEX_R));
    }

    private void updataHigher() {
        today.setCountValueHigher_c_y(today.getCountValueHigher_c_y()+1);
        if(dayCount>(m_1+1)){
            return;
        }
        today.setCountValueHigher_c_m(today.getCountValueHigher_c_m()+1);
        if(dayCount>(w_1+1)){
            return;
        }
        today.setCountValueHigher_c_w(today.getCountValueHigher_c_w()+1);
    }

    private void updataLower() {
        today.setCountValueLower_c_y(today.getCountValueLower_c_y()+1);
        if(dayCount>(m_1+1)){
            return;
        }
        today.setCountValueLower_c_m(today.getCountValueLower_c_m()+1);
        if(dayCount>(w_1+1)){
            return;
        }
        today.setCountValueLower_c_w(today.getCountValueLower_c_w()+1);
    }

    /**返回需要环比的月份*/
    private int lastMonthDay(String dayValue){

        int l=Integer.parseInt(dayValue.substring(4,6));
        System.out.println(l);
        if(l==1){
            System.out.println("无上月");
            return -1;
        }
        l=l-1;
        String day;
        if(l<10){
            day=dayValue.substring(0, 4)+"0"+l+dayValue.substring(6, 8);
        }else{
            day=dayValue.substring(0, 4)+l+dayValue.substring(6, 8);
        }
        return Integer.parseInt(day);
    }

    private String format(double value) {

        DecimalFormat df = new DecimalFormat("0.000");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }


    private void updataMin(List<Object> dayValue) {

        dayInfoMin_1y.upValue(dayValue);
        if(dayCount>m_6){
            return;
        }
        dayInfoMin_6m.upValue(dayValue);
        if(dayCount>m_1){
            return;
        }
        dayInfoMin_1m.upValue(dayValue);
        if(dayCount>w_3){
            return;
        }
        dayInfoMin_3w.upValue(dayValue);
        if(dayCount>w_2){
            return;
        }
        dayInfoMin_2w.upValue(dayValue);
        if(dayCount>w_1){
            return;
        }
        dayInfoMin_1w.upValue(dayValue);
    }

    private void updataMax(List<Object> dayValue) {
        dayInfoMax_1y.upValue(dayValue);
        if(dayCount>m_6){
            return;
        }
        dayInfoMax_6m.upValue(dayValue);
        if(dayCount>m_1){
            return;
        }
        dayInfoMax_1m.upValue(dayValue);
        if(dayCount>w_3){
            return;
        }
        dayInfoMax_3w.upValue(dayValue);
        if(dayCount>w_2){
            return;
        }
        dayInfoMax_2w.upValue(dayValue);
        if(dayCount>w_1){
            return;
        }
        dayInfoMax_1w.upValue(dayValue);
    }
}
