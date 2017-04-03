package com.example.mypc.appchat;

import android.app.Application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by MyPC on 15/03/2017.
 */

public class MyApplication extends Application {
    private static MyApplication _instance;
    private Socket _socket;

    public Socket getSocket(){
        return _socket;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        try {
            _socket = IO.socket("http://192.168.1.168:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static MyApplication getInstance(){
        return _instance;
    }
}
