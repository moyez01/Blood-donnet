package com.example.moyezasus.blooddonor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDonor extends AppCompatActivity implements View.OnClickListener{

    EditText editTextName, editTextPhone, editTextEmail, editTextPassword,  editTextAge, editTextAddress;
    Spinner spinnerBloodGenres, spinnerVarsityGenres;
    Button buttonAddDoner;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    String profileImage, email, password, name, phone, age, address, bloodGenres, varsityGenres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donor);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        buttonAddDoner = (Button) findViewById(R.id.buttonAddDoner);
        spinnerBloodGenres = (Spinner) findViewById(R.id.spinnerBloodGenres);
        spinnerVarsityGenres = (Spinner) findViewById(R.id.spinnerVarsityGenres);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        buttonAddDoner.setOnClickListener(this);
    }

    private void registerUser() {
        profileImage ="";
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();

        name = editTextName.getText().toString();
        phone = editTextPhone.getText().toString();
        age = editTextAge.getText().toString();
        address = editTextAddress.getText().toString();
        bloodGenres = spinnerBloodGenres.getSelectedItem().toString();
        varsityGenres = spinnerVarsityGenres.getSelectedItem().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(AddDonor.this,"Input Name Field", Toast.LENGTH_SHORT);
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(AddDonor.this,"Input Name Field", Toast.LENGTH_SHORT);
        }
        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(AddDonor.this,"Input Name Field", Toast.LENGTH_SHORT);
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(AddDonor.this,"Input Name Field", Toast.LENGTH_SHORT);
        }
        else if(TextUtils.isEmpty(age)){
            Toast.makeText(AddDonor.this,"Input Name Field", Toast.LENGTH_SHORT);
        }
        else {
            progressBar.setVisibility(View.VISIBLE);

            //Upload data to the database
            String user_email = editTextEmail.getText().toString().trim();
            String user_password = editTextPassword.getText().toString().trim();

            mAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        sendEmailVerification();
                        //Toast.makeText(AddDonor.this, "Successfully Registered, Upload complete!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(AddDonor.this, MainActivity.class));
                    }
                    else{
                        Toast.makeText(AddDonor.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(mAuth.getUid());
        Info info = new Info(profileImage, name, email, phone, age, address, bloodGenres, varsityGenres);
        myRef.setValue(info);
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(AddDonor.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(AddDonor.this, MainActivity.class));
                    }else{
                        Toast.makeText(AddDonor.this, "Verification mail has'nt been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        registerUser();

    }
}
