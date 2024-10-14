package com.example.airbnbsimulapp_androidstudiopart;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
    private static final long serialVersionUID = 8626459955423694374L;
    Context context;
    String strings[];
    int integers[];
    // override other abstract methods here
    private LayoutInflater inflater;    //this take an xml and renter to a view.
    int choice;
    int myID;
    public MyAdapter(Context context, String[] strings, int[] integers, int myID){
        this.context=context;
        this.strings=strings;
        this.integers=integers;
        this.inflater = (LayoutInflater.from(context));
        this.myID=myID;
    }

    @Override
    public int getCount() {
        return strings.length;
    }
    @Override
    public Object getItem(int i) {
        return strings[i];
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list1, container, false);
        }

        TextView optTxt = (TextView) convertView.findViewById(R.id.list_text);
        ImageButton optBtn = (ImageButton) convertView.findViewById(R.id.list_btn);

        optTxt.setText(strings[position]);
        optBtn.setImageResource(integers[position]);

        //fix the setOnClickListener for the ImageButton
        optBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice =position;
                Intent i3;
                if(choice ==0){                 //if chose the filter option it will go to View3
                    i3 = new Intent(context, View3.class);
                }else if(choice ==1){           //if chose the book option it will go to View4
                    i3 = new Intent(context, View4.class);
                }else{                          //if chose the rate option it will go to View5
                    i3 = new Intent(context, View5.class);
                }
                i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Needed if using application context
                i3.putExtra("label02", strings[position]);
                i3.putExtra("epilogh", choice);
                i3.putExtra("myID",myID);
                context.startActivity(i3);
            }
        }));
        return convertView;
    }
}