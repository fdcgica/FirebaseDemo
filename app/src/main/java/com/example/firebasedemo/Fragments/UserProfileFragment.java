package com.example.firebasedemo.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.Model.Users;
import com.example.firebasedemo.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfileFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView user_name_fragment;
    private Button uploadBtn, updateBtn, captureBtn;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    private TextInputEditText userName,name,email;
    private static final int REQUEST_IMAGE_SELECT = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageView profileImage;
    private Uri imageUri;
    private Context context;
    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        // Inside the fragment's onViewCreated() or any appropriate method
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Profile");

        //ImageView
        profileImage = view.findViewById(R.id.profile_image);
        //Button
        updateBtn = view.findViewById(R.id.update);
        uploadBtn = view.findViewById(R.id.upload);
        captureBtn = view.findViewById(R.id.capture);
        //Fields
        userName = view.findViewById(R.id.username);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        context = getContext();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String uid = currentUser.getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        DatabaseReference currentUserRef = mDatabaseRef.child(uid);

        updateBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
        captureBtn.setOnClickListener(this);

        setData(currentUserRef);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                String uid = currentUser.getUid();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
                DatabaseReference currentUserRef = mDatabaseRef.child(uid);
                updateUser(currentUserRef);
                break;
            case R.id.upload:
                openFile();
                break;
            case R.id.capture:

                break;
        }
    }
    private void setData(DatabaseReference currentUserRef){
        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String userName_txt = snapshot.child("userName").getValue().toString();
                    String nameTxt = snapshot.child("name").getValue().toString();
                    String email_txt = snapshot.child("email").getValue().toString();
                    String uri = snapshot.child("imageUrl").getValue().toString();
                    userName.setText(userName_txt);
                    name.setText(nameTxt);
                    email.setText(email_txt);
                    email.setEnabled(false);
                    email.setTextColor(ContextCompat.getColor(context, R.color.netflix_grey));

                    if(uri.equals("")){
                        profileImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        Picasso.get().load(uri).into(profileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void updateUser(DatabaseReference currentUserRef){
        String userNameTxt = userName.getText().toString();
        String nameTxt = name.getText().toString();
        String iUri = imageUri.toString();

        Users users = new Users();
        users.setUserName(userNameTxt);
        users.setName(nameTxt);
        users.setImageUrl(iUri);

        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("userName",users.getUserName());
        updatedData.put("name",users.getName());
        updatedData.put("imageUrl",users.getImageUrl());

        currentUserRef.updateChildren(updatedData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context,"Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void openFile(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQUEST_IMAGE_SELECT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();

            Picasso.get().load(imageUri).into(profileImage);
            profileImage.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Image added", Toast.LENGTH_SHORT).show();
        }
    }
}