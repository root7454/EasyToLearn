package com.example.easytolearn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Python_course extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.python_course);


        ImageView back1;
        ImageButton chapter1;


        chapter1 = findViewById(R.id.chaptar1);
        back1 = findViewById(R.id.back1);



        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goTonextactivity();
            }
        });

        chapter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Python_course.this, Video_view.class);
                startActivity(intent);
            }
        });

    }
    private void goTonextactivity() {

        Intent intent = new Intent(Python_course.this, Home_Screen.class);
        startActivity(intent);
        finish();


    }
}