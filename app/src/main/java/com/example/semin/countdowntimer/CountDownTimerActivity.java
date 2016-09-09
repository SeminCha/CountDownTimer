package com.example.semin.countdowntimer;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class CountDownTimerActivity extends Activity {

    private static final int MILLISINFUTURE = 11 * 2000;

    private static final int COUNT_DOWN_INTERVAL = 1000;

    int count;
    private CustomDialog mCustomDialog;
    TextView countTxt;
    CountDownTimer countDownTimer;
    Vibrator vibrator;
    MediaPlayer mp;
    //private PopupWindow window;
    private Button btn_cancel;
    private int mWidthPixels, mHeightPixels;


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_background);


    }

    public void btn_countdown_click(View v) {

        initiateAlertDialog();
    }

//    public void btn_cancel_click(View v) {
//
//        countDownTimer.cancel();
//    }


    public void countDownTimer() {


        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {

            public void onTick(long millisUntilFinished) {
                playSound();
                CustomDialog.setCountTxt(String.valueOf(count));
                vibrator.vibrate(500);
                count--;

            }

            public void onFinish() {

                CustomDialog.setCountTxt(String.valueOf("신고완료"));
                mp.stop();
                mCustomDialog.dismiss();
            }
        };
    }

    public void playSound() {

//        if(mp.isPlaying())
//        {
//            mp.stop();
//        }
        try {
            mp.reset();
            AssetFileDescriptor afd;
            afd = getAssets().openFd("alarmsound.mp3");
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.prepare();
            mp.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        try {

            countDownTimer.cancel();

        } catch (Exception e) {
        }

        countDownTimer = null;

    }

    private void initiateAlertDialog() {
        try {
//            //  LayoutInflater 객체와 시킴
           LayoutInflater inflater = (LayoutInflater) CountDownTimerActivity.this
                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            View layout = inflater.inflate(R.layout.activity_countdown_timer,
                  (ViewGroup) findViewById(R.id.countdown_window));

//            window = new PopupWindow(layout, 600, 500, true);
//            window.showAtLocation(layout, Gravity.CENTER, 0, 0);
//            btn_cancel = (Button) layout.findViewById(R.id.btn_cancel);
//            btn_cancel.setOnClickListener(cancel_button_click_listener);
            mCustomDialog = new CustomDialog(this, cancelClickListener);

            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            mp = new MediaPlayer();
            count = 20;
            countDownTimer();
            countDownTimer.start();

            mCustomDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //팝업창 닫기
    private View.OnClickListener cancelClickListener =
            new View.OnClickListener() {

                public void onClick(View v) {
                    countDownTimer.cancel();
                    mp.stop();
                   mCustomDialog.dismiss();
                }
            };

}