package com.example.airbnbsimulapp_androidstudiopart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import airbnbsimulapp.Room;
import airbnbsimulapp.RoomNode;
import airbnbsimulapp.UserPack;


public class View6 extends AppCompatActivity {
    private static final long serialVersionUID = 8626459955423694374L;
    TextView txt1;
    ListView mylistView2;
    Room[] returnedRooms;
    UserPack answer;
    Button btn6;
    TextView lastTxt;//label15 ".................."

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view6);

        Intent myIntent = getIntent();
        answer=(UserPack) myIntent.getExtras().get("answer");

        //I calculate how many rooms came as response
        int i = 0;
        RoomNode temp1 = answer.getTempItem().getHeadRoomNode();
        while (temp1 != null && temp1.getRoomItem() != null) {
            i++;
            temp1 = temp1.getPrevRoomNode();
        }

        //I pass the rooms which came to a Room[]
        returnedRooms = new Room[i];
        i = 0;
        temp1 = answer.getTempItem().getHeadRoomNode();
        while (temp1 != null && temp1.getRoomItem() != null) {
            returnedRooms[i] = temp1.getRoomItem();
            i++;
            temp1 = temp1.getPrevRoomNode();
        }

        txt1=(TextView) findViewById(R.id.label03);
        txt1.setText("The available roomS based on your filters are the following: ");

        mylistView2 = (ListView) findViewById(R.id.listView02);

        MyAdapter2 adapter2 = new MyAdapter2(this, returnedRooms);

        mylistView2.setAdapter(adapter2);//pass the respones to a list

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