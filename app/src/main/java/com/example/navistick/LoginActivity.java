package com.example.navistick;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn,forgetpasswordbtn, registerbtn;
    EditText username,password;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();


        username = findViewById(R.id.loginemail);
        password = findViewById(R.id.loginpassword);
        loginBtn = findViewById(R.id.loginbtn);
        forgetpasswordbtn = findViewById(R.id.forgetpassbtn);
        registerbtn = findViewById(R.id.registerbtn);

        forgetpasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetpass();
            }
        });

//        forgetpasswordbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // start alert
//                View view = inflater.inflate(R.layout.reset_pop, null);
//
//                reset_alert.setTitle("Reset Forget Password")
//                        .setMessage("Enter Your Email To Reset")
//                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                // Validate the email address
//                                EditText email = view.findViewById(R.id.resetemailpop);
//                                if (email.getText().toString().isEmpty()){
//                                    email.setError("Required Field");
//                                    return;
//                                }
//                                //send the reset Link
//                                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Toast.makeText(LoginActivity.this, "Reset Email Sent", Toast.LENGTH_SHORT).show();
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//                            }
//                        }).setNegativeButton("Cancel",null)
//                        .setView(view)
//                        .create().show();
//            }
//        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // extract

                if (username.getText().toString().isEmpty()) {
                    username.setError("Email is missing");
                    return;
                }
                if (password.getText().toString().isEmpty()) {
                    password.setError("Password is missing");
                    return;
                }
                // data is valid
                //login user
                firebaseAuth.signInWithEmailAndPassword(username.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //login is successful
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        checkUserAccessLevel(authResult.getUser().getUid());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

    }

    private void resetpass() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater =LayoutInflater.from(this);

        View view = inflater.inflate(R.layout.reset_pop, null);
        myDialog.setView(view);

        final AlertDialog dialog = myDialog.create();

        final EditText resetemailpop = view.findViewById(R.id.resetemailpop);
        final Button cancel = view.findViewById(R.id.btnCancel);
        final Button reset = view.findViewById(R.id.btnReset);

        cancel.setOnClickListener(v -> dialog.dismiss());

        reset.setOnClickListener(view1 -> {
            EditText email = view.findViewById(R.id.resetemailpop);
            if (email.getText().toString().isEmpty()){
                email.setError("Required Field");
                return;
            }

            firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(LoginActivity.this, "Reset Email Sent", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            dialog.dismiss();
        });

        dialog.show();
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        //Extract Data from document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: "+ documentSnapshot.getData());
                // identify user access level

                if (documentSnapshot.getString("isAdmin") !=null){
                    //user is admin

                    startActivity(new Intent(getApplicationContext(), Admin.class));
                    finish();
                }

                if (documentSnapshot.getString("isUser") !=null){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.getString("isAdmin")!= null){
                        startActivity(new Intent(getApplicationContext(), Admin.class));
                        finish();
                    }
                    if (documentSnapshot.getString("isUser")!= null){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }
            });
        }
    }
}