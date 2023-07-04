package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;

public class StartActivity extends AppCompatActivity {
    public Dialog dialogInputTeamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        EditText IPAddress = findViewById(R.id.IPAddress);
        Button buttonConnect = findViewById(R.id.buttonConnect);
        globalClass.activity = this;
        dialogInputTeamName = new Dialog(StartActivity.this) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.dialog_input_team_name);
                Button buttonStart = findViewById(R.id.buttonStart);
                globalClass.dialog = this;
                EditText teamName = findViewById(R.id.teamName);
                buttonStart.setOnClickListener(view -> {
                    try {
                        int edt = Integer.getInteger(String.valueOf(IPAddress.getText()));
                        int a = (int) (edt / Math.pow(256, 3));
                        int b = (int) (edt / Math.pow(256, 2) - a * 256);
                        int c = (int) (edt / 256 - b * 256 - a * Math.pow(256, 2));
                        int d = (int) (edt - c * 256 - b * Math.pow(256, 2) - a * Math.pow(256, 3));
                        String url = String.format("ws://%d.%d.%d.%d:8001", a, b, c, d);
                        URI uri = new URI(url);
                        globalClass.webSocket = new WebSocket(uri);
                        globalClass.teamName = String.valueOf(teamName.getText());
//                        globalClass.webSocket.send("{\"type_request\":\"CONNECT\"," +
//                                "\"type_man\":\"student\"," +
//                                "\"name\":\"Emil\"}");
                        globalClass.webSocket.onConnect(String.valueOf(teamName.getText()));
                    } catch (URISyntaxException e) {
                        Toast.makeText(getApplicationContext(),
                                "Попросите учителя ввести код",
                                Toast.LENGTH_SHORT).show();
                    }
//                    dialogInputTeamName.cancel();
                });
            }
        };
        buttonConnect.setOnClickListener(view -> dialogInputTeamName.show());
    }

    public void start_game() {
        Toast.makeText(getApplicationContext(),
                "Team " + globalClass.teamName + " hello!",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}