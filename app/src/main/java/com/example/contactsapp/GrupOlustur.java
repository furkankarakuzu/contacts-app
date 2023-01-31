package com.example.contactsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.contactsapp.Adapter.GroupAdapter;
import com.example.contactsapp.Adapter.MessageAdapter;
import com.example.contactsapp.Models.GroupModels;
import com.example.contactsapp.Models.MesajData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class GrupOlustur extends Fragment {
    Button BSelectImage, CreateGroup;
    ImageView IVPreviewImage;
    Uri imageUri;
    String Link;
    TextInputEditText GroupName,GroupDesc;

    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    RecyclerView recyclerView;
    ArrayList<GroupModels> groupModelsArrayList;
    GroupAdapter groupAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grup_olustur, container, false);

        BSelectImage = rootView.findViewById(R.id.selectimage);
        CreateGroup = rootView.findViewById(R.id.creategroup);
        IVPreviewImage = rootView.findViewById(R.id.previmage);
        GroupName = rootView.findViewById(R.id.grupadi);
        GroupDesc = rootView.findViewById(R.id.grupaciklamasi);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = rootView.findViewById(R.id.gruplar);
        LinearLayoutManager lls = new LinearLayoutManager(getContext());
        lls.setOrientation(LinearLayoutManager.VERTICAL);
        lls.setReverseLayout(false);
        recyclerView.setLayoutManager(lls);
        groupModelsArrayList = new ArrayList<>();
        groupAdapter = new GroupAdapter(groupModelsArrayList,getContext());
        recyclerView.setAdapter(groupAdapter);

        BSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        CreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GroupName.getText().toString().isEmpty() || GroupDesc.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Lütfen tüm alanları doldurunuz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(imageUri == null) {
                    Toast.makeText(getActivity(), "Lütfen bir resim seçiniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                uploadImage();
            }
        });
        LoadGroups();
        return rootView;
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    private void uploadImage(){
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Lütfen bekleyiniz...");
        progressDialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss", Locale.ROOT);
        Date now = new Date();
        String fileName = formatter.format(now);

        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        IVPreviewImage.setImageURI(null);
                        Toast.makeText(getActivity(), "Resim yüklendi.", Toast.LENGTH_SHORT).show();
                        if(progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Resim yüklenemedi.", Toast.LENGTH_SHORT).show();
                        if(progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            String userid = firebaseUser.getUid();

                            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful()){

                                        String groupid = UUID.randomUUID().toString();

                                        GroupModels groupModels = new GroupModels(userid,GroupName.getText().toString(),GroupDesc.getText().toString(),task.getResult().toString(),groupid);

                                        firebaseFirestore.collection("group")
                                                .add(groupModels)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(getActivity(), "Grup oluşturuldu.", Toast.LENGTH_SHORT).show();
                                                        GroupName.setText("");
                                                        GroupDesc.setText("");
                                                        groupModelsArrayList.clear();
                                                        LoadGroups();
                                                    }
                                                });

                                    }
                                }
                            });

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            IVPreviewImage.setImageURI(imageUri);
        }
    }
}