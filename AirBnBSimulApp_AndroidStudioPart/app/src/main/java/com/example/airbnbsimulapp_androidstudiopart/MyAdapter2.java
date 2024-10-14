package com.example.airbnbsimulapp_androidstudiopart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import airbnbsimulapp.Room;

public class MyAdapter2 extends BaseAdapter {
    private static final long serialVersionUID = 8626459955423694374L;
    Context context;
    Room[] returnedRooms;
    private LayoutInflater inflater;
    public MyAdapter2(Context context, Room[] returnedRooms){
        this.context=context;
        this.returnedRooms=returnedRooms;
        this.inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return returnedRooms.length;
    }
    @Override
    public Object getItem(int i) {
        return returnedRooms[i];
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list2, container, false);
        }

        TextView roomName = (TextView) convertView.findViewById(R.id.roomName);
        TextView roomArea = (TextView) convertView.findViewById(R.id.roomArea);
        TextView roomPrice = (TextView) convertView.findViewById(R.id.roomPrice);
        TextView roomStars = (TextView) convertView.findViewById(R.id.roomStars);
        ImageView roomImg = (ImageView) convertView.findViewById(R.id.roomImg);

        roomName.setText("Room's name: "+returnedRooms[position].getRoomName());
        roomArea.setText("area: "+returnedRooms[position].getArea());
        roomPrice.setText("price: "+returnedRooms[position].getPrice());
        roomStars.setText("stars: "+returnedRooms[position].getStars());
        Bitmap bitmap= BitmapFactory.decodeByteArray(returnedRooms[position].getByteArrayPNG(), 0, returnedRooms[position].getByteArrayPNG().length);
        roomImg.setImageBitmap(bitmap);

        return convertView;
    }
}