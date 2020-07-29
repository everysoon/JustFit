package com.example.justfit_minsun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //차트에 사용되는 변수
    LineChart weightChart, exersiceChart;
    String[] echartLabel = new String[31];
    String[] wchartLabel = new String[31];
    ArrayList<Entry> echartValue = new ArrayList<>();
    ArrayList<Entry> wchartValue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        //init
        weightChart = (LineChart) findViewById(R.id.weightGraph);
        exersiceChart = (LineChart) findViewById(R.id.exersiceGraph);
        initChartValue();

        // Navigation Drawer (옆구리 네비게이션 바)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();


        //몸무게 차트 만들기
        LineDataSet wdataSet = new LineDataSet(wchartValue, "이번 달 칼로리 섭취량");
        LineData wdata = new LineData(wdataSet);
        wdataSet.setColor(ColorTemplate.getHoloBlue());
        weightChart.setData(wdata);
        weightChart.animateY(1000);
        //차트 옵션주기
        wdataSet.setLineWidth(2);
        wdataSet.setCircleRadius(6);
        wdataSet.setCircleColor(Color.parseColor("#6c6c6c"));
        wdataSet.setColor(Color.parseColor("#6c6c6c"));
        wdataSet.setDrawCircleHole(true);
        wdataSet.setDrawCircles(true);
        wdataSet.setDrawHorizontalHighlightIndicator(false);
        wdataSet.setDrawHighlightIndicators(false);
        wdataSet.setDrawValues(false);

        XAxis wxAxis = weightChart.getXAxis();
        wxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        wxAxis.setTextColor(Color.BLACK);
        wxAxis.enableGridDashedLine(8, 24, 0);
        wxAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (wchartLabel[(int) value] != null) {
                    return wchartLabel[(int) value];
                } else {
                    for (int i = 0; i < 30; i++)
                        Log.e("empty", wchartLabel[i]);

                }
                return null;
            }
        });
        wxAxis.setLabelCount(wchartLabel.length, true);

        YAxis wyLAxis = weightChart.getAxisLeft();
        wyLAxis.setTextColor(Color.BLACK);

        YAxis wyRAxis = weightChart.getAxisRight();
        wyRAxis.setDrawLabels(false);
        wyRAxis.setDrawAxisLine(false);
        wyRAxis.setDrawGridLines(false);

        Description wdescription = new Description();
        wdescription.setText("단위 : kg");
        weightChart.setDoubleTapToZoomEnabled(false);
        weightChart.setDrawGridBackground(false);
        weightChart.invalidate();
        weightChart.setDescription(wdescription);
        MyMarkerView marker = new MyMarkerView(this, R.layout.markerviewtext);
        marker.setChartView(weightChart);
        weightChart.setMarker(marker);


        //운동 차트 만들기///////////////////////////////////////////////////////////////////////////////
        LineDataSet edataSet = new LineDataSet(echartValue, "이번 달 칼로리 섭취량");
        LineData edata = new LineData(edataSet);
        edataSet.setColor(ColorTemplate.getHoloBlue());
        exersiceChart.setData(edata);
        exersiceChart.animateY(1000);
        //차트 옵션주기
        edataSet.setLineWidth(2);
        edataSet.setCircleRadius(6);
        edataSet.setCircleColor(Color.parseColor("#6c6c6c"));
        edataSet.setColor(Color.parseColor("#6c6c6c"));
        edataSet.setDrawCircleHole(true);
        edataSet.setDrawCircles(true);
        edataSet.setDrawHorizontalHighlightIndicator(false);
        edataSet.setDrawHighlightIndicators(false);
        edataSet.setDrawValues(false);

        XAxis exAxis = exersiceChart.getXAxis();
        exAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        exAxis.setTextColor(Color.BLACK);
        exAxis.enableGridDashedLine(8, 24, 0);
        exAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (echartLabel[(int) value] != null) {
                    return echartLabel[(int) value];
                } else {
                    for (int i = 0; i < 30; i++)
                        Log.e("empty", echartLabel[i]);

                }
                return null;
            }
        });
        exAxis.setLabelCount(echartLabel.length, true);

        YAxis eyLAxis = exersiceChart.getAxisLeft();
        eyLAxis.setTextColor(Color.BLACK);

        YAxis eyRAxis = exersiceChart.getAxisRight();
        eyRAxis.setDrawLabels(false);
        eyRAxis.setDrawAxisLine(false);
        eyRAxis.setDrawGridLines(false);

        Description edescription = new Description();
        edescription.setText("단위 : 분");

        exersiceChart.setDoubleTapToZoomEnabled(false);
        exersiceChart.setDrawGridBackground(false);
        exersiceChart.invalidate();
        exersiceChart.setDescription(edescription);

        MyMarkerView emarker = new MyMarkerView(this, R.layout.markerviewtext);
        emarker.setChartView(exersiceChart);
        exersiceChart.setMarker(emarker);

    }

    public void initChartValue() {
        for (int i = 0; i < echartLabel.length; i++) {
            echartLabel[i] = ((i + 1) + "일");
            wchartLabel[i] = ((i + 1) + "일");
        }
        for (int i = 0; i < 30; i++) {
            echartValue.add(new Entry(i + 1, (int) (Math.random() * 30) + 90));
        }
        wchartValue.add(new Entry(1,80));
        wchartValue.add(new Entry(2,(float)79.8));
        wchartValue.add(new Entry(3,(float)79.7));
        wchartValue.add(new Entry(4,(float)80.1));
        wchartValue.add(new Entry(5,(float)79.9));
        wchartValue.add(new Entry(6,(float)79.5));
        wchartValue.add(new Entry(7,(float)79.5));
        wchartValue.add(new Entry(8,(float)79.2));
        wchartValue.add(new Entry(9,(float)79.3));
        wchartValue.add(new Entry(10,(float)79.1));
        wchartValue.add(new Entry(11,(float)78.9));
        wchartValue.add(new Entry(12,(float)79.1));
        wchartValue.add(new Entry(13,(float)78.8));
        wchartValue.add(new Entry(14,(float)78.9));
        wchartValue.add(new Entry(15,(float)78.7));
        wchartValue.add(new Entry(16,(float)78.9));
        wchartValue.add(new Entry(17,(float)78.7));
        wchartValue.add(new Entry(18,(float)78.4));
        wchartValue.add(new Entry(19,(float)78.7));
        wchartValue.add(new Entry(20,(float)78.1));
        wchartValue.add(new Entry(21,(float)78.5));
        wchartValue.add(new Entry(22,(float)78.4));
        wchartValue.add(new Entry(23,(float)78.2));
        wchartValue.add(new Entry(24,(float)78.2));
        wchartValue.add(new Entry(25,(float)78.2));
        wchartValue.add(new Entry(26,(float)78.1));
        wchartValue.add(new Entry(27,(float)78.2));
        wchartValue.add(new Entry(28,(float)77.9));
        wchartValue.add(new Entry(29,(float)77.8));

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id) {
            // 운동한날보이기
            case R.id.nav_calendar:
                startActivity(new Intent(this,CalendarActivity.class));
                break;
            // 식단관리로 이동
            case R.id.nav_dietManagement:
                startActivity(new Intent(this, DietManagementActivity.class));
                break;
            // 프로필로이동
            case R.id.nav_myProfile:
                startActivity(new Intent(this, MyProfileActivity.class));
                break;
            case R.id.nav_video:
                startActivity(new Intent(this, VideoActivity.class));
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
