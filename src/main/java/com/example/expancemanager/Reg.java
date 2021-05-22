package com.example.expancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Reg extends AppCompatActivity {
    private EditText mEmail,mPass;
    private Button reg;
    private FirebaseAuth mAuth;
    private ProgressDialog mdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        mEmail=findViewById(R.id.email_reg);
        mPass=findViewById(R.id.pass_reg);
        reg=findViewById(R.id.btn_reg);
        mAuth = FirebaseAuth.getInstance();
        mdialog=new ProgressDialog(this);



        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmail.getText().toString().trim();
                String pass=mPass.getText().toString().trim();

                if(email.isEmpty())
                {
                    mEmail.setError("Email is requiredxsxsx");
                    mEmail.requestFocus();


                }
                if(pass.isEmpty())
                {
                    mPass.setError("Password is required");
                    mEmail.requestFocus();


                }

                else
                {
                    mdialog.setMessage("Processing");
                    mdialog.show();
                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Reg.this,"Registration Succesful",Toast.LENGTH_SHORT).show();
                                mdialog.cancel();

                            } else {
                                Toast.makeText(Reg.this,"error",Toast.LENGTH_SHORT).show();
                                mdialog.cancel();

                            }
                        }
                    });
                }
            }
        });

    }


}