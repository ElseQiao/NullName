package com.allen.money.model.entity;


/**
 * Created by Else.
 * Date: 2019/11/26
 * Time: 16:21
 * Describe:
 */
public class TodayDayInfo extends DayInfo{
    //今年统计的总数
    private int totalCount;
    //高于今天的收市值
    private int countValueHigher_c_w;
    //低于今天的收市值
    private int countValueLower_c_w;
    //高于今天的收市值
    private int countValueHigher_c_m;
    //低于今天的收市值
    private int countValueLower_c_m;
    //高于今天的收市值
    private int countValueHigher_c_y;
    //低于今天的收市值
    private int countValueLower_c_y;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getCountValueHigher_c_w() {
        return countValueHigher_c_w;
    }

    public void setCountValueHigher_c_w(int countValueHigher_c_w) {
        this.countValueHigher_c_w = countValueHigher_c_w;
    }

    public int getCountValueLower_c_w() {
        return countValueLower_c_w;
    }

    public void setCountValueLower_c_w(int countValueLower_c_w) {
        this.countValueLower_c_w = countValueLower_c_w;
    }

    public int getCountValueHigher_c_m() {
        return countValueHigher_c_m;
    }

    public void setCountValueHigher_c_m(int countValueHigher_c_m) {
        this.countValueHigher_c_m = countValueHigher_c_m;
    }

    public int getCountValueLower_c_m() {
        return countValueLower_c_m;
    }

    public void setCountValueLower_c_m(int countValueLower_c_m) {
        this.countValueLower_c_m = countValueLower_c_m;
    }

    public int getCountValueHigher_c_y() {
        return countValueHigher_c_y;
    }

    public void setCountValueHigher_c_y(int countValueHigher_c_y) {
        this.countValueHigher_c_y = countValueHigher_c_y;
    }

    public int getCountValueLower_c_y() {
        return countValueLower_c_y;
    }

    public void setCountValueLower_c_y(int countValueLower_c_y) {
        this.countValueLower_c_y = countValueLower_c_y;
    }
}
