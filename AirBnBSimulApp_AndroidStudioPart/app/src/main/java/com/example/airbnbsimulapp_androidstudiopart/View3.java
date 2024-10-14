package com.example.airbnbsimulapp_androidstudiopart;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.widget.DatePicker;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import androidx.activity.EdgeToEdge;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import airbnbsimulapp.UserPack;
import airbnbsimulapp.Filters;

//filter option
public class View3 extends AppCompatActivity {
    private static final long serialVersionUID = 8626459955423694374L;
    UserPack t;
    UserPack answer;
    String answer2;
    TextView myText;
    int myID;

    //the filters
    String[] area= {null,null,null};	//set NULL as default value in order to must choose an area
    int intForChosenAreas = 0;
    double minPrice=0;			//I set 0 euro as the default value so that this value will be set in case the user keep it empty
    double maxPrice=1000000;	//I set 1 million euro as the default value so that this value will be set in case the user keep it empty
    double stars=0;				//I set 0 stars as the default value so that this value will be set in case the user keep it empty
    int noOfPerson=1;			//I set 1 person as the default value so that this value will be set in case the user keep it empty
    //check
    int startReservation =0;		//I set  01-01-2024  as the default date so that this value will be set in case the user keep it empty
    int noOfBookedDays=1;		//I set 1 as the default value so that this value will be set in case the user keep it empty
    Filters tempFltr=new Filters();
    TextView firstDay; //label05 "Choose the first day that you want to book"
    DatePicker firstDayPick; //date01
    TextView numOfDays; //label06 "Complete the number of the days you want to book"
    EditText numOfDaysInt; //input01
    TextView titleAreas;//label07 "Complete at least one area:"
    EditText fstArea;//input02
    EditText sndArea;//input03
    EditText trdArea;//input04
    TextView titleStars;//label08 "Check the minimum number of stars"
    TextView title1Star;//label09 "Pleasant: 1+"
    CheckBox chck1;//CheckBox01
    TextView title2Star;//label10 "Good: 2+"
    CheckBox chck2;//CheckBox02
    TextView title3Star;//label11 "Very good: 3+"
    CheckBox chck3;//CheckBox03
    TextView title4Star;//label12 "Superb: 4+"
    CheckBox chck4;//CheckBox04
    TextView titleGuests;//label13 "Complete the number of the guests"
    SeekBar numGuests;//seekbar01
    TextView titlePrice;//label14 "Choose the preferable price per day"
    EditText minUsrPrice;//input05
    EditText maxUsrPrice;//input06
    TextView lastTxt;//label15 ".................."
    Button btn2;//btn02 Next
    int indicatorMsg;
    Intent i6;
    public Handler myHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if(message.getData().getInt("pntr")==1) {
                indicatorMsg =1;
                answer = (UserPack) message.getData().getSerializable("masterPackNew");
                i6 = new Intent(getApplicationContext(), View6.class); //this is the way to initiate the new activity
                i6.putExtra("answer", answer);
            }else{
                indicatorMsg =0;
                answer2=(String) message.getData().getSerializable("toBePrinted");
                i6 = new Intent(getApplicationContext(), View7.class); //this is the way to initiate the new activity
                i6.putExtra("answer2", answer2);
            }
            startActivity(i6);
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view3);
        Intent i3=getIntent();
        myID = i3.getIntExtra("myID",0);
        //int myID=i3.getIntExtra("myID",0);
        int epilogh=i3.getIntExtra("epilogh",0);//here I took the user's choice (0 or 1 or 2)

        myText=(TextView) findViewById(R.id.label04);
        myText.setText(" the choice is: "+epilogh+" and the ID is "+myID);//for debugging reasons

        firstDay=(TextView) findViewById(R.id.label05); //label05 "Choose the first day that you want to book"
        firstDay.setText("Choose the first day that you want to book");

        //update the first day
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

        //update the areas
        titleAreas=(TextView) findViewById(R.id.label07);//label07 "Complete at least one area:"
        titleAreas.setText("Complete at least one area:");
        //1st area
        fstArea=(EditText) findViewById(R.id.input02);//input02
        fstArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                area[0]=s.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //2nd area
        sndArea=(EditText) findViewById(R.id.input03);//input03
        sndArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                area[1]=s.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //3rd area
        trdArea=(EditText) findViewById(R.id.input04);//input04
        trdArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                area[2]=s.toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //update the stars
        titleStars=(TextView) findViewById(R.id.label08);//label08 "Check the minimum number of stars"
        titleStars.setText("Check the minimum number of stars");
        title1Star=(TextView) findViewById(R.id.label09);//label09 "Pleasant: 1+"
        title1Star.setText("Pleasant: 1+");
        chck1=(CheckBox) findViewById(R.id.CheckBox01);//CheckBox01
        chck1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    stars = 1.00;
                }
            }
        });

        title2Star=(TextView) findViewById(R.id.label10);//label10 "Good: 2+"
        title2Star.setText("Good: 2+");
        chck2=(CheckBox) findViewById(R.id.CheckBox02);//CheckBox02
        chck2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    stars = 2.00;
                }
            }
        });

        title3Star=(TextView) findViewById(R.id.label11);//label11 "Very good: 3+"
        title3Star.setText("Very good: 3+");
        chck3=(CheckBox) findViewById(R.id.CheckBox03);//CheckBox03
        chck3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    stars = 3.00;
                }
            }
        });

        title4Star=(TextView) findViewById(R.id.label12);//label12 "Superb: 4+"
        title4Star.setText("Superb: 4+");
        chck4=(CheckBox) findViewById(R.id.CheckBox04);//CheckBox04
        chck4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked) {
                    stars = 4.00;
                }
            }
        });

        //update the number of visitors
        titleGuests=(TextView) findViewById(R.id.label13);//label13 "Complete the number of the guests"
        titleGuests.setText("Complete the number of the guests");
        numGuests=(SeekBar)  findViewById(R.id.seekbar01);//seekbar01
        numGuests.setMax(9);
        numGuests.setMin(0);
        numGuests.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                noOfPerson=progress+1;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //update the min and max price
        titlePrice=(TextView) findViewById(R.id.label14);//label14 "Choose the preferable price per day"
        titlePrice.setText("Choose the preferable price per day");
        minUsrPrice=(EditText) findViewById(R.id.input05);//input05

        minUsrPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                minPrice=Integer.parseInt(minUsrPrice.getText().toString());
            }
        });

        maxUsrPrice=(EditText) findViewById(R.id.input06);//input06

        maxUsrPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                maxPrice=Integer.parseInt(maxUsrPrice.getText().toString());
            }
        });

        t=new UserPack();
        //here I will pass the chosen filters
        t.setIdUser(myID);
        t.setUserSelectIndicator(1);

        //here the t is ready to be sent
        //the package is the "t" and I have to dend it to the master
        //the next package I will have to check if it will be package or string
        btn2=(Button) findViewById(R.id.btn03);//
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempFltr.setArea(area[0]);
                if(area[1]!=null){
                    tempFltr.setArea(area[1]);
                }
                if(area[2]!=null){
                    tempFltr.setArea(area[2]);
                }
                tempFltr.setMinPrice(minPrice);
                tempFltr.setMaxPrice(maxPrice);
                tempFltr.setStars(stars);
                tempFltr.setNoOfPerson(noOfPerson);
                tempFltr.setReservationStart(startReservation);
                tempFltr.setNoOfBookedDays(noOfBookedDays);
                t.setChosenFilters(tempFltr);

                MyThread th = new MyThread(myHandler,t);
                th.start();
            }
        });

        lastTxt=(TextView) findViewById(R.id.label15);// ".................."
        lastTxt.setText("..................");
    }
}