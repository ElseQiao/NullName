package com.allen.money.model.entity;

import android.util.Log;

/**
 * Created by Else.
 * Date: 2019/11/27
 * Time: 14:48
 * Describe:
 */
public class HistoryRange {
    //索引0~10 分别表示幅度>0/>1/>2/>3......的个数
    private int[] positive={0,0,0,0,0,0,0,0,0,0,0};
    private int[] mNative={0,0,0,0,0,0,0,0,0,0,0};

    private int count;



    public void addToCount(double range){
        if(range>0){
            positiveAdd(range);
        }else{
            nativeAdd(range);
        }
    }

    private void nativeAdd(double range) {
        int mCase=(int)(range/(-1));
        if(mCase>10){
            mCase=10;
        }
        for(int i=0;i<=mCase;i++){
            mNative[i]=mNative[i]+1;
        }

    }
    private void positiveAdd(double range) {
        int mCase=(int)(range/1);
        if(mCase>10){
            mCase=10;
        }
        for(int i=0;i<=mCase;i++){
            positive[i]=positive[i]+1;
        }
    }

    public void setTotalCount(int count) {
        this.count=count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        for(int i=0;i<positive.length;i++){
            Log.d("test", "toString: -zhang-----大于"+i+":----"+positive[i]+"------比例："+((double)positive[i]/count));

        }
        for(int i=0;i<mNative.length;i++){
            Log.d("test", "toString: -die-----小于"+i+":===="+mNative[i]+"------比例："+((double)mNative[i]/count));

        }

        return "";
    }
}
