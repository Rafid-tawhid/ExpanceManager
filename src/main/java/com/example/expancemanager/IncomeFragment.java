package com.example.expancemanager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expancemanager.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class IncomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mIncomeDatabase;
    private RecyclerView recyclerView;
    private TextView incomeTotalSum;
    private EditText edtAmount,edtNote,edtType;
    private Button BtnSave,BtnDelete;
    private String type,note;
    private int amount;
    private String post_key;





    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View myview= inflater.inflate(R.layout.fragment_income, container, false);

      mAuth=FirebaseAuth.getInstance();
        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();


        mIncomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeDatabase").child(uid);
        recyclerView=myview.findViewById(R.id.recycler_income_id);
       LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
      linearLayoutManager.setReverseLayout(true);
      linearLayoutManager.setStackFromEnd(true);
      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(linearLayoutManager);

      incomeTotalSum=myview.findViewById(R.id.income_txt_result);








      mIncomeDatabase.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
        int totalvalue=0;
              for (DataSnapshot mysnapshot:snapshot.getChildren())
              {
                  Data data=mysnapshot.getValue(Data.class);
                totalvalue+=data.getAmount();

                String stTotal=String.valueOf(totalvalue);

                incomeTotalSum.setText(stTotal);

              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });

      return myview;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options=new FirebaseRecyclerOptions.Builder<Data>().setQuery(mIncomeDatabase,Data.class).build();


        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter=new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyViewHolder holder, final int position, @NonNull final Data model) {

                holder.setAmount(model.getAmount());
                holder.setDate(model.getDate());
                holder.setNote(model.getNote());
                holder.setType(model.getType());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        post_key=getRef(position).getKey();

                        type=model.getType();
                        note=model.getNote();
                        amount=model.getAmount();
                        UpdateDataItem();


                    }
                });


            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View myview=LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data,parent,false);
           return new MyViewHolder(myview);
            }
        };
        adapter.startListening();
       recyclerView.setAdapter(adapter);


    }

    public void UpdateDataItem(){


        AlertDialog.Builder mydialoge=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.update_data_item,null);
        mydialoge.setView(myview);
        final AlertDialog dialog=mydialoge.create();

        dialog.show();


        edtAmount=myview.findViewById(R.id.amount_edt);
        edtNote=myview.findViewById(R.id.note_edt);
        edtType=myview.findViewById(R.id.type_edt);
        BtnSave=myview.findViewById(R.id.btnUpdate);
        BtnDelete=myview.findViewById(R.id.btnDelete);

        edtNote.setText(note);
        edtNote.setSelection(note.length());
        edtType.setText(type);
        edtType.setSelection(type.length());
        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection(String.valueOf(amount).length());



        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type=edtType.getText().toString().trim();
                note=edtNote.getText().toString().trim();
                String mdamount=String.valueOf(amount);
                mdamount=edtAmount.getText().toString().trim();
                int myamount=Integer.parseInt(mdamount);
                String mDate= DateFormat.getDateInstance().format(new Date());
                Data data=new Data(myamount,type,note,post_key,mDate);
                mIncomeDatabase.child(post_key).setValue(data);
                dialog.dismiss();
                Toast.makeText(getActivity(),"Data Updated",Toast.LENGTH_SHORT).show();


            }
        });

        BtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                type="None";
                note="Nothing";
                String mdamount="null";
                mdamount=edtAmount.getText().toString().trim();
                int myamount=0000;
                String mDate= DateFormat.getDateInstance().format(new Date());
                Data data=new Data(myamount,type,note,post_key,mDate);
                mIncomeDatabase.child(post_key).setValue(data);

            dialog.dismiss();
            }
        });



    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        void setType(String type)
        {
            TextView  mType=mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }
        void setNote(String note)
        {
            TextView  mNote=mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }
        void setDate(String date)
        {
            TextView  mDate=mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }

        void setAmount(int amount)
        {
            TextView  mAmount=mView.findViewById(R.id.amount_txt_income);
            String stamount=String.valueOf(amount);
            mAmount.setText(stamount);
        }
    }
}