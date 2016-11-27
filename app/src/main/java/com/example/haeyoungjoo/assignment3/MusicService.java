package com.example.haeyoungjoo.assignment3;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import static com.example.haeyoungjoo.assignment3.MediaPlayerClass.player;

/**
 * Created by hae young Joo on 2016-11-26.
 */

public class MusicService extends Service {

    private static final String TAG = "MusicService";


    @Override
    public void onCreate(){

        Log.d(TAG, "onCreate()");

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand()");

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, new Intent(this, MusicPlayControlActivity.class) , PendingIntent.FLAG_UPDATE_CURRENT);//이 함수 매개변수들이 의미하는 것들. 질문하기!

        // 1-3. Notification 객체 생성
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Music service")
                .setContentText("Service is running... start an activity")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .build();

        // 2. foregound service 설정 - startForeground() 메소드 호출, 위에서 생성한 nofication 객체 넘겨줌
        startForeground(123, noti);




        MediaPlayerClass.player.start();




        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {

        Log.d(TAG, "onDestroy()");
        Toast.makeText(this, "MusicService 중지", Toast.LENGTH_SHORT).show();

        // MediaPlayer play 중지
        MediaPlayerClass.player.stop();

        // MediaPlayer 객체 해제
        MediaPlayerClass.player.release();
        MediaPlayerClass.player=null;

    }

    // Binder 클래스를 상속 받는 클래스를 정의
    // getService() 메소드에서 현재 서비스 객체를 반환
    public class MusicBinder extends Binder {
        // 클라이언트가 호출할 수 있는 공개 메소드가 있는 현재 Service 객체 반환
        MusicService getService() {
            return MusicService.this;
        }
    }
    // 위에서 정의한 Binder 클래스의 객체 생성
    // Binder 클래스는 Interface인 IBinder를 구현한 클래스
    private final IBinder mBinder = new MusicBinder();

    @Override
    public IBinder onBind(Intent intent) {

        return mBinder;
    }
    public void MusicPause(){

        player.pause();

    }

    public void MussicPlay(){
            player.start();
    }
    public void PrevPlay(){

    }
    public void NextPlay(){

    }

}
