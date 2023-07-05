package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;

import java.util.ArrayList;
import java.util.HashMap;

public class globalClass {
    public static WebSocket webSocket;
    public static CONNECT start_game_check = CONNECT.NONE;
    public enum CONNECT{
        NONE,
        START_GAME,
        ALREADY_EXIST
    }
    public static ArrayList<String> tasks = new ArrayList<>();
    public static Dialog dialog;
    public static String teamName;
    public static HashMap<String, byte[]> tasks_images;
}
