package com.example.easytolearn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ui.PlayerControlView;

public class Video_view extends AppCompatActivity {
    String url = "https://firebasestorage.googleapis.com/v0/b/easy-to-learn-52514.appspot.com/o/What%20is%20Python_%20_%20Python%20Programming%20For%20Beginners%20_%20Python%20Tutorial%20_%20Edureka.mp4?alt=media&token=497f6d12-6352-44fc-9309-b96cec1aa67c";
    int position = -1;
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

            videoView = findViewById(R.id.video_view);

            position = getIntent().getIntExtra("p", -1);
            if (url!=null){
                videoView.setVideoPath(url);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        videoView.start();
                    }
                });
            }else {
                Toast.makeText(this, "path didn't exits", Toast.LENGTH_SHORT).show();
            }

    }
}