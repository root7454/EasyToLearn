package com.example.easytolearn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Dashbord extends AppCompatActivity {

    RecyclerView recview;
    myadapter adapter;
    SearchView searchView;
    FloatingActionButton fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);


        searchView = findViewById(R.id.search_icon);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return true;
            }
        });

        recview=(RecyclerView)findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<stored_data> options =
                new FirebaseRecyclerOptions.Builder<stored_data>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users"), stored_data.class)
                        .build();

        adapter=new myadapter(options);
        recview.setAdapter(adapter);
        fb=(FloatingActionButton)findViewById(R.id.fadd);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Adding_usersInFB.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void processsearch(String s) {
        FirebaseRecyclerOptions<stored_data> options =
                new FirebaseRecyclerOptions.Builder<stored_data>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").orderByChild("name").startAt(s).endAt(s+"\uf8ff"), stored_data.class)
                        .build();

        adapter=new myadapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);
    }


}