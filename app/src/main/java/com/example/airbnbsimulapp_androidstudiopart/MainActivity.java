package com.example.airbnbsimulapp_androidstudiopart; //the first step where I enter my ID

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final long serialVersionUID = 8626459955423694374L;
    TextView label01;
    Button btn01;
    EditText input01;
    ImageView photo01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        photo01= (ImageView) findViewById(R.id.photo01);
        label01 = (TextView) findViewById(R.id.label01);
        btn01 = (Button) findViewById(R.id.btn01);
        photo01.setImageResource(R.drawable.applogo);
        input01=(EditText) findViewById(R.id.input01);

        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int myID = Integer.parseInt(input01.getText().toString());
                Intent i1 = new Intent(getApplicationContext(), View2.class); //this is the way to initiate the new activity    //i1 I call the 1st intent which I make
                //I can transport information to the new activity with the function putExtra
                i1.putExtra("myID", myID);//pass the user's id to userId
                startActivity(i1);//when I call "startActivity" with the intent which I want, the new activity will start
                //the first thing which will de called by the new activity is the onCreate function
            }
        });
    }
}