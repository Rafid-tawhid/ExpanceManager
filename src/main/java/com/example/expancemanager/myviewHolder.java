package com.example.expancemanager;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myviewHolder extends RecyclerView.ViewHolder {
    TextView amount,type,date,note;
    View mView;
    public myviewHolder(@NonNull View itemView) {

        super(itemView);
        mView=itemView;

        amount=itemView.findViewById(R.id.amount_txt_expenses);
        type=itemView.findViewById(R.id.type_txt_expenses);
        date=itemView.findViewById(R.id.date_txt_expenses);
        note=itemView.findViewById(R.id.note_txt_expenses);
    }

}
