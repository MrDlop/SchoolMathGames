package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;

import java.util.ArrayList;
import java.util.HashMap;

public class globalClass {
    public static WebSocket webSocket;
    public static ArrayList<String> tasks = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static StartActivity activity;
    public static Dialog dialog;
    public static String teamName;
    public static HashMap<String, byte[]> tasks_images;
}
