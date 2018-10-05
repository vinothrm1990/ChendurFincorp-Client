package com.app.chendurfincorp.client.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.chendurfincorp.client.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfitAdapter extends RecyclerView.Adapter<ProfitAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<HashMap<String,String>> profitList;

    public ProfitAdapter(Context mContext, ArrayList<HashMap<String, String>> profitList) {
        this.mContext = mContext;
        this.profitList = profitList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profit_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        HashMap<String,String> itemmap = profitList.get(position);

        holder.tvDate.setText(itemmap.get("date"));
        holder.tvAmount.setText("₹"+itemmap.get("amount"));
    }

    @Override
    public int getItemCount() {
        return profitList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvAmount;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.total_date_tv);
            tvAmount = itemView.findViewById(R.id.total_amount_tv);
        }
    }
}
