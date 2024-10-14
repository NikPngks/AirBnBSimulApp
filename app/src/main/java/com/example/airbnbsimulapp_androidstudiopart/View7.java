package com.example.airbnbsimulapp_androidstudiopart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import airbnbsimulapp.UserPack;

public class View7 extends AppCompatActivity{
    private static final long serialVersionUID = 8626459955423694374L;
    UserPack t;
    UserPack answer2;
    TextView myText;
    int myID;
    //TA FILTRA
    String apanthsh;
    TextView temp455; //label04 "Choose the first day that you want to book"
    Button btn6;
    TextView lastTxt;//label15 ".................."

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view7);
        Intent i3 = getIntent();
        myID = i3.getIntExtra("myID", 0);
        myText = (TextView) findViewById(R.id.label04);
        myText.setText("to ID toy einai " + myID);

        apanthsh=i3.getStringExtra("answer2");
        temp455 =(TextView) findViewById(R.id.label05);
        temp455.setText(apanthsh);

        btn6=(Button) findViewById(R.id.btn06);//
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i6;
                i6 = new Intent(getApplicationContext(), View2.class); //this is the way to initiate the new activity
                startActivity(i6);
            }
        });

        lastTxt=(TextView) findViewById(R.id.label15);// ".................."
        lastTxt.setText("..................");
    }
}