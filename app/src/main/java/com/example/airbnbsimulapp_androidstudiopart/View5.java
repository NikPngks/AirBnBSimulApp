package com.example.airbnbsimulapp_androidstudiopart;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import airbnbsimulapp.Filters;
import airbnbsimulapp.UserPack;

public class View5 extends AppCompatActivity {
    private static final long serialVersionUID = 8626459955423694374L;
    UserPack t;
    UserPack answer;
    TextView myText;
    int myID;
    String roomName;
    int noOfRate;
    String answer2;
    TextView lastTxt;//label15 ".................."
    TextView numOfStars; //label06 "Complete the number of the stars you want to rate"
    EditText numOfRate; //input01
    TextView titleAreas;//label07 "Complete the name of the room that you want to rate: "
    EditText roomName2;//input02
    Button btn2;//btn03 Next
    Intent i9;

    public Handler myHandler3 = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            //the response is a string!!
            answer2=(String) message.getData().getSerializable("toBePrinted");
            i9 = new Intent(getApplicationContext(), View7.class); //this is the way to initiate the new activity
            i9.putExtra("answer2", answer2);
            startActivity(i9);
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view5);
        Intent i3 = getIntent();
        myID = i3.getIntExtra("myID", 0);

        myText = (TextView) findViewById(R.id.label04);
        myText.setText("the ID is " + myID);

        numOfStars = (TextView) findViewById(R.id.label06);
        numOfStars.setText("Complete the number of the stars you want to rate");

        numOfRate = (EditText) findViewById(R.id.input01); //input01

        numOfRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                noOfRate=Integer.parseInt(numOfRate.getText().toString());
            }
        });

        titleAreas = (TextView) findViewById(R.id.label07);//label07 "Complete the name of the room that you want to book: "
        titleAreas.setText("Complete the name of the room that you want to book: ");
        //perioxh 1h
        roomName2 = (EditText) findViewById(R.id.input02);//input02
        roomName2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                roomName = s.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        t = new UserPack();
        //here I will pass the data I chose to a package
        t.setIdUser(myID);
        t.setUserSelectIndicator(3);

        btn2 = (Button) findViewById(R.id.btn03);//
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.setChosenFilters(new Filters());
                t.setRoomNameToBeBooked(roomName);
                t.setRoomReview(noOfRate);

                MyThread th = new MyThread(myHandler3, t);
                th.start();
            }
        });

        lastTxt=(TextView) findViewById(R.id.label15);// ".................."
        lastTxt.setText("..................");
    }
}