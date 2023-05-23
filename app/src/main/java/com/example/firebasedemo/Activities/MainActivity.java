package com.example.firebasedemo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedemo.Fragments.ForecastsFragment;
import com.example.firebasedemo.Fragments.HomeFragment;
import com.example.firebasedemo.Fragments.UserProfileFragment;
import com.example.firebasedemo.Fragments.UserSettingsFragment;
import com.example.firebasedemo.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private FrameLayout frameContainer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private TextView headerName, headerEmail;
    private ProgressDialog progressDialog;
    private Context context;
    private UserProfileFragment userProfileFragment;
    private UserSettingsFragment userSettingsFragment;
    private HomeFragment homeFragment;
    private ForecastsFragment forecastsFragment;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Context
        context = getApplicationContext();

        //Progress Dialog
        progressDialog = new ProgressDialog(this);
        //Frame Container
        frameContainer = findViewById(R.id.frame_container);
        //Fragments
        userProfileFragment = new UserProfileFragment();
        userSettingsFragment = new UserSettingsFragment();
        homeFragment = new HomeFragment();
        forecastsFragment = new ForecastsFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,homeFragment).commit();
        //Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        //Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        headerName = headerView.findViewById(R.id.header_name);
        headerEmail = headerView.findViewById(R.id.header_email);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.forecasts:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, forecastsFragment).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.profile:
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, userProfileFragment).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.settings:
                    {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, userSettingsFragment).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.logout:
                    {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Logout")
                                .setMessage("Are you sure you want to logout?")
                                .setNegativeButton(android.R.string.no, null)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface arg0, int arg1) {
                                        FirebaseAuth.getInstance().signOut();
                                        Toast.makeText(MainActivity.this,"You have signed out!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, StartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }
                                }).create().show();
                        break;
                    }
                    case R.id.facebook:
                    {
                        openFacebook();
                        break;
                    }
                    case R.id.instagram:
                    {
                        openInstagram();
                        break;
                    }
                    case R.id.linkedin:
                    {
                        openLinkedin();
                        break;
                    }
                }
                return false;
            }
        });
        loadUserData();
    }

    private void loadUserData(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String uid = currentUser.getUid();
        DatabaseReference usersRef  = FirebaseDatabase.getInstance().getReference().child("Users");
        DatabaseReference currentUserRef = usersRef.child(uid);

        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    String email = snapshot.child("email").getValue().toString();
                    headerName.setText(name);
                    headerEmail.setText(email);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void openFacebook(){
        String facebookUrl = "https://www.facebook.com/FortyDegreesCelsiusInc";

        try {
            // Check if the Facebook app is installed
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            // Facebook app is present, open it
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + facebookUrl));
            startActivity(intent);
        } catch (Exception e) {
            // Facebook app is not installed, open in the mobile browser
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
            startActivity(intent);
        }
    }

    private void openInstagram(){
        String instagramUrl = "https://www.instagram.com/giiiiics/";

        try {
            // Check if the Instagram app is installed
            getPackageManager().getPackageInfo("com.instagram.android", 0);
            // Instagram app is present, open it
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/" + instagramUrl));
            intent.setPackage("com.instagram.android");
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // Instagram app is not installed, open in the mobile browser
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl));
            startActivity(intent);
        }
    }

    private void openLinkedin(){
         String linkedInUrl = "https://www.linkedin.com/company/forty-degrees-celsius-inc/";

        try {
            // Check if the LinkedIn app is installed
            getPackageManager().getPackageInfo("com.linkedin.android", 0);
            // LinkedIn app is present, open it
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://profile/"));
            intent.setPackage("com.linkedin.android");
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // LinkedIn app is not installed, open in the mobile browser
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkedInUrl));
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
                    new AlertDialog.Builder(MainActivity.this)
                .setTitle("About to exit the app")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                }).create().show();
        }
    }


}