package com.example.sachin.instaimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static java.lang.System.load;

public class SingleInstaActivity extends AppCompatActivity {

    private String post_key= null;
    private DatabaseReference mDatabase;
    private ImageView singlePostImage;
    private TextView singlePostTitle;
    private TextView singlePostDesc;


    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_insta);

        post_key = getIntent().getExtras().getString("PostId");
        mDatabase=FirebaseDatabase.getInstance().getReference().child("InstaApp");
        singlePostImage= (ImageView) findViewById(R.id.singleImageView);
        singlePostTitle= (TextView) findViewById(R.id.singleTitle);
        singlePostDesc= (TextView) findViewById(R.id.singleDesc);
        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title= (String) dataSnapshot.child("title").getValue();
                String post_desc= (String) dataSnapshot.child("desc").getValue();
                String post_image = (String) dataSnapshot.child("image").getValue();
                String post_uid= (String) dataSnapshot.child("uid").getValue();

                singlePostTitle.setText(post_title);
                singlePostDesc.setText(post_desc);
                Picasso.with(SingleInstaActivity.this).load(post_image).into(singlePostImage);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showData(DataSnapshot dataSnapshot){

        for(DataSnapshot ds : dataSnapshot.getChildren()){








        }
    }

}
