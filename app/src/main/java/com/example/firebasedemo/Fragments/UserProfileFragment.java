package com.example.firebasedemo.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.Model.Users;
import com.example.firebasedemo.R;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class UserProfileFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int CAMERA_PERMISSION_CODE = 1;
    private String mParam1;
    private String mParam2;
    private Button uploadBtn, updateBtn, captureBtn;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference currentUserRef;
    private FirebaseAuth mAuth;
    private TextInputEditText userName, name, email, mobileNo;
    private ImageView profileImage;
    private Uri imageUri;
    private byte[] imageDataUri;
    private Context context;
    private ProgressDialog pd;
    private ActivityResultLauncher<Intent> imagePickerLauncher, cameraLauncher;


    public UserProfileFragment() {
        // Required empty public constructor
    }
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
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
        mobileNo = view.findViewById(R.id.mobile);
        email = view.findViewById(R.id.email);
        context = getContext();
        pd = new ProgressDialog(context);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String uid = currentUser.getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUserRef = mDatabaseRef.child(uid);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        pd.setMessage("Please wait");
                        pd.show();
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            imageUri = data.getData();

                            Picasso.get().load(imageUri).into(profileImage);
                            profileImage.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "Image added", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                        else {
                            pd.dismiss();
                        }
                    }
                });
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        pd.setMessage("Please wait");
                        pd.show();
                        imageUri = null;
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            if (data != null && data.getExtras() != null) {
                                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                imageDataUri = baos.toByteArray();
                                profileImage.setImageBitmap(imageBitmap);
                                profileImage.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(), "Image added", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            } else if (data != null && data.getData() != null) {
                                imageUri = data.getData();
                                Picasso.get().load(imageUri).into(profileImage);
                                profileImage.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(), "Image added", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        } else {
                            pd.dismiss();
                        }
                    }
                });

        updateBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
        captureBtn.setOnClickListener(this);

        setData(currentUserRef);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update:
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                String uid = currentUser.getUid();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
                currentUserRef = mDatabaseRef.child(uid);
                updateUser(currentUserRef);
                break;
            case R.id.upload:
                openFile();
                break;
            case R.id.capture:
                requestCameraPermission();
                break;
        }
    }

    private void updateUser(DatabaseReference currentUserRef) {

        pd.setMessage("Please Wait");
        pd.show();

        String userNameTxt = userName.getText().toString();
        String nameTxt = name.getText().toString();

        if (imageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String saveUri = uri.toString();
                                    String iUri = saveUri;
                                    Users users = new Users();
                                    users.setUserName(userNameTxt);
                                    users.setName(nameTxt);
                                    users.setImageUrl(iUri);

                                    Map<String, Object> updatedData = new HashMap<>();
                                    updatedData.put("userName", users.getUserName());
                                    updatedData.put("name", users.getName());
                                    updatedData.put("imageUrl", users.getImageUrl());

                                    currentUserRef.updateChildren(updatedData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                            setData(currentUserRef);
                                            pd.dismiss();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });

        }
        else if (imageDataUri != null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + ".jpg");
            UploadTask uploadTask = fileReference.putBytes(imageDataUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String saveUri = uri.toString();
                            String iUri = saveUri;

                            Users users = new Users();
                            users.setUserName(userNameTxt);
                            users.setName(nameTxt);
                            users.setImageUrl(iUri);

                            Map<String, Object> updatedData = new HashMap<>();
                            updatedData.put("userName", users.getUserName());
                            updatedData.put("name", users.getName());
                            updatedData.put("imageUrl", users.getImageUrl());

                            currentUserRef.updateChildren(updatedData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    setData(currentUserRef);
                                    pd.dismiss();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }
        else{

            Users users = new Users();
            users.setUserName(userNameTxt);
            users.setName(nameTxt);

            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("userName", users.getUserName());
            updatedData.put("name", users.getName());

            currentUserRef.updateChildren(updatedData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    setData(currentUserRef);
                    pd.dismiss();
                }
            });
        }
    }

    private void setData(DatabaseReference currentUserRef) {
        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName_txt = "";
                String nameTxt = "";
                String email_txt = "";
                String mobile_txt = "";
                String uri = "";
                if (snapshot.exists()) {
                    if(snapshot.child("name").getValue() != null && snapshot.child("userName").getValue() != null
                        && snapshot.child("name").getValue() != null && snapshot.child("email").getValue() != null
                    && snapshot.child("mobileNo").getValue() != null && snapshot.child("imageUrl").getValue() != null) {

                        userName_txt = snapshot.child("userName").getValue().toString();
                        nameTxt = snapshot.child("name").getValue().toString();
                        email_txt = snapshot.child("email").getValue().toString();
                        mobile_txt = snapshot.child("mobileNo").getValue().toString();
                        uri = snapshot.child("imageUrl").getValue().toString();
                        userName.setText(userName_txt);
                        name.setText(nameTxt);
                        mobileNo.setText(mobile_txt);
                        mobileNo.setEnabled(false);
                        mobileNo.setTextColor(ContextCompat.getColor(context, R.color.netflix_grey));
                        email.setText(email_txt);
                        email.setEnabled(false);
                        email.setTextColor(ContextCompat.getColor(context, R.color.netflix_grey));

                        if (uri.equals(null) || uri.equals("")) {
                            profileImage.setVisibility(View.VISIBLE);
                        } else {
                            profileImage.setVisibility(View.VISIBLE);
                            Picasso.get().load(uri).into(profileImage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void openCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}