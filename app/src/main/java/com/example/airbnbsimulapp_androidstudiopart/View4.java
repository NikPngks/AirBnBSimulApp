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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import airbnbsimulapp.Filters;
import airbnbsimulapp.UserPack;

public class View4 extends AppCompatActivity {
    private static final long serialVersionUID = 8626459955423694374L;
    UserPack t;
    UserPack answer;
    String answer2;
    TextView myText;
    int myID;
    //the filters
    String roomName;
    int startReservation =0;		//I set  01-01-2024  as the default date so that this value will be set in case the user keep it empty
    int noOfBookedDays=1;			//I set 1 as the default value so that this value will be set in case the user keep it empty
    TextView firstDay; //label05 "Choose the first day that you want to book"
    DatePicker firstDayPick; //date01
    TextView numOfDays; //label06 "Complete the number of the days you want to book"
    EditText numOfDaysInt; //input01
    TextView titleAreas;//label07 "Complete the name of the room that you want to book: XML"
    EditText roomName2;//input02
    Button btn4;//btn04 Next
    TextView lastTxt;//label15 ".................."
    Intent i7;
    public Handler myHandler2 = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            //tha parei ws apanthsh ena string!!
            answer2=(String) message.getData().getSerializable("toBePrinted");
            i7 = new Intent(getApplicationContext(), View7.class); //this is the way to initiate the new activity
            i7.putExtra("answer2", answer2);
            startActivity(i7);
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view4);
        Intent i4 = getIntent();
        myID = i4.getIntExtra("myID", 0);

        myText = (TextView) findViewById(R.id.label04);
        myText.setText("the ID is " + myID);

        firstDay = (TextView) findViewById(R.id.label05); //label05 "Choose the first day that you want to book"
        firstDay.setText("Choose the first day you want to book");

        //update the first day of reservation
        firstDayPick=(DatePicker)findViewById(R.id.date01); //date01
        firstDayPick.init(firstDayPick.getYear(), firstDayPick.getMonth(), firstDayPick.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String startDay=(dayOfMonth+"-"+(monthOfYear+1)+"-"+year);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    Date firstDay =dateFormat.parse(startDay);
                    Date referenceDate = dateFormat.parse("01-01-2024");
                    long differenceInMillis = firstDay.getTime() - referenceDate.getTime();
                    long  differenceInDays1 = differenceInMillis / (1000 * 60 * 60 * 24);
                    startReservation = (int) differenceInDays1;
                }catch(ParseException e) {
                    System.err.println("wrong date entry format");
                }
            }
        });

        //update the number of the days to book
        numOfDays=(TextView) findViewById(R.id.label06); //label06 "Complete the number of the days you want to book"
        numOfDays.setText("Complete the number of the days you want to book");
        numOfDaysInt=(EditText) findViewById(R.id.input01); //input01

        numOfDaysInt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                noOfBookedDays=Integer.parseInt(numOfDaysInt.getText().toString());
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
        //I'll pass the data into the package
        t.setIdUser(myID);
        t.setUserSelectIndicator(2);

        btn4 = (Button) findViewById(R.id.btn04);//
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.setChosenFilters(new Filters());
                t.setRoomNameToBeBooked(roomName);
                t.setFirstBookedDay(startReservation);
                t.setNoOfBookedDays(noOfBookedDays);

                MyThread th = new MyThread(myHandler2, t);
                th.start();
            }
        });

        lastTxt=(TextView) findViewById(R.id.label15);// ".................."
        lastTxt.setText("..................");
    }
}