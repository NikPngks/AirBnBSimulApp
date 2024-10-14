package com.example.airbnbsimulapp_androidstudiopart;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import airbnbsimulapp.UserPack;

public class MyThread extends Thread{
    private static final long serialVersionUID = 8626459955423694374L;
    Handler myHandlerX;
    UserPack t;
    public MyThread(Handler handler, UserPack us){ //pass the package on thread as t
        this.myHandlerX = handler;
        this.t=us;
    }

    @Override
    public void run() {
        UserPack masterPackNew=null;
        Socket requestSocketToMaster = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;

        Message m = new Message();
        Bundle b = new Bundle();
        int indicator;
        try {
            requestSocketToMaster = new Socket("0.0.0.0", 1234); //you have to add your Master's host here
            out = new ObjectOutputStream(requestSocketToMaster.getOutputStream());
            in = new ObjectInputStream(requestSocketToMaster.getInputStream());
            out.writeObject(this.t); //here I send it to MASTER
            out.flush();
            try{
                Object masterAnswer = (Object) in.readObject(); //here I recieve the package
                if(masterAnswer  instanceof String) {
                    String toBePrinted = (String) masterAnswer;
                    indicator=0;//I will use it to check what I receive, string or userpack
                    b.putInt("pntr",indicator);
                    b.putString("toBePrinted",toBePrinted);
                    m.setData(b);
                }else {
                    masterPackNew = (UserPack) masterAnswer;
                    indicator=1;
                    b.putInt("pntr",indicator);
                    b.putSerializable("masterPackNew", masterPackNew);
                    m.setData(b);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        myHandlerX.sendMessage(m);
    }
}
