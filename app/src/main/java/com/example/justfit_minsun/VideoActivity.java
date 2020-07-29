package com.example.justfit_minsun;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , Button.OnClickListener{

    VideoAdapter adapter;
    ArrayList<Video> vlist;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button videoSelectButton;
    int position;
    MyService myService;
    boolean isService = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        serviceBind();
        //recyclerView Init
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        vlist = new ArrayList<>();
        addItem();
        videoSelectButton = (Button) findViewById(R.id.videoSelectbutton);
        videoSelectButton.setOnClickListener(this);
        // 리싸이클러뷰

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new VideoAdapter(getApplicationContext(),vlist);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                   setPosition(position);

            }
        });
        adapter.notifyDataSetChanged();
        recyclerView.setHasFixedSize(true);

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
    public void addItem(){
        vlist.add(new Video(getDrawable(R.drawable.r1),"스트레칭","전사 자세2","요가 소년"));
        vlist.add(new Video(getDrawable(R.drawable.r4),"스트레칭","운동 전 스트레칭(1)","심으뜸"));
        vlist.add(new Video(getDrawable(R.drawable.r3),"승모근(상체)","승모근 스트레칭","강하나"));
        vlist.add(new Video(getDrawable(R.drawable.r4),"스트레칭","운동 전 스트레칭(2)","심으뜸"));
        vlist.add(new Video(getDrawable(R.drawable.r5),"하체","하체근력 강화운동","강하나"));

    }

    public void setPosition(int position) {
        this.position = position;
        Log.e("setPosition",position+"");
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isService){
            unbindService(conn); // 서비스 종료
            isService = false;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            // 서비스와 연결되었을 때 호출되는 메서드
            // 서비스 객체를 전역변수로 저장
            MyService.MyBinder mb = (MyService.MyBinder) service;
            myService = mb.getService(); // 서비스가 제공하는 메소드 호출하여
            // 서비스쪽 객체를 전달받을수 있슴
            isService = true;
            Toast.makeText(getApplicationContext(),
                    "서비스 연결",
                    Toast.LENGTH_LONG).show();
        }

        public void onServiceDisconnected(ComponentName name) {
            // 서비스와 연결이 끊겼을 때 호출되는 메서드
            isService = false;
            Toast.makeText(getApplicationContext(),
                    "서비스 연결 해제",
                    Toast.LENGTH_LONG).show();
        }
    };
    public void serviceBind(){
        Intent intent = new Intent(
                VideoActivity.this, // 현재 화면
                MyService.class); // 다음넘어갈 컴퍼넌트

        bindService(intent, // intent 객체
                conn, // 서비스와 연결에 대한 정의
                Context.BIND_AUTO_CREATE);
        //처음 서비스를 시작하는 액티비티에서는 Context.BIND_AUTO_CREATE
        //다른 액티비티에서는 Context.BIND_NOT_FOREGROUND를 주어야합니다.
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(VideoActivity.this,"화면 전환 중 입니다...",Toast.LENGTH_LONG).show();
        //버튼 누르면 어떤 영상이 전해 졌는지 소켓통신으로 알려주기
        Log.e("position?",String.valueOf(position));
        int value = position+1;
        myService.sendMessage(String.valueOf(value));
    }
}
