package com.example.contactsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.contactsapp.Adapter.MessageAdapter;
import com.example.contactsapp.Models.MesajData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MesajGonder extends Fragment {

    Button createMessage;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    TextInputEditText messageName, messageDesc;
    FirebaseFirestore firebaseFirestore;

    RecyclerView recyclerView;
    ArrayList<MesajData> messageDataArrayList;

    MessageAdapter messageAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mesaj_olustur, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        createMessage = rootView.findViewById(R.id.mesajolustur);
        messageName = rootView.findViewById(R.id.mesajadi);
        messageDesc = rootView.findViewById(R.id.mesaj);

        recyclerView = rootView.findViewById(R.id.mesajlar);
        LinearLayoutManager lls = new LinearLayoutManager(getContext());
        lls.setOrientation(LinearLayoutManager.VERTICAL);
        lls.setReverseLayout(false);
        recyclerView.setLayoutManager(lls);
        messageDataArrayList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageDataArrayList,getContext());
        recyclerView.setAdapter(messageAdapter);

        LoadMessages();
        createMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(messageName.getText().toString()) || TextUtils.isEmpty(messageDesc.getText().toString())) {
                    Toast.makeText(getActivity(),"Mesaj adı veya mesaj içeriği boş bırakalamaz", Toast.LENGTH_LONG).show();
                    return;
                }

                String userid = firebaseUser.getUid();

                MesajData mesajData = new MesajData(userid,messageName.getText().toString(), messageDesc.getText().toString());

                firebaseFirestore.collection("message").add(mesajData).addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(),"Mesaj başarıyla gönderildi", Toast.LENGTH_LONG).show();
                    messageDataArrayList.clear();
                    LoadMessages();
                });
            }
        });

        return rootView;
    }

    private void LoadMessages(){
        firebaseFirestore.collection("message")
                .whereEqualTo("userid",String.valueOf(firebaseUser.getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            messageDataArrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                MesajData mesajData = document.toObject(MesajData.class);
                                messageDataArrayList.add(mesajData);
                            }
                            messageAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}