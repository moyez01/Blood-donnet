package com.example.moyezasus.blooddonor;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class EditProfile extends AppCompatActivity {

    EditText editTextName, editTextPhone, editTextEmail, editTextPassword,  editTextAge, editTextAddress;
    Spinner spinnerBloodGenres, spinnerVarsityGenres;

    String email, password, name, phone, age, address, bloodGenres, varsityGenres, profileImage;

    private static final int PICK_IMAGE_REQUEST = 1;
    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";

    private Button mButtonChooseImage;
    private Button buttonSave;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ImageView edituserProfile;
    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    private StorageTask mUploadTask;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        spinnerBloodGenres = (Spinner) findViewById(R.id.spinnerBloodGenres);
        spinnerVarsityGenres = (Spinner) findViewById(R.id.spinnerVarsityGenres);
        edituserProfile = (ImageView) findViewById(R.id.edituserProfile);

        mButtonChooseImage = (Button) findViewById(R.id.button_choose_image);
        buttonSave = (Button) findViewById(R.id.buttonSave);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = firebaseDatabase.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });



    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            edituserProfile.setImageURI(mImageUri);

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                edituserProfile.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void uploadFile() {
        if(mImageUri != null)
        {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            mUploadTask = fileReference.putFile(mImageUri)
            //to save image in a folder
            //StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            //ref.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            //profileImage = taskSnapshot.getDownloadUrl().toString();
                            /*Task<Uri> downUrl=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            profileImage = String.valueOf(Log.i("url:",downUrl.getResult().toString()));*/

                            Toast.makeText(EditProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            //Save image info in to firebase database
                            Info info = new Info(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString(), editTextName.getText().toString(), editTextPhone.getText().toString(), editTextEmail.getText().toString(),
                                    editTextAddress.getText().toString(), editTextAge.getText().toString(), spinnerBloodGenres.getSelectedItem().toString(),
                                    spinnerVarsityGenres.getSelectedItem().toString());
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
                            myRef.setValue(info);


                            Log.d("TEST", "path: "+taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
                            Log.d("TEST", "path: "+mImageUri);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }

        /*if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downUrl=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            profileImage = String.valueOf(Log.i("url:",downUrl.getResult().toString()));
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                    //Save image info in to firebase database
                   Info info = new Info(profileImage, editTextName.getText().toString(), editTextPhone.getText().toString(), editTextEmail.getText().toString(),
                            editTextAddress.getText().toString(), editTextAge.getText().toString(), spinnerBloodGenres.getSelectedItem().toString(),
                            spinnerVarsityGenres.getSelectedItem().toString());
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());
                    myRef.setValue(info);

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
            Toast.makeText(this, "DOne", Toast.LENGTH_SHORT).show();
        }*/
        else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }


}
