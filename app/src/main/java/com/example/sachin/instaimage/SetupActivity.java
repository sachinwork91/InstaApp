package com.example.sachin.instaimage;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SetupActivity extends AppCompatActivity {



    private EditText editDisplayName;
    private ImageButton displayImage;
    private static final int GALLERY_REQ = 1;
    private Uri mImageUri= null ;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        editDisplayName = (EditText) findViewById(R.id.displayName);
        displayImage = (ImageButton ) findViewById(R.id.setupImageButton);
        mAuth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
        mStorageReference = FirebaseStorage.getInstance().getReference().child("profile_image");
    }


    //this is used to store the details of the user in the firebase
    public void doneButtonClicked(View view) {
        final String name = editDisplayName.getText().toString().trim();
        final String user_id = mAuth.getCurrentUser().getUid();
        //The name is not empty and the user has selected the image
        if(!TextUtils.isEmpty(name) && mImageUri!=null){
            StorageReference filepath = mStorageReference.child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadurl= taskSnapshot.getDownloadUrl().toString();
                    mDatabase.child(user_id).child("name").setValue(name);
                    mDatabase.child(user_id).child("image").setValue(downloadurl);
                    Toast.makeText(SetupActivity.this,
                           "Profile Successfully Updated", //ADD THIS
                            Toast.LENGTH_SHORT).show();
                      Intent intent= new Intent(SetupActivity.this, MainActivity.class);
                      startActivity(intent);

                }
            });


        }else if(mImageUri==null){
            Toast.makeText(SetupActivity.this,
                    "please select a profile picture", //ADD THIS
                    Toast.LENGTH_SHORT).show();

        }


    }


    //When the image button is clicked
    public void profileImageButtonClicked(View view) {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("Image/*");
        startActivityForResult(galleryIntent, GALLERY_REQ);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //We are able to get the image from gallery correctly
        if(requestCode == GALLERY_REQ && resultCode == RESULT_OK){

        Uri imageUri = data.getData();
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
        }




        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE )
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                mImageUri = result.getUri();
                displayImage.setImageURI(mImageUri);

            }else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }

    }
}
