package com.example.contactsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsapp.Models.MesajData;
import com.example.contactsapp.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private ArrayList<MesajData> messageDataArrayList;
    private Context context;

    public MessageAdapter(ArrayList<MesajData> messsageDataArrayList, Context context) {
        this.messageDataArrayList = messsageDataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mesajitem,parent,false));
    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        holder.messagetitle.setText(messageDataArrayList.get(position).getMessagename());
        holder.messagedesc.setText(messageDataArrayList.get(position).getMessagedesc());
    }

    @Override
    public int getItemCount() {
        return messageDataArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView messagetitle,messagedesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            messagetitle = itemView.findViewById(R.id.messagetitle);
            messagedesc = itemView.findViewById(R.id.messagedesc);
        }
    }
}
