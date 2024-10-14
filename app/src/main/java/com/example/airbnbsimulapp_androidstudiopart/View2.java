package com.example.airbnbsimulapp_androidstudiopart;     //choose  of  options

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class View2 extends AppCompatActivity {
    private static final long serialVersionUID = 8626459955423694374L;
    TextView label02;
    TextView label03;
    ListView mylistView;
    String optionTxt[]={"1. Add Filters","2. Book a Room","3. Rate a Room"};
    int optionImg[]={R.drawable.filterroom,R.drawable.bookroom,R.drawable.rateroom};
    int myID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view2);

        Intent myIntent = getIntent();
        myID = myIntent.getIntExtra("myID",0);

        mylistView = (ListView) findViewById(R.id.listView01);

        MyAdapter adapter = new MyAdapter(this, optionTxt, optionImg,myID);

        mylistView.setAdapter(adapter);

        label03 = (TextView) findViewById(R.id.label03);

        label02 = (TextView) findViewById(R.id.label02);
        label02.setText("Choose one of the following options");

        label03.setText("Your ID is "+myID);

        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }
}