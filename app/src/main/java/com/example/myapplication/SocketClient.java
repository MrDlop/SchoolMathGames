package com.example.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class SocketClient {
    private final int port;
    private final String host;

    private final String TAG = "SOCKET";

    public String message;

    public SocketClient(String host, int port) {
        this.port = port;
        this.host = host;
    }

    String send(String request) {
        Runnable runnable = () -> {
            try (
                    Socket socket = new Socket(host, port);
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()))
            ) {
                writer.write(request);
                writer.newLine();
                writer.flush();
                message = reader.readLine();
                Log.i(TAG, message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        Thread thread = new Thread(runnable, "sendThread");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    public boolean onConnect(String team_name) {
        String message = this.send("{" +
                "\"type_request\":\"CONNECT\"," +
                "\"type_man\":\"student\"," +
                "\"name\":\"" + team_name + "\"" +
                "}");
        try {
            JSONObject response = new JSONObject(message);
            return response.getBoolean("answer");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void getNewTasks() {
        String message = this.send("{" +
                "\"type_request\":\"NEW_TASK\"," +
                "\"type_man\":\"student\"," +
                "\"name\":\"" + globalClass.teamName + "\"" +
                "}");
        try {
            JSONObject response = new JSONObject(message);
            JSONArray tasks = response.getJSONArray("tasks");
            for (int i = 0; i < tasks.length(); i++) {
                globalClass.tasks_queue.add(tasks.getString(i));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getTask(String task_name) {
        Runnable runnable = () -> {
            try (
                    Socket socket = new Socket(host, port);
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()))
            ) {
                writer.write("{" +
                        "\"type_request\":\"GET\"," +
                        "\"type_man\":\"student\"," +
                        "\"task_name\":\"" + task_name + "\"" +
                        "}");
                writer.newLine();
                writer.flush();
                message = reader.readLine();
                Log.i(TAG, message);
                if (message.equals("file")) {
                    int n = Integer.parseInt(reader.readLine());
                    globalClass.bytes = new byte[n];
                    for (int i = 0; i < n; ++i) {
                        globalClass.bytes[i] = (byte) Integer.parseInt(reader.readLine());
                    }
                    message = reader.readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        Thread thread = new Thread(runnable, "sendThread");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            return new JSONObject(message);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendTask(String task_name, String team_name, String answer, int type) {
        this.send("{" +
                "\"type_request\":\"SEND\"," +
                "\"type_man\":\"student\"," +
                "\"task_name\":\"" + task_name + "\"," +
                "\"team_name\":\"" + team_name + "\"," +
                "\"type_answer\":" + type + "," +
                "\"answer\":\"" + answer + "\"" +
                "}");
    }

    public void sendTask(String task_name, String team_name, byte[] answer) {
        Runnable runnable = () -> {
            try (
                    Socket socket = new Socket(host, port);
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream()));
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()))
            ) {
                for (int i = 0, pi = 0; i < answer.length; ++i) {
                    if (i % 4048 == 0) {
                        int type = 0;
                        if (i != 0) {
                            type = 1;
                            writer.flush();
                        }
                        writer.write("{" +
                                "\"type_request\":\"SEND\"," +
                                "\"type_man\":\"student\"," +
                                "\"task_name\":\"" + task_name + "\"," +
                                "\"team_name\":\"" + team_name + "\"," +
                                "\"type_answer\":2," +
                                "\"type_send\":" + type + "," +
                                "\"len\":" + Math.min(4048, answer.length - i) + "," +
                                "\"send_size\"" + (i + Math.min(4048, answer.length - i)) + "," +
                                "\"full_size\":" + answer.length +
                                "}");
                        writer.newLine();
                    }
                    Log.i(TAG, i + " " + String.valueOf(answer[i]));
                    writer.write(answer[i]);
                    writer.newLine();
                }
                writer.flush();
                message = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        Thread thread = new Thread(runnable, "sendThread");
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
