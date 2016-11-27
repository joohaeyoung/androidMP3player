package com.example.haeyoungjoo.assignment3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by hae young Joo on 2016-11-26.
 */

public class MusicPlayControlActivity extends AppCompatActivity {

    TextView title; //텍스튜뷰
    ImageButton playandpauseview;//재생,일시정지 버튼
    int playandpause = 0; //재생,일시정지 아이콘 변경시 사용되는 변수


    /*****************************************boundService를 위한 코드 ******************************************/

    //바운드 서비스를 위한 객체, 변수.
    MusicService mService ;
    boolean mBound = false;

    //ServiceConnection 인터페이스를 구현한 ServiceConnection 객체 생성
    //onServiceConnection() 콜백 메소드와 onServiceDisconnected() 콜백 메소드를 구현해야함.

    private ServiceConnection mConnection = new ServiceConnection() {

        //Service에 연결(bound) 되었을 떄 호출되는 callback 메소드.
        //Service의 onBind() 메소드에서 반환한 IBinder 객체를 받음
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            mService = binder.getService();
            mBound =true;
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

            mBound = false;
        }
    };
    /*********************************************************************************************************************/

    /******************************************MusicPlayControlActivity 화면**********************************************/
    protected void onCreate(Bundle savedInstanceState) {

        Log.d( "ControlActivity","onCreate()" );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        title=(TextView)findViewById(R.id.title);
        playandpauseview=(ImageButton)findViewById(R.id.playandpause);


    }

    /**************************************startService와 boundService****************************************************/
    @Override
    protected void onStart(){

        Log.d( "ControlActivity","onStart()" );
        super.onStart();

        title.setText(MediaPlayerClass.title);

        //서비스를 위한 인텐트 생성.
        Intent intent = new Intent(this, MusicService.class);

        startService(intent);//스타트 서비스로 음악을 재생한다.
        Log.d("startService","startServie!!!!");


        //Service에 연결하기위해 bindService 호출, 생성한 intent 객체와 구현한 ServiceConnection 의 객체를 전달
        //boolean bindService(intent service, ServiceConnetcion coon, int flags)

        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Log.d("boundService","boundServie!!!!");

    }

    /***********************************MusicPlayControlActivity가 닫히면 실행되는 코드*******************************************************************/
    @Override
    protected  void onStop(){
        Log.d( "ControlActivity","onStop()" );

        super.onStop();
        if(mBound){
            unbindService(mConnection);
            System.out.println(mBound);
            mBound = false;
        }
    }
    /***********************이전버튼, 재생,일시정지버튼 , 다음버튼, 종료버튼 클릭시 발생하는 이벤트********************************/

    public void playandpauseClick(View v){

        if(playandpause == 0){ //음악재생중일때 클릭한다면 즉 일시정지한다면.->pause 아이콘에서 play 아이콘으로 넘어감
            Log.d("pauseClick","playandpauseClick");
            // play로 이미지 전환.
            playandpause = 1;
            Drawable drawable = getResources().getDrawable( R.drawable.ic_play_arrow_black_24dp);
            playandpauseview.setImageDrawable(drawable);

            mService.MusicPause();

        }
        else{//음악일시정지 중일때 클릭한다면  즉 다시재생한다면
            Log.d("playClick","playandpauseClick");
            //play아이콘 에서 pause 아이콘으로  이미지 전환.
            playandpause = 0;
            Drawable drawable = getResources().getDrawable( R.drawable.ic_pause_circle_filled_black_24dp);
            playandpauseview.setImageDrawable(drawable);

            mService.MussicPlay();

        }
    }

    public void prevClick(View v){


    }

    public void nextClick(View v){

    }

    public void closeClick(View v){

        System.out.println("SDfsdds");
        stopService(new Intent(this, MusicService.class));
        System.out.println("SDfsdds");

    }

}
