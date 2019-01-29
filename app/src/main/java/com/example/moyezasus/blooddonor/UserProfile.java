package com.example.moyezasus.blooddonor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private ImageView profilePic;
    private TextView textViewName, etextViewPhone, textViewEmail, textViewAge, textviewBloodGroup, textviewAddress, textviewVarsity;
    private Button btnLogout, buttonEdit;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        textViewName = (TextView) findViewById(R.id.textViewName);
        etextViewPhone = (TextView) findViewById(R.id.etextViewPhone);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewAge = (TextView) findViewById(R.id.textViewAge);
        textviewBloodGroup = (TextView) findViewById(R.id.textviewBloodGroup);
        textviewAddress = (TextView) findViewById(R.id.textviewAddress);
        textviewVarsity = (TextView) findViewById(R.id.textviewVarsity);



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        btnLogout = (Button)findViewById(R.id.buttonSingout);
        buttonEdit = (Button)findViewById(R.id.buttonEdit);

        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Info info = dataSnapshot.getValue(Info.class);
                //Glide.with(UserProfile.this).using(new FirebaseImageLoader()).load(IMAGE_URL).into(IMAGE_VIEW);
                textViewName.setText("Name: " + info.name);
                etextViewPhone.setText("Phone: " + info.phone);
                textViewEmail.setText("Email: " + info.email);
                textViewAge.setText("Age: " + info.age);
                textviewBloodGroup.setText("Blood Group: " + info.bloodGenres);
                textviewAddress.setText("Address: " + info.address);
                textviewVarsity.setText("Varsity: " + info.varsityGenres);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserProfile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this,EditProfile.class));
            }
        });

    }

    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(UserProfile.this, MainActivity.class));
    }

}
