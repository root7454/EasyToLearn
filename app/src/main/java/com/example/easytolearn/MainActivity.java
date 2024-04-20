package com.example.easytolearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(5000);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(MainActivity.this, Login_Screen.class);
                    startActivity(intent);
                    finish();
                }
            }
        };thread.start();

    }
}