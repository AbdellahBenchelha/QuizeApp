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

public class LoginActivity extends AppCompatActivity {
    TextView txtSignUp;
    EditText edLogin, edPassword;
    Button btn_Login;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtSignUp = findViewById(R.id.tv_login_signup);
        edLogin = findViewById(R.id.edt_user_login);
        edPassword = findViewById(R.id.edt_user_password);
        btn_Login = findViewById(R.id.btn_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edLogin.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "LoginActivity is Empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Password is Empty", Toast.LENGTH_SHORT).show();

                } else {
                    /*DbContext dbContext=new DbContext(LoginActivity.this);
                    if(!dbContext.IsUserExiste(edLogin.getText().toString(),edPassword.getText().toString()))
                    {
                        Toast.makeText(LoginActivity.this,"LoginActivity or password incorrect",Toast.LENGTH_LONG).show();

                    }else
                    {
                        Intent intent =new Intent(LoginActivity.this,QuizActivity.class);
                        startActivity(intent);
                    }*/
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                               // importUserData(firebaseAuth.getCurrentUser().getEmail());
                              Intent intent = new Intent(LoginActivity.this, HomePage.class);
                              startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }
        });


        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


}
