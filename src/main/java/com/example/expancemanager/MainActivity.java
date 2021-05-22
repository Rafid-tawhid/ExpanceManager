package com.example.expancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TextView mresetP,mCreateA;
    private EditText memail,mpass;
    private Button btn;
    private ProgressDialog mdialog;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login();
    }

    private void login() {

        mresetP=findViewById(R.id.fg_login);
        mCreateA=findViewById(R.id.ca_login);
        memail=findViewById(R.id.email_login);
        mpass=findViewById(R.id.pass_login);
        btn=findViewById(R.id.login_btn);
        mAuth = FirebaseAuth.getInstance();
        mdialog=new ProgressDialog(this);





        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Lemail=memail.getText().toString().trim();
                String Lpass=mpass.getText().toString().trim();
                if(TextUtils.isEmpty(Lemail))
                {
                    memail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(Lpass))
                {
                    mpass.setError("Password is required");
                    return;
                }

                else
                {
                    mdialog.setMessage("Processing");
                    mdialog.show();
                    mAuth.signInWithEmailAndPassword(Lemail,Lpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this,"Login Succesful",Toast.LENGTH_SHORT).show();
                                mdialog.cancel();
                                Intent intent=new Intent(MainActivity.this,homeActivity.class);
                                startActivity(intent );


                            } else {
                                Toast.makeText(MainActivity.this,"error",Toast.LENGTH_SHORT).show();
                                mdialog.cancel();

                            }
                        }
                    });
                }

            }
        });

        mCreateA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Reg.class);
                startActivity(intent);
            }
        });

        mresetP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }
}