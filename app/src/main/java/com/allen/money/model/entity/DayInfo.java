package com.allen.money.model.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by Else.
 * Date: 2019/11/26
 * Time: 16:21
 * Describe:
 */
public class DayInfo {
    private String data;
    private double o;
    private double c;
    private double h;
    private double l;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getO() {
        return o;
    }

    public void setO(double o) {
        this.o = o;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getL() {
        return l;
    }

    public void setL(double l) {
        this.l = l;
    }

    public void upValue(List<Object> dayValue) {
        data=(String)dayValue.get(0);
        o=(double)dayValue.get(1);
        c=(double)dayValue.get(2);
        h=(double)dayValue.get(3);
        l=(double)dayValue.get(4);
    }
}
