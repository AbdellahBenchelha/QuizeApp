package com.example.quizapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.quizapp.R;
import com.example.quizapp.adapter.LeaderboardRecyclerAdapter;
import com.example.quizapp.model.LeaderListItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HomePage extends AppCompatActivity {
    RecyclerView leadboardRecyclerView;
    LeaderboardRecyclerAdapter leaderboardRecyclerAdapter;
    List<LeaderListItem> leaderListItems=new ArrayList<>();
    Button btn_start_quiz;
    ImageView btn_logout;
    TextView tv_name;
    TextView tv_email;
    FirebaseAuth firebaseAuth;
    String userID;
    RelativeLayout progress_relative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        leadboardRecyclerView=findViewById(R.id.rv_leaderboard);
        btn_start_quiz=findViewById(R.id.btn_start_quiz);
        btn_logout=findViewById(R.id.btn_logout);
        tv_name=findViewById(R.id.tv_home_name);
        tv_email=findViewById(R.id.tv_home_email);
        progress_relative=findViewById(R.id.progress_activity);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser fuser = firebaseAuth.getCurrentUser();
        tv_name.setText(fuser.getEmail());
        tv_email.setText(fuser.getDisplayName());


        leadboardRecyclerView.setHasFixedSize(true);
        leadboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getQuizeData();


        btn_start_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapLocationActivity.class));
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

    }
    public  void getQuizeData()
    {

        userID = firebaseAuth.getCurrentUser().getUid();
        leaderListItems=new ArrayList<>();
        DatabaseReference mdatabaseref = FirebaseDatabase.getInstance().getReference("history");
        mdatabaseref.addValueEventListener(new ValueEventListener() {
            String date=null;
            String quize=null;
            String score=null;


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String key1 = postSnapshot.getKey();
                    Log.e("err1",key1);
                    Log.e("err2",userID);
                    if(key1.equals(userID) ){
                        for (DataSnapshot userInfo : postSnapshot.getChildren()) {
                            for (DataSnapshot historyInfo : userInfo.getChildren()) {
                                String key = historyInfo.getKey();
                                Log.e("key", key);
                                switch (key) {
                                    case "history_date":
                                        date = historyInfo.getValue().toString();
                                        break;
                                    case "history_quize":
                                        quize = historyInfo.getValue().toString();
                                        break;
                                    case "history_score":
                                        score = historyInfo.getValue().toString();
                                        break;

                                }
                            }
                         Log.e("date",date);
                         SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

                        Date mydate = null;
                        try {
                            mydate = formatter.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                            Log.e("date",String.valueOf(mydate));
                            LeaderListItem leaderListItem = new LeaderListItem(score, mydate, "dzad.com", quize);
                            leaderListItems.add(leaderListItem);

                        }


                    }

                }
                Collections.reverse(leaderListItems);
                leaderboardRecyclerAdapter = new LeaderboardRecyclerAdapter(leaderListItems, HomePage.this);
                leadboardRecyclerView.setAdapter(leaderboardRecyclerAdapter);
                progress_relative.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}

