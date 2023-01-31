package com.example.contactsapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsapp.Interface.ContactsListener;
import com.example.contactsapp.Models.Contacts;
import com.example.contactsapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    Context context;
    List<Contacts> contactsList;
    private ContactsListener listener;

    public ContactsAdapter(){}

    public ContactsAdapter(Context context, List<Contacts> contactsList,ContactsListener listener) {
        this.context = context;
        this.contactsList = contactsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contactsitem,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Contacts contacts = contactsList.get(position);
        holder.contactsName.setText(contacts.getName());
        holder.contactsNumber.setText(contacts.getPhone());
        if(contacts.getPhone() != null){
            Picasso.get().load(contacts.getPhoto()).into(holder.contactsImage);
        }else{
            holder.contactsImage.setImageResource(R.mipmap.ic_launcher_round);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(contactsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView contactsImage;
        TextView contactsName,contactsNumber;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            contactsImage = itemView.findViewById(R.id.contancsimage);
            contactsName = itemView.findViewById(R.id.contactsname);
            contactsNumber = itemView.findViewById(R.id.contactsphone);
            cardView = itemView.findViewById(R.id.contactsitem);
        }
    }
}
