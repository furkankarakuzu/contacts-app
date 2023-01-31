package com.example.contactsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsapp.Models.GroupModels;
import com.example.contactsapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder>{

    private ArrayList<GroupModels> groupModelsArrayList;
    private Context context;

    public GroupAdapter(ArrayList<GroupModels> groupModelsArrayList, Context context) {
        this.groupModelsArrayList = groupModelsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.groupitem,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.MyViewHolder holder, int position) {
        Picasso.get().load(groupModelsArrayList.get(position).getLink()).into(holder.groupImage);
        holder.groupname.setText(groupModelsArrayList.get(position).getGroupname());
        holder.groupdesc.setText(groupModelsArrayList.get(position).getGroupdesc());
    }

    @Override
    public int getItemCount() {
        return groupModelsArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView groupImage;
        TextView groupname,groupdesc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            groupImage = itemView.findViewById(R.id.groupimage);
            groupname = itemView.findViewById(R.id.groupname);
            groupdesc = itemView.findViewById(R.id.groupdesc);
        }
    }
}
