package com.example.firebasedemo.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.firebasedemo.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

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
    private FirebaseDatabase mRootRef;
    private FirebaseAuth mAuth;
    private FirebaseStorage mStorage;
    private TextInputEditText userName,name,email;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_SELECT = 2;
    private ImageView profileImage;
    private Uri imageUri;

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

        updateBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
        captureBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update:
                break;
            case R.id.upload:

                break;
            case R.id.capture:

                break;
        }
    }
}