package com.example.myapplication;

import android.app.Dialog;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class globalClass {
    public static SocketClient socketClient;
    public static CONNECT start_game_check = CONNECT.NONE;

    public enum CONNECT {
        NONE,
        START_GAME,
        ALREADY_EXIST
    }

    public static ArrayList<String> tasks = new ArrayList<>();
    public static Dialog dialog;
    public static String teamName;

    public static byte[] bytes;
    public static Queue<String> tasks_queue = new PriorityQueue<>();
}
