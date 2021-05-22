package com.example.expancemanager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expancemanager.Model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

/**

 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    private FloatingActionButton fav_main_btn;
    private FloatingActionButton fav_income_btn;
    private FloatingActionButton fav_expenses_btn;
    private TextView fav_income_txt;
    private TextView fav_expenses_txt;
    private boolean isopen=false;
    private Animation fabOpen,fabClose;

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpensesDatabase;
    private TextView totalIncomeResult;
    private TextView totalExpensesResult;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View myview= inflater.inflate(R.layout.fragment_dashboard, container, false);

       fav_main_btn=myview.findViewById(R.id.fb_main_plus_btn);
       fav_income_btn=myview.findViewById(R.id.income_ft_btn);
       fav_expenses_btn=myview.findViewById(R.id.expenses_ft_btn);
       fav_income_txt=myview.findViewById(R.id.income_ft_txt);
       fav_expenses_txt=myview.findViewById(R.id.expenses_ft_txt);

       mAuth=FirebaseAuth.getInstance();
       FirebaseUser mUser=mAuth.getCurrentUser();
       String uid=mUser.getUid();
       mIncomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeDatabase").child(uid);
       mExpensesDatabase= FirebaseDatabase.getInstance().getReference().child("ExpensesDatabase").child(uid);


       totalIncomeResult=myview.findViewById(R.id.income_set_result);




       fabOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
       fabClose= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);

       fav_main_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               addData();
               if (isopen)
               {
                   fav_income_btn.startAnimation(fabClose);
                   fav_expenses_btn.startAnimation(fabClose);
                   fav_income_btn.setClickable(false);
                   fav_expenses_btn.setClickable(false);

                   fav_income_txt.startAnimation(fabClose);
                   fav_expenses_txt.startAnimation(fabClose);
                   fav_income_txt.setClickable(false);
                   fav_expenses_txt.setClickable(false);
                   isopen=false;
               }
               else
               {
                   fav_income_btn.startAnimation(fabOpen);
                   fav_expenses_btn.startAnimation(fabOpen);
                   fav_income_btn.setClickable(true);
                   fav_expenses_btn.setClickable(true);

                   fav_income_txt.startAnimation(fabOpen);
                   fav_expenses_txt.startAnimation(fabOpen);
                   fav_income_txt.setClickable(true);
                   fav_expenses_txt.setClickable(true);
                   isopen=true;
               }

           }
       });


       //calculate total income

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalsum=0;
                for (DataSnapshot mysnap:snapshot.getChildren())
                {
                    Data data=mysnap.getValue(Data.class);

                    totalsum+=data.getAmount();
                    String stresult=String.valueOf(totalsum);
                    totalIncomeResult.setText(stresult);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       return myview;
    }
    private void addData()
    {
        fav_income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            incomeDataInsert();
            }
        });
        fav_expenses_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expensesDataInsert();

            }
        });

    }

    public void incomeDataInsert()
    {
        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myview);
        final AlertDialog dialog=mydialog.create();

        dialog.setCancelable(false);


        final EditText edtAmount=myview.findViewById(R.id.amount_edt);
        final EditText edtType=myview.findViewById(R.id.type_edt);
        final EditText edtNote=myview.findViewById(R.id.note_edt);

        Button btnSave=myview.findViewById(R.id.btnSave);
        Button btnCancel=myview.findViewById(R.id.btnCancel);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type=edtType.getText().toString().trim();
                String amount=edtAmount.getText().toString().trim();
                String note=edtNote.getText().toString().trim();


                if (TextUtils.isEmpty(amount))
                {
                    edtAmount.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(type))
                {
                    edtType.setError("Required Field..");
                    return;
                }

                int ouramountint=Integer.parseInt(amount);

                if (TextUtils.isEmpty(note))
                {
                    edtNote.setError("Required Field..");
                    return;
                }

                String id=mIncomeDatabase.push().getKey();
                String mDate= DateFormat.getDateInstance().format(new Date());
                Data data=new Data(ouramountint,type,note,id,mDate);
                mIncomeDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Data saved",Toast.LENGTH_SHORT).show();
;               dialog.dismiss();
                flotingAnimationClose();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flotingAnimationClose();
                dialog.dismiss();
            }
        });
        dialog.show();

    }


    public void expensesDataInsert()
    {
        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.custom_layout_for_insertdata,null);
        mydialog.setView(myview);
        final AlertDialog dialog=mydialog.create();

        dialog.setCancelable(false);


        final EditText edtAmount=myview.findViewById(R.id.amount_edt);
        final EditText edtType=myview.findViewById(R.id.type_edt);
        final EditText edtNote=myview.findViewById(R.id.note_edt);

        Button btnSave=myview.findViewById(R.id.btnSave);
        Button btnCancel=myview.findViewById(R.id.btnCancel);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type=edtType.getText().toString().trim();
                String amount=edtAmount.getText().toString().trim();
                String note=edtNote.getText().toString().trim();


                if (TextUtils.isEmpty(amount))
                {
                    edtAmount.setError("Required Field..");
                    return;
                }
                if (TextUtils.isEmpty(type))
                {
                    edtType.setError("Required Field..");
                    return;
                }

                int ouramountint=Integer.parseInt(amount);

                if (TextUtils.isEmpty(note))
                {
                    edtNote.setError("Required Field..");
                    return;
                }

                String id=mExpensesDatabase.push().getKey();
                String mDate= DateFormat.getDateInstance().format(new Date());
                Data data=new Data(ouramountint,type,note,id,mDate);
                mExpensesDatabase.child(id).setValue(data);
                Toast.makeText(getActivity(),"Data saved",Toast.LENGTH_SHORT).show();
                 dialog.dismiss();
                flotingAnimationClose();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flotingAnimationClose();
                dialog.dismiss();
            }
        });
        dialog.show();






    }

    public void flotingAnimationClose()
    {
        if (isopen)
        {
            fav_income_btn.startAnimation(fabClose);
            fav_expenses_btn.startAnimation(fabClose);
            fav_income_btn.setClickable(false);
            fav_expenses_btn.setClickable(false);

            fav_income_txt.startAnimation(fabClose);
            fav_expenses_txt.startAnimation(fabClose);
            fav_income_txt.setClickable(false);
            fav_expenses_txt.setClickable(false);
            isopen=false;
        }
        else
        {
            fav_income_btn.startAnimation(fabOpen);
            fav_expenses_btn.startAnimation(fabOpen);
            fav_income_btn.setClickable(true);
            fav_expenses_btn.setClickable(true);

            fav_income_txt.startAnimation(fabOpen);
            fav_expenses_txt.startAnimation(fabOpen);
            fav_income_txt.setClickable(true);
            fav_expenses_txt.setClickable(true);
            isopen=true;
        }
    }
}