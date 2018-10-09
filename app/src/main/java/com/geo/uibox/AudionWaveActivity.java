package com.geo.uibox;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.geo.widget.AudionWaveView;

import java.io.File;
import java.io.IOException;

/**
 * Created by liwei on 2018/9/26.
 */

public class AudionWaveActivity extends BaseActivity{

    private int BASE = 600;
    private int SPACE = 20;// 间隔取样时间
    boolean isStart=false;
    AudionWaveView mAudionWaveView;
    MediaRecorder mRecorder;
    String mFilePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audion_wave_layout);
        final Button start = (Button) findViewById(R.id.audion_wave_start);
        mAudionWaveView = (AudionWaveView) findViewById(R.id.audion_wave);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isStart){
                    start.setText("开始录音");
                    stopRecording();
                }else {
                    start.setText("停止录音");
                    startRecording();
                }
            }
        });
    }
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {

                updateMicStatus();
        }
    };
    private final Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
                int db = (int) msg.obj;
            mAudionWaveView.setAudio(db);
        }
    };
    // 开始录音
    public void startRecording() {
        isStart=true;
        File file = new File(android.os.Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + "/data/Langao/audio");
        if(!file.exists()){
            file.mkdirs();
        }
        mFilePath = android.os.Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + "/data/Langao/audio"+"/"+System.currentTimeMillis()+".mp3";
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); //录音文件保存的格式，这里保存为 mp4
        mRecorder.setOutputFile(mFilePath); // 设置录音文件的保存路径
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setAudioChannels(1);
        // 设置录音文件的清晰度
        mRecorder.setAudioSamplingRate(44100);
        mRecorder.setAudioEncodingBitRate(192000);

        try {
            mRecorder.prepare();
            new Thread(mUpdateMicStatusTimer).start();
            mRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
            isStart=false;
            Log.e("AudioRecorder",e.getMessage());
        }
    }

    private void updateMicStatus() {
        if (mRecorder != null) {
            // int vuSize = 10 * mMediaRecorder.getMaxAmplitude() / 32768;
            int ratio = mRecorder.getMaxAmplitude() / BASE;
            int db = 0;// 分贝
            if (ratio > 1)
                db = (int) (20 * Math.log10(ratio));
            System.out.println("分贝值："+db+"     "+Math.log10(ratio));

            Log.e("AUDIOAJK","分贝值: "+db);
            switch (db / 4) {
                case 0:
//                    view.setImageBitmap(null);
                    break;
                case 1:
//                    view.setImageResource(R.drawable.logo64);
                    break;
                case 2:
//                    view.setImageResource(R.drawable.logo96);
                    break;
                case 3:
//                    view.setImageResource(R.drawable.logo124);
                    break;
                case 4:
//                    view.setImageResource(R.drawable.logo135);
                    break;
                case 5:
//                    view.setImageResource(R.drawable.logo512);
                    break;
                default:
//                    view.setImageResource(R.drawable.ic_launcher);
                    break;
            }
            Message msg = mHandler.obtainMessage();
            msg.obj=db;
            mHandler.sendMessage(msg);
            if(isStart){
                mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
//                mHandler.post(mUpdateMicStatusTimer);
            }
        }
    }


    // 停止录音
    public void stopRecording() {
        if(mRecorder!=null) {
            try{
                mRecorder.stop();
            } catch (IllegalStateException e) {
                // TODO 如果当前java状态和jni里面的状态不一致，
                //e.printStackTrace();
                mRecorder = null;
                mRecorder = new MediaRecorder();
            }
            isStart=false;
            mRecorder.release();
            mRecorder = null;
        }
    }
    @Override
    protected void initToolBar(boolean isShowBackArrow) {
        super.initToolBar(isShowBackArrow);
        mToolBar.setTitle("录音波纹");
    }
}
