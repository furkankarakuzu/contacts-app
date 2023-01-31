package com.example.contactsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsapp.Interface.GroupAddMemberListener;
import com.example.contactsapp.Models.GroupModels;
import com.example.contactsapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroupAddMember extends RecyclerView.Adapter<GroupAddMember.MyViewHolder> {
    private ArrayList<GroupModels> groupModelsArrayList;
    private Context context;
    private GroupAddMemberListener listener;

    public GroupAddMember(ArrayList<GroupModels> groupModelsArrayList, Context context, GroupAddMemberListener listener){
        this.groupModelsArrayList = groupModelsArrayList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.groupaddmemberitem,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(groupModelsArrayList.get(position).getLink()).into(holder.groupImage);
        holder.groupname.setText(groupModelsArrayList.get(position).getGroupname());
        holder.groupdesc.setText(groupModelsArrayList.get(position).getGroupdesc());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(groupModelsArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupModelsArrayList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView groupImage;
        TextView groupname,groupdesc;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            groupImage = itemView.findViewById(R.id.groupaddmemberimage);
            groupname = itemView.findViewById(R.id.groupaddmembername);
            groupdesc = itemView.findViewById(R.id.groupaddmemberdesc);
            cardView = itemView.findViewById(R.id.groupaddmembercardview);
        }
    }
}
