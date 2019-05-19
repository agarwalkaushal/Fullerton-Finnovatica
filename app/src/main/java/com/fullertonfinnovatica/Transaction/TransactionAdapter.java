package com.fullertonfinnovatica.Transaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fullertonfinnovatica.Accounts.JournalEntryModel;
import com.fullertonfinnovatica.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.RecyclerViewHolder>{

    List<JournalEntryModel> list;
    Context context;

    public TransactionAdapter(List<JournalEntryModel> arrayList2, Context context)
    {

        this.list=arrayList2;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_journal_retrieve, viewGroup, false);
        TransactionAdapter.RecyclerViewHolder recyclerViewHolder = new TransactionAdapter.RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.RecyclerViewHolder recyclerViewHolder, int i) {

        JournalEntryModel modelList = list.get(i);

        recyclerViewHolder.date.setText(modelList.getDate().substring(4));
        recyclerViewHolder.credit.setText("₹ "+modelList.getCredit());

        Log.e("From: ",modelList.getFrom());

        // TODO: If cash keep cash icon or for cheque/credit make text

        if(modelList.getNarration().get(0).contains("purchase")){
            recyclerViewHolder.credit.setTextColor(context.getResources().getColor(R.color.red_orignal));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView date, credit;

        public RecyclerViewHolder(View view) {
            super(view);

            date = view.findViewById(R.id.date);
            credit = view.findViewById(R.id.credit);

        }
    }


}
