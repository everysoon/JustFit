package com.example.justfit_minsun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;

public class CalendarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    MaterialCalendarView calendarView;
    String[] eventDay ={"2019,11,1","2019,11,2","2019,11,6","2019,11,9","2019,11,11","2019,11,13","2019,11,14"
                        ,"2019,10,28","2019,10,22","2019,10,18","2019,10,15","2019,10,7"}; //특정 날짜 선택
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = (MaterialCalendarView)findViewById(R.id.calendar); //calendarView init
        calendarView.state().edit() //CalendarView 설정
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2019,6,10))
                .setMaximumDate(CalendarDay.from(2021,11,14))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        calendarView.addDecorators(new WeekendDecorator(0),new WeekendDecorator(1),new oneDayDecorater());
        new ApiSimulator(eventDay).executeOnExecutor(Executors.newSingleThreadExecutor());
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int year =date.getYear();
                int month =date.getMonth();
                int day =date.getDay();

                String shot_day =year+","+month+","+day;
                Log.e("?",shot_day);
                calendarView.clearSelection();
            }
        });
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

    //날짜를 꾸며주는 데코레이터 클래스들
    //weekend - 토(파랑), 일(빨강)으로 색 설정
    //oneday -오늘 날짜의 색 설정
    //eventDecorator - 특정 날짜에 효과주기
    //출처 : https://dpdpwl.tistory.com/3

    public class WeekendDecorator implements DayViewDecorator {

        Calendar calendar =Calendar.getInstance();
        int sundayAndSaturday; // 0이면 일요일, 1이면 토요일로지정

        public WeekendDecorator(int sundayAndSaturday){
            this.sundayAndSaturday =sundayAndSaturday;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            if(sundayAndSaturday == 0 )
                return weekday == Calendar.SUNDAY;
            else
                return weekday == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            if(sundayAndSaturday == 0)
                view.addSpan(new ForegroundColorSpan(Color.RED));
            else
                view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    public class oneDayDecorater implements DayViewDecorator{

        CalendarDay date;

        public oneDayDecorater(){
            date = CalendarDay.today();
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return date != null & day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.8f));
            view.addSpan(new ForegroundColorSpan(Color.BLACK));
        }

        public void setDate(Date date){
            this.date = CalendarDay.from(date);
        }
    }
    public class EventDecorator implements DayViewDecorator{

        int color;
        HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay>dates){
            this.color = color;
            this.dates = new HashSet<>(dates);
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5,color));
        }
    }
    public class ApiSimulator extends AsyncTask<Void,Void, List<CalendarDay>>{

        String [] eventday;

        public ApiSimulator(String [] eventday){
            this.eventday = eventday;
        }
        @Override
        protected List<CalendarDay> doInBackground(Void... voids) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {e.printStackTrace();}

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();
            //특정 날짜 달력에 점 표시해주는 곳
            //String인 문자열인 eventday를 받아와서 ,를 기준으로 자르고 String을 int로 변환
            for(int i=0; i<eventday.length; i++){
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = eventDay[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayofmonth = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year,month-1,dayofmonth);
            }
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            calendarView.addDecorator(new EventDecorator(Color.YELLOW, calendarDays));
        }
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
