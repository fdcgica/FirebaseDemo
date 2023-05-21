package com.example.firebasedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private TextView headerName, headerEmail;

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

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                        Toast.makeText(MainActivity.this, "Home Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.profile:
                    {
                        Toast.makeText(MainActivity.this, "Profile Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.settings:
                    {
                        Toast.makeText(MainActivity.this, "Settings Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.logout:
                    {
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(MainActivity.this,"You have signed out!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, StartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                        break;
                    }
                    case R.id.facebook:
                    {
                        Toast.makeText(MainActivity.this, "Facebook Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.instagram:
                    {
                        Toast.makeText(MainActivity.this, "Instagram Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.linkedin:
                    {
                        Toast.makeText(MainActivity.this, "Linkedin Selected", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                return false;
            }
        });

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
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                }).create().show();
        }
    }


}