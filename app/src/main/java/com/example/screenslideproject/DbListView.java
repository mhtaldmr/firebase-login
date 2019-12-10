package com.example.screenslideproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DbListView extends AppCompatActivity  {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    DatabaseReference myOtherRef = database.getReference().child("newUsers").child("333");
    String input= null;
    String input2  = null;
    EditText minput = null;
    EditText minput2 = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_list_view);

        minput = findViewById(R.id.editText);
        minput2 = findViewById(R.id.editText2);
        Button mButton = findViewById(R.id.button1);


        mButton.setOnClickListener(v -> {
            input = minput.getText().toString();
            input2 = minput2.getText().toString();
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

    }



    public void writeNewUser ( String name, String email ){
        User user = new User(name,email);
        myRef.child("newUsers").push().setValue(user);
    }

}
