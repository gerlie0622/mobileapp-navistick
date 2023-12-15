package com.example.navistick;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Admin extends AppCompatActivity {
    Button createaccountbtn;
    Button Scheduler;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    FirebaseFirestore db;
    ProgressDialog progressDialog;



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.resetUserPassword){
            startActivity(new Intent(getApplicationContext(),Resetpassword.class));
        }
        if (item.getItemId() == R.id.logoutbtn){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}