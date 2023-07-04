package com.example.myapplication;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Objects;

import tech.gusavila92.websocketclient.WebSocketClient;

public class WebSocket extends WebSocketClient {
    /**
     * Initialize all the variables
     *
     * @param uri URI of the WebSocket server
     */

    private int get_file;
    public WebSocket(URI uri) {
        super(uri);
        get_file = 0;
        this.setConnectTimeout(10000);
        this.setReadTimeout(60000);
        this.enableAutomaticReconnection(5000);
        this.connect();
    }

    @Override
    public void onOpen() {
    }

    public void onConnect(String team_name) {
        this.send("{" +
                "\"type_request\":\"CONNECT\"," +
                "\"type_man\":\"student\"," +
                "\"name\":\"" + team_name + "\"" +
                "}"
        );
    }

    @Override
    public void onTextReceived(String message) {
        // you actions when receive message
        Log.e("Test", message);
        if(get_file != 0){
            if(Objects.equals(message, "None")){
                get_file = 0;
            } else if (get_file == 1) {
//                    globalClass.tasks_images
            }
            {

            }
        }else {
            if (Objects.equals(message, "TEAM_1")) {
                StartActivity activity = globalClass.activity;
                activity.start_game();
            } else if (Objects.equals(message, "TEAM_0")) {
                StartActivity activity = globalClass.activity;
                Toast toast = Toast.makeText(
                        activity,
                        "Команда с таким названием уже существует",
                        Toast.LENGTH_SHORT);
                toast.show();
            } else if (Objects.equals(message, "file")) {
                get_file = 1;
            }
        }
    }


    @Override
    public void onBinaryReceived(byte[] data) {

    }

    @Override
    public void onPingReceived(byte[] data) {

    }

    @Override
    public void onPongReceived(byte[] data) {

    }

    @Override
    public void onException(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onCloseReceived() {
        // session close handling
        this.send("{" +
                "\"request\":\"DISCONNECT\"," +
                "}"
        );
    }
}