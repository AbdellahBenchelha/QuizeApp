package com.example.quizapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    TextView tv_login;
    EditText ed_username, ed_email, ed_login, ed_password, ed_conf_password;
    Button btn_signUp;
    FirebaseAuth firebaseAuth;
    String userID;
    DatabaseReference mdatabaseref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tv_login = findViewById(R.id.tv_register_sign_in);
        ed_username = findViewById(R.id.edt_reg_name);
        ed_email = findViewById(R.id.edt_reg_email);
        ed_login = findViewById(R.id.edt_reg_login);
        ed_password = findViewById(R.id.edt_reg_password);
        ed_conf_password = findViewById(R.id.edt_reg_confim_password);
        btn_signUp = findViewById(R.id.btn_reg_signup);


        firebaseAuth = FirebaseAuth.getInstance();


        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = ed_username.getText().toString().trim();
                final String email = ed_email.getText().toString().trim();
                final String login = ed_login.getText().toString();
                final String password = ed_password.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterActivity.this, "UserName is Empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Email is Empty", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(login)) {
                    Toast.makeText(RegisterActivity.this, "LoginActivity is Empty", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Password is Empty", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(ed_conf_password.getText())) {
                    Toast.makeText(RegisterActivity.this, "Confirm Password is Empty", Toast.LENGTH_SHORT).show();
                } else if (!ed_conf_password.getText().toString().equals(ed_password.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "Confirm password err", Toast.LENGTH_SHORT).show();
                } else {

                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                            FirebaseUser fuser = firebaseAuth.getCurrentUser();
                                            userID = firebaseAuth.getCurrentUser().getUid();
                                            firebaseAuth.getCurrentUser();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(ed_username.getText().toString()).build();
                                            fuser.updateProfile(profileUpdates);
                                             Toast.makeText(RegisterActivity.this, "User Add", Toast.LENGTH_SHORT).show();
                                          startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                           /* DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                            HashMap<String, Object> sendhashMap = new HashMap<>();
                                            sendhashMap.put("user_name", username);
                                            sendhashMap.put("user_email", email);
                                            sendhashMap.put("user_login", login);
                                            sendhashMap.put("user_id", userID);

                                            databaseReference.child("users").push().setValue(sendhashMap);*/


                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkLoginExsit() {
        final boolean[] isExiste = {false};
        mdatabaseref = FirebaseDatabase.getInstance().getReference().child("users");
        mdatabaseref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    for (DataSnapshot userInfo : postSnapshot.getChildren()) {
                        String key = userInfo.getKey();
                        if (key == "user_login") {
                            if (ed_login.getText().equals(userInfo.getValue().toString())) {
                                isExiste[0] = true;
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return isExiste[0];
    }
}
