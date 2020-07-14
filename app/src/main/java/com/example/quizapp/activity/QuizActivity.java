package com.example.quizapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quizapp.R;
import com.example.quizapp.Util;
import com.example.quizapp.adapter.QuizModelAdapter;
import com.example.quizapp.model.Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {
    RecyclerView recyclerview_quiz_option;
    TextView tv_step,tv_question_name,tv_quiz_time;
    Button btn_Next;
    ArrayList<Question> questionArrayList;
    int step;
    CountDownTimer countDownTimer;
    ImageView img_quize;
    RelativeLayout progress_relative;
    FirebaseAuth firebaseAuth;
    String userID;
    DatabaseReference mdatabaseref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        recyclerview_quiz_option=findViewById(R.id.recyclerview_quiz_option);
        tv_step=findViewById(R.id.tv_step);
        tv_question_name=findViewById(R.id.tv_question_name);
        tv_quiz_time=findViewById(R.id.tv_quiz_time);
        btn_Next=findViewById(R.id.btn_next);
        img_quize=findViewById(R.id.img_quize);
        progress_relative=findViewById(R.id.progress_activity);

        Util.SCORE=0;
        step=0;
        Log.e("zeze",String.valueOf(step));
        recyclerview_quiz_option.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerview_quiz_option.setLayoutManager(layoutManager);

        getQuizeData();



        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Util.ITEMSELECT==-1)
                {
                    Toast.makeText(QuizActivity.this,"Select Answer",Toast.LENGTH_LONG).show();
                }else
                {

                       if(Util.ITEMSELECT==questionArrayList.get(step).getAnswerNr()-1)
                        {
                            Util.SCORE++;
                        }
                    step++;
                    if(step<questionArrayList.size()) {
                        Util.ITEMSELECT=-1;
                        UpdateQuize();
                    }else
                    {
                        saveData();
                        Intent intent =new Intent(QuizActivity.this, CongratsActivity.class);
                        startActivity(intent);
                    }
                }


            }
        });



    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(QuizActivity.this, HomePage.class));

    }
    public  void UpdateQuize()
    {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer= new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                tv_quiz_time.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                step++;
                if(step<questionArrayList.size()) {
                    Util.ITEMSELECT=-1;
                    UpdateQuize();
                }else
                {
                    saveData();
                    Intent intent =new Intent(QuizActivity.this,CongratsActivity.class);
                    startActivity(intent);
                }
            }
        };
        countDownTimer.start();

        QuizModelAdapter adapter=new QuizModelAdapter(questionArrayList.get(step).getOptions(),QuizActivity.this);
        recyclerview_quiz_option.setAdapter(adapter);
        Glide.with(getApplicationContext()).load(questionArrayList.get(step).getImage()).placeholder(R.drawable.placeholder_quize).into(img_quize);
        tv_step.setText((step+1)+"/"+questionArrayList.size());
        tv_question_name.setText(questionArrayList.get(step).getQuestion()+" ?");

    }
    public  void getQuizeData()
    {
        questionArrayList=new ArrayList<>();
        DatabaseReference mdatabaseref = FirebaseDatabase.getInstance().getReference("quizs");
        mdatabaseref.addValueEventListener(new ValueEventListener() {
            String quize_qustion=null;
            String quize_req1=null;
            String quize_req2=null;
            String quize_req3=null;
            String quize_req4=null;
            String quize_true=null;
            String quize_image=null;


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot userInfo : postSnapshot.getChildren()) {
                        String key = userInfo.getKey();

                        switch (key) {
                            case "question":
                                quize_qustion = userInfo.getValue().toString();
                                break;
                            case "answer_1":
                                quize_req1 = userInfo.getValue().toString();
                                break;
                            case "answer_2":
                                quize_req2 = userInfo.getValue().toString();
                                break;
                            case "answer_3":
                                quize_req3 = userInfo.getValue().toString();
                                break;
                            case "answer_4":
                                quize_req4 = userInfo.getValue().toString();
                                break;
                            case "answer_true":
                                quize_true = userInfo.getValue().toString();
                                break;
                            case "image":
                                quize_image = userInfo.getValue().toString();
                                break;
                        }
                    }
                    String quize_allQestion[]={quize_req1,quize_req2,quize_req3,quize_req4};
                    int true_number=Integer.valueOf(quize_true);
                    Question question=new Question(quize_qustion,quize_allQestion,true_number,quize_image);
                    Log.e("info",question.getQuestion());
                    questionArrayList.add(question);
                }
                UpdateQuize();
                progress_relative.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
    public  void saveData()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> sendhashMap = new HashMap<>();
        String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        sendhashMap.put("history_date", date);
        sendhashMap.put("history_score", Util.SCORE);
        sendhashMap.put("history_quize", questionArrayList.size());

        databaseReference.child("history").child(userID).push().setValue(sendhashMap);
    }
}
