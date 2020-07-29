package com.example.justfit_minsun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Map;

public class DietManagementActivity extends AppCompatActivity implements SensorEventListener,View.OnClickListener,NavigationView.OnNavigationItemSelectedListener  {

    Button BreakfastButton,morningSnackButton,midnightSnackkButton,launchButton,afternoonSnack,water,exersice,dinnerButton;
    SensorManager sensorManager;
    Sensor stepCountSensor;
    TextView stepcount,stepcount2;
    int calrory = 0;
    int sum = 0;
    //차트에 사용되는 변수
    LineChart calroryChart;
    String[] chartLabel = new String[31];
    ArrayList<Entry> chartValue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_management);
        //init
        stepcount = (TextView) findViewById(R.id.stepCount);
        stepcount2 = (TextView) findViewById(R.id.stepCount2);
        calroryChart = (LineChart)findViewById(R.id.calroryGraph);
        chartInit();
        //Button init
        water = (Button)findViewById(R.id.waterButton);
        afternoonSnack = (Button)findViewById(R.id.afternoonSnackButton);
        BreakfastButton = (Button)findViewById(R.id.BreakfastButton);
        midnightSnackkButton = (Button)findViewById(R.id.midnightSnackkButton);
        morningSnackButton = (Button)findViewById(R.id.morningSnackButton);
        launchButton = (Button)findViewById(R.id.launchButton);
        dinnerButton = (Button)findViewById(R.id.dinnerButton);
        exersice =(Button)findViewById(R.id.exersiceButton);
        BreakfastButton.setOnClickListener(this);
        morningSnackButton.setOnClickListener(this);
        midnightSnackkButton.setOnClickListener(this);
        launchButton.setOnClickListener(this);
        afternoonSnack.setOnClickListener(this);
        water.setOnClickListener(this);
        exersice.setOnClickListener(this);
        dinnerButton.setOnClickListener(this);

        //핸드폰에 내장되어있는 움직임 센서가지고 만보계
        sensorManager =(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor =sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(stepCountSensor == null){
            Toast.makeText(this,"No Step Detect Sensor",Toast.LENGTH_SHORT).show();
            Log.e("StepSensor?","No Step Sensor");
            stepcount.setText("핸드폰에 내장된 센서가 없습니다.");
            stepcount2.setText(" ");

        }

        //차트 만들기
        LineDataSet dataSet = new LineDataSet(chartValue,"이번 달 칼로리 섭취량");
        LineData data = new LineData(dataSet);
        dataSet.setColor(ColorTemplate.getHoloBlue());
        calroryChart.setData(data);
        calroryChart.animateY(1000);
        //차트 옵션주기
        dataSet.setLineWidth(2);
        dataSet.setCircleRadius(6);
        dataSet.setCircleColor(Color.parseColor("#6c6c6c"));
        dataSet.setColor(Color.parseColor("#6c6c6c"));
        dataSet.setDrawCircleHole(true);
        dataSet.setDrawCircles(true);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setDrawHighlightIndicators(false);
        dataSet.setDrawValues(false);

        XAxis xAxis = calroryChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.enableGridDashedLine(8, 24, 0);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (chartLabel[(int) value] != null) {
                    return chartLabel[(int) value];
                } else {
                    for(int i=0;i<30;i++)
                        Log.e("empty", chartLabel[i]);

                }
                return null;
            }
        });
        xAxis.setLabelCount(chartLabel.length,true);

        YAxis yLAxis = calroryChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);

        YAxis yRAxis = calroryChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        Description description = new Description();
        description.setText("단위 : kcal");
        calroryChart.setDoubleTapToZoomEnabled(false);
        calroryChart.setDrawGridBackground(false);
        calroryChart.invalidate();
        calroryChart.setDescription(description);
        MyMarkerView marker = new MyMarkerView(this,R.layout.markerviewtext);
        marker.setChartView(calroryChart);
        calroryChart.setMarker(marker);

        // Navigation Drawer (옆구리 네비게이션 바)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
    }
    public void chartInit(){
        for(int i=0; i<chartLabel.length; i++) {
            chartLabel[i] = ((i + 1) + "일");
        }
        for(int i=0; i<30; i++) {
            chartValue.add(new Entry(i+1, (int) (Math.random()*1000)+1500));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            stepcount.setText(String.valueOf(sensorEvent.values[0])+" 걸음");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    @Override
    public void onClick(View view) {
        int clickButton = view.getId();
        switch (clickButton){

            case R.id.waterButton:
                waterClickHandler();
                break;
            case R.id.exersiceButton:
                exersiceClickHandler();
                break;
            case R.id.afternoonSnackButton:
                foodClickHandler(afternoonSnack);
                break;
            case R.id.BreakfastButton:
                foodClickHandler(BreakfastButton);
                break;
            case R.id.midnightSnackkButton:
                foodClickHandler(midnightSnackkButton);
                break;
            case R.id.morningSnackButton:
                foodClickHandler(morningSnackButton);
                break;
            case R.id.launchButton:
                foodClickHandler(launchButton);
                break;
            case R.id.dinnerButton:
                foodClickHandler(dinnerButton);
                break;

        }
    }
    public void waterClickHandler(){
        final EditText editText = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("오늘 먹은 물의 양을 입력해주세요.(단위 : ml)");
        FrameLayout container = new FrameLayout(getApplicationContext());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        editText.setLayoutParams(params);
        container.addView(editText);

        builder.setView(container).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String editWater = editText.getText().toString();
                Toast.makeText(getApplicationContext(),editWater+"ml",Toast.LENGTH_SHORT).show();
                water.setText(String.valueOf(editWater)+"ml");
            }
        });

      AlertDialog alertDialog = builder.create();
      alertDialog.show();
    }
    public void exersiceClickHandler(){
        final EditText editText = new EditText(this);
        //다이얼로그에 editText추가
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("운동한 시간을 적어주세요.(단위 : 분)");
        FrameLayout container = new FrameLayout(getApplicationContext());
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        editText.setLayoutParams(params);
        container.addView(editText);

        builder.setView(container).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String editTime = editText.getText().toString();
                exersice.setText(editTime+"분");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void foodClickHandler(final Button buttonName){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("오늘 먹은 음식을 선택해주세요.");
        builder.setItems(R.array.food_name, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                String[] food_name = getResources().getStringArray(R.array.food_name);
                String[] food_calrory = getResources().getStringArray(R.array.food_calrory);
                calrory = Integer.valueOf(food_calrory[position]);
                sum += calrory;
                Log.e("calrory?",String.valueOf(sum));
                buttonName.setText(String.valueOf(sum)+"kcal");
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
