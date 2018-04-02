package com.example.sachin.instaimage;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class LoginActivity extends AppCompatActivity {


       private EditText loginEmail;
       private EditText password;
       private FirebaseAuth mAuth;
       private DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail= (EditText) findViewById(R.id.loginEmail);
        password= (EditText) findViewById(R.id.loginPass);


        mAuth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Users");
    }



    public void loginButtonClicked(View view){

        String email = loginEmail.getText().toString().trim();
        String pass = password.getText().toString().trim();


        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)){
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                                checkUserExists();
                            }
                        }
                    }
            );
        }
    }


    //THis method checks if the User exists or not
    public void checkUserExists(){

        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(user_id)){ // the User exists and redirecting the user

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
