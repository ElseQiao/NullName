package com.allen.money.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.allen.money.R;
import com.allen.money.bean.NetEasyHistory;
import com.allen.money.utils.HistoryAnalyse;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,
        OnChartGestureListener, OnChartValueSelectedListener {
    private static final String TAG = "MainActivity";
    private Handler mhandler = new Handler();
    String api = "http://img1.money.126.net/data/hs/kline/day/history/2019/1300454.json";



    String base = "http://img1.money.126.net/data/hs/kline/day/history/";
    String year="2019";
    String code="399001";
    private Button button;
    private HistoryAnalyse historyAnalyse;
    private EditText editText_code;
    private EditText editText_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        historyAnalyse = new HistoryAnalyse();
        initView();
    }


    //TODO 比较大盘，和大盘的同步性



    private void initView() {
        inch();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[0]);
            int l = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[1]);
            int m = ContextCompat.checkSelfPermission(getApplicationContext(), permissions[2]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED || l != PackageManager.PERMISSION_GRANTED || m != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                startRequestPermission();
            }
        }
        editText_code= findViewById(R.id.editText_code);
        editText_data=findViewById(R.id.editText_data);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestData();
            }
        });
        testChart();
    }

    private LineChart chart;
    private void testChart() {
        //
        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);

        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setDrawBorders(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
    }

    private void requestData() {
        String c=editText_code.getText().toString();
        String y=editText_data.getText().toString();
        if(c.equals("")||y.equals("")){
            toastShow("未设置");
            return;
        }

        if(c.startsWith("6")){
            code="0"+c;
        }else{
            code="1"+c;
        }

        if(code.equals("1000001")){
            code="0000001";
            toastShow("默认为sz");
        }

        String request=base+y+"/"+code+".json";

        OkGo.<String>get(request).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Log.d(TAG, "onSuccess: " + response.body());
                Gson gson = new Gson();
                NetEasyHistory netEasyHistory = gson.fromJson(response.body(), NetEasyHistory.class);
                historyAnalyse.analyseHistory(netEasyHistory);
                chartShow(netEasyHistory);
            }

        });
    }

    private final int[] colors = new int[] {
            Color.GREEN,
            Color.RED
    };

    private void chartShow(NetEasyHistory netEasyHistory) {
        chart.resetTracking();


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        List<List<Object>> ls=netEasyHistory.getData();
        ArrayList<Entry> valuesO = new ArrayList<>();
        ArrayList<Entry> valuesC = new ArrayList<>();
        for(int i=0;i<ls.size();i++){

            double o=(double)ls.get(i).get(NetEasyHistory.DATA_INDEX_O);
            valuesO.add(new Entry(i, (float) o));
            double c=(double)ls.get(i).get(NetEasyHistory.DATA_INDEX_C);
            valuesC.add(new Entry(i, (float) c));
        }
        LineDataSet d_o = new LineDataSet(valuesO, "O ");
        d_o.setLineWidth(1f);
        d_o.setCircleRadius(2f);
        LineDataSet d_c = new LineDataSet(valuesC, "C");
        d_c.setLineWidth(1f);
        d_c.setCircleRadius(2f);

        int color = colors[0];
        d_o.enableDashedLine(10, 10, 0);
        d_o.setColor(color);
        d_o.setCircleColor(color);
        d_c.setColor(colors[1]);
        d_c.setCircleColor(colors[1]);

        dataSets.add(d_o);
        dataSets.add(d_c);


        // make the first DataSet dashed
//        ((LineDataSet) dataSets.get(0)).enableDashedLine(10, 10, 0);
//        ((LineDataSet) dataSets.get(0)).setColors(ColorTemplate.VORDIPLOM_COLORS);
//        ((LineDataSet) dataSets.get(0)).setCircleColors(ColorTemplate.VORDIPLOM_COLORS);

        LineData data = new LineData(dataSets);
        chart.setData(data);
        chart.invalidate();

    }


    private void toastShow(String s) {
        Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(Build.VERSION_CODES.DONUT)
    private void inch() {
         //Display display = getWindow().getWindowManager().getDefaultDisplay();
         //DisplayMetrics displayMetrics = new DisplayMetrics();
         //display.getMetrics(displayMetrics);
         //
         //int width = displayMetrics.widthPixels;//宽度
         //int height = displayMetrics.heightPixels;//高度
         //float density = displayMetrics.density;//密度
         //int densityDpi = displayMetrics.densityDpi; //每英寸点数(打印分辨率)
         //float xdpi = displayMetrics.xdpi;//x轴物理密度
         //float ydpi = displayMetrics.ydpi;//y轴物理密度
         //Log.d(TAG, "inch:------------ " + width + "-----" + height + "-----" + density + "-----" + densityDpi + "-----");
         //
         //getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
         //int width1 = displayMetrics.widthPixels;//实际宽度（含状态栏）
         //int height1 = displayMetrics.heightPixels;//实际高度（含状态栏）
         //Log.d(TAG, "inch:------------ " + width1 + "-----" + height1);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private String[] permissions = {Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};


    /**
     * 开始提交请求权限
     */
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //如果没有获取权限，那么可以提示用户去设置界面--->应用权限开启权限
                } else {
                    //获取权限成功提示，可以不要
                    Toast toast = Toast.makeText(this, "获取权限成功", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        }
    }
}
