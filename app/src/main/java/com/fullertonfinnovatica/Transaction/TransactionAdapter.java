package com.fullertonfinnovatica.Transaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fullertonfinnovatica.Accounts.JournalEntryModel;
import com.fullertonfinnovatica.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.RecyclerViewHolder>{

    List<JournalEntryModel> list;
    Context context;
    String narration;

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
        String typeOfTransaction = modelList.getFrom().toLowerCase().trim();
        narration = modelList.getNarration().get(0);

        recyclerViewHolder.date.setText(modelList.getDate().substring(4));
        recyclerViewHolder.credit.setText("₹ "+modelList.getCredit());

        String subTypeOfTransaction = modelList.getNarration().get(0).substring(modelList.getNarration().get(0).indexOf(":")+1);

        if(modelList.getTo().toLowerCase().equals("cash"))
            recyclerViewHolder.modeOfTransaction.setImageDrawable(context.getResources().getDrawable(R.drawable.funds));
        else if(modelList.getTo().toLowerCase().equals("credit"))
            recyclerViewHolder.modeOfTransaction.setImageDrawable(context.getResources().getDrawable(R.drawable.credit));
        else
            recyclerViewHolder.modeOfTransaction.setImageDrawable(context.getResources().getDrawable(R.drawable.cheque));

        if(typeOfTransaction.contains("purchase") || typeOfTransaction.contains("sale return") || typeOfTransaction.contains("payment done") || (typeOfTransaction.contains("commission") && subTypeOfTransaction.contains("Given" )) )
            recyclerViewHolder.credit.setTextColor(context.getResources().getColor(R.color.red_orignal));
        else
            recyclerViewHolder.credit.setTextColor(context.getResources().getColor(R.color.green_orignal));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView date, credit;
        ImageView modeOfTransaction;

        public RecyclerViewHolder(View view) {
            super(view);

            date = view.findViewById(R.id.date);
            credit = view.findViewById(R.id.credit);
            modeOfTransaction = view.findViewById(R.id.mmode);

        }
    }

    public AdapterView.OnItemClickListener messageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id)
        {
            // Display a messagebox.
            Toast.makeText(context,narration,Toast.LENGTH_SHORT).show();
        }
    };



}
