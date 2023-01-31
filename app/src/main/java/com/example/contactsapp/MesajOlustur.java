package com.example.contactsapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contactsapp.Adapter.HomeGroupAdapter;
import com.example.contactsapp.Adapter.HomeMessageAdapter;
import com.example.contactsapp.Interface.GroupAddMemberListener;
import com.example.contactsapp.Interface.MessageListener;
import com.example.contactsapp.Models.ContactsToGroup;
import com.example.contactsapp.Models.GroupModels;
import com.example.contactsapp.Models.MesajData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MesajOlustur extends Fragment implements GroupAddMemberListener, MessageListener {

    RecyclerView recyclerView,recyclerViewTwo;
    Button SendMessageAuto;
    TextView selectedGroup,selectedMessage;
    ArrayList<GroupModels> groupModelsArrayList;
    ArrayList<MesajData> messageDataArrayList;
    HomeMessageAdapter messageHomeAdapter;
    HomeGroupAdapter groupAdapter;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    String SeciliGrupid=null,SeciliGrupAdi=null;
    String SecilenMesaj=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mesaj_gonder, container, false);
        selectedGroup = rootView.findViewById(R.id.selectedgroupname);
        selectedMessage = rootView.findViewById(R.id.selectedmessage);
        SendMessageAuto = rootView.findViewById(R.id.mesajgonder);
        SendMessageAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                    sendMessage();
                }else{
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.SEND_SMS},100);
                }
            }
        });

        recyclerView = rootView.findViewById(R.id.guruplistesi);
        recyclerViewTwo = rootView.findViewById(R.id.mesajlar);

        LoadGroups();
        LoadMessages();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(recyclerView.HORIZONTAL);
        llm.setReverseLayout(false);
        recyclerView.setLayoutManager(llm);

        LinearLayoutManager llms = new LinearLayoutManager(getActivity());
        llms.setOrientation(recyclerViewTwo.HORIZONTAL);
        llms.setReverseLayout(false);
        recyclerViewTwo.setLayoutManager(llms);

        groupModelsArrayList = new ArrayList<>();
        messageDataArrayList = new ArrayList<>();

        groupAdapter = new HomeGroupAdapter(groupModelsArrayList,getContext(),this::onItemClicked);
        messageHomeAdapter = new HomeMessageAdapter(messageDataArrayList,getActivity(),this::onItemClicked);

        recyclerView.setAdapter(groupAdapter);
        recyclerViewTwo.setAdapter(messageHomeAdapter);

        return rootView;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            sendMessage();
        }
        else{
            Toast.makeText(getContext(), "İzin Verilmedi", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendMessage(){
        if(SecilenMesaj!=null && SeciliGrupid!=null) {
            SmsManager smsManager = SmsManager.getDefault();
            firebaseFirestore.collection("contactsToGroup")
                    .whereEqualTo("groupid",String.valueOf(SeciliGrupid))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isComplete()){
                                for(QueryDocumentSnapshot doc:task.getResult()){
                                    ContactsToGroup contactsToGroup = doc.toObject(ContactsToGroup.class);
                                    String number = contactsToGroup.getContactsPhone();
                                    smsManager.sendTextMessage(number,null,SecilenMesaj,null,null);
                                }
                                Toast.makeText(getContext(), "Mesaj Başarıyla Gönderildi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(getContext(), "Lütfen mesajı ve grubu seçiniz", Toast.LENGTH_SHORT).show();
        }
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
                            messageHomeAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    private void LoadGroups(){
        firebaseFirestore.collection("group")
                .whereEqualTo("userid",String.valueOf(firebaseUser.getUid()))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            groupModelsArrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                GroupModels groupModels = document.toObject(GroupModels.class);
                                groupModelsArrayList.add(groupModels);
                            }
                            groupAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    @Override
    public void onItemClicked(GroupModels groupModels) {
        SeciliGrupid=groupModels.getGroupid();
        SeciliGrupAdi=groupModels.getGroupname();
        Toast.makeText(getContext(), "Seçilen Grubun ID: "+SeciliGrupid, Toast.LENGTH_SHORT).show();
        selectedGroup.setText("Seçilen Grup : "+String.valueOf(groupModels.getGroupname()));
    }

    @Override
    public void onItemClicked(MesajData messageData) {
        SecilenMesaj = messageData.getMessagedesc();
        Toast.makeText(getContext(), "Seçilen Mesaj "+SecilenMesaj, Toast.LENGTH_SHORT).show();
        selectedMessage.setText("Seçilen Mesaj : "+String.valueOf(messageData.getMessagename()));
    }
}