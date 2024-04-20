package com.example.easytolearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Home_Screen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        ImageView profile, python;

        ImageSlider imageSlider = findViewById(R.id.sliding_img);

        python = findViewById(R.id.py_c);



        ArrayList<SlideModel> slideModels = new ArrayList<>();

        profile = findViewById(R.id.profile);
        slideModels.add(new SlideModel(R.drawable.sliding_img, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.sliding_img1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.sliding_img2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.sliding_img3, ScaleTypes.FIT));


        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Home_Screen.this, Profile_Activity.class));

            }
        });
        python.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Home_Screen.this, Course_preview.class));


            }
        });

    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}