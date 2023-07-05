package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
    private long IPAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        EditText EIPAddress = findViewById(R.id.IPAddress);
        Button buttonConnect = findViewById(R.id.buttonConnect);
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
                        if (globalClass.start_game_check != globalClass.CONNECT.START_GAME) {
                            long edt = IPAddress;
                            int IPP1 = (int) (edt / Math.pow(256, 3));
                            int IPP2 = (int) (edt / Math.pow(256, 2) - IPP1 * 256);
                            int IPP3 = (int) (edt / 256 - IPP2 * 256 - IPP1 * Math.pow(256, 2));
                            int IPP4 = (int) (edt - IPP3 * 256 - IPP2 * Math.pow(256, 2) - IPP1 * Math.pow(256, 3));
                            @SuppressLint("DefaultLocale")
                            String url = String.format("ws://%d.%d.%d.%d:8001", IPP1, IPP2, IPP3, IPP4);
                            globalClass.webSocket = new WebSocket(new URI(url));
                            globalClass.teamName = String.valueOf(teamName.getText());
                            globalClass.webSocket.onConnect(globalClass.teamName);
                        }
                    } catch (URISyntaxException e) {
                        Toast.makeText(getApplicationContext(),
                                "Попросите учителя ввести код",
                                Toast.LENGTH_SHORT).show();
                    }
                    dialogInputTeamName.cancel();
                });
            }
        };
        buttonConnect.setOnClickListener(view -> {
            IPAddress = Long.parseLong(String.valueOf(EIPAddress.getText()));
            dialogInputTeamName.show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (globalClass.start_game_check == globalClass.CONNECT.START_GAME) {
            start_game();
        } else if (globalClass.start_game_check == globalClass.CONNECT.ALREADY_EXIST) {
            Toast.makeText(getApplicationContext(),
                    "Команда с таким названием уже существует",
                    Toast.LENGTH_SHORT).show();
            globalClass.start_game_check = globalClass.CONNECT.NONE;
        }
    }

    public void start_game() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}