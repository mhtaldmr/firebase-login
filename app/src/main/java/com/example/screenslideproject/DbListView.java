package com.example.screenslideproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DbListView extends AppCompatActivity  {

    String[] myStringArray= new  String[]{
            "tag.png",
            "beautiful.png",
            "call.png",
            "gift.png",
            "invert.png",
            "product.png",
            "speaker.png"
    };
    String input,input2,input3;
    EditText mInput,mInput2,mInput3;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myOtherRef = database.getReference().child("newUsers").child("333");

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference mStorageRef = storage.getReference();


    final File localFile = File.createTempFile("images","jpeg");
    public DbListView() throws IOException {
    }


    @SuppressLint("Assert")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_list_view);

        mInput = findViewById(R.id.editText);
        mInput2 = findViewById(R.id.editText2);
        mInput3 = findViewById(R.id.editText3);
        ImageView avatarImage = findViewById(R.id.avatarimage);
        ImageView avatarImage2 = findViewById(R.id.avatarimage2);
        ListView myListView = findViewById(R.id.myListView);
        Button mButton4 = findViewById(R.id.button4);
        input3 = mInput3.getText().toString();


        ArrayAdapter<String> mArrayAdapter = new ArrayAdapter
                <>(this, android.R.layout.simple_expandable_list_item_1, myStringArray);



        Button mButton = findViewById(R.id.button1);
        mButton.setOnClickListener(v -> {
            input = mInput.getText().toString();
            input2 = mInput2.getText().toString();
            writeNewUser(input2,input);
/*
            myRef = database.getReference(input2);
            myRef.setValue(input);
 */
        });

        Button mButton2 = findViewById(R.id.button2);
        mButton2.setOnClickListener(v -> {
            ValueEventListener listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User u;
                    u = dataSnapshot.getValue(User.class);
                    assert u != null;
                    Toast.makeText(getApplicationContext(),"Name:" + u.name+"\n"+"Email:"+u.email,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            myOtherRef.addValueEventListener(listener);

            /*
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
            */
        });


        Button mButton3 = findViewById(R.id.button3);


        mButton3.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i,1);
        });

        mButton4.setOnClickListener(v -> {
            StorageReference mOtherStorageRef;
            mOtherStorageRef = mStorageRef.child("1377130043");
            mOtherStorageRef.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        avatarImage2.setImageBitmap(bitmap);
                    })
                    .addOnFailureListener(e -> Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show());



        });

        if(myListView !=null){
            myListView.setAdapter(mArrayAdapter);
        }


        assert myListView != null;
        myListView.setOnItemClickListener((parent, view, position, id) -> {
            StorageReference mOtherStorageRef;
            mOtherStorageRef = mStorageRef.child(myStringArray[position]);
            mOtherStorageRef.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        avatarImage.setImageBitmap(bitmap);
                    });

        });





    }

    public void writeNewUser (String name, String email ){
        User user = new User(name,email);
        myRef.child("newUsers").push().setValue(user);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK){
            Uri myUri;
            assert data != null;
            myUri = data.getData();
            assert myUri != null;
            StorageReference myPhotoRef= mStorageRef.child(Objects.requireNonNull(myUri.getLastPathSegment()));
            myPhotoRef.putFile(myUri).addOnSuccessListener(taskSnapshot ->
                    Toast.makeText(getApplicationContext(),"Upload is successful!",Toast.LENGTH_SHORT).show());
        }

    }


}
