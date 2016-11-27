package com.example.haeyoungjoo.assignment3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    File musicDir; //공용디렉토리 (Music) 에 접근하는 File 객체

    private ListView m_ListView;//리스트뷰! (어뎁터 뷰)
    private ArrayAdapter<String> m_Adapter; //배열에서 값을 가져오는 어뎁터


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity","onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //*******************************************************************
        // Runtime permission check
        //*******************************************************************
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
            // READ_EXTERNAL_STORAGE 권한이 있는 것이므로
            // Public Directory에 접근할 수 있고 거기에 있는 파일을 읽을 수 있다
            //prepareAccessToMusicDirectory();
        }

        musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

        File files[] = {};

        files =  musicDir.listFiles();

        Vector<String> values = new Vector<String>();


        for( int i = 0 ; i<files.length; i++)
        {
            values.addElement(files[i].getName());
        }

        for( int i = 0 ; i<files.length; i++)
            System.out.println( files[i].getAbsolutePath() );

        m_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);//어뎁터 생성.

        // Xml에서 추가한 ListView의 객체
        m_ListView = (ListView) findViewById(R.id.list);

        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);

        // ListView 아이템 터치 시 이벤트를 처리할 리스너 설정
        m_ListView.setOnItemClickListener(onClickListItem);


    }



    // 아이템 터치 이벤트 리스너 구현
    private AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Log.d("MainActivity","리스트 아이템 클릭");

            String data =  m_Adapter.getItem(position);//리스트의 해당 위치에 있는 아이템을 가져와서 String data에 저장.

            MediaPlayerClass.title = data;//재생음악 제목 전역변수에 초기화.

            //리스트 클릭시 만약 재생중인게 있다면 재생중인것을 멈추고 새로 재생한다.
            if ( MediaPlayerClass.player != null){

                MediaPlayerClass.player.stop();
                MediaPlayerClass.player.release();
                MediaPlayerClass.player = null;
            }

            MediaPlayerClass.player =new MediaPlayer();
            try {
                MediaPlayerClass.player.setDataSource("/storage/emulated/0/Music/"+MediaPlayerClass.title);
                MediaPlayerClass.player.prepare();//미리준비한다.

            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(MainActivity.this, MusicPlayControlActivity.class);//ViewActivity.class로 넘겨주는 인텐트 생성
            startActivity(intent);// MusicPlayControll.class 로 액티비티 전환!

        }

    };























    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // read_external_storage-related task you need to do.

                    // READ_EXTERNAL_STORAGE 권한을 얻었으므로
                    // 관련 작업을 수행할 수 있다

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    // 권한을 얻지 못 하였으므로 파일 읽기를 할 수 없다
                    // 적절히 대처한다
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
