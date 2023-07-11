package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(getApplicationContext(),
                "Team " + globalClass.teamName + " hello!",
                Toast.LENGTH_SHORT).show();
        ListView listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, globalClass.tasks);
        listView.setAdapter(adapter);
        Button updateButton = findViewById(R.id.buttonUpdate);
        updateButton.setOnClickListener(view -> {
            globalClass.socketClient.getNewTasks();
            while (!globalClass.tasks_queue.isEmpty()) {
                addTask(globalClass.tasks_queue.peek());
                globalClass.tasks_queue.remove();
            }
        });

        listView.setOnItemClickListener((parent, itemClicked, position, id) -> {
            TextView textView = (TextView) itemClicked;
            String strText = textView.getText().toString();
            Intent i = new Intent(this, TaskActivity.class);
            i.putExtra("nameTask", strText);
            startActivity(i);
        });
    }


    public void addTask(String task) {
        globalClass.tasks.add(task);
        adapter.notifyDataSetChanged();
    }

    public void deleteTask(String task) {
        globalClass.tasks.removeAll(Collections.singleton(task));
        adapter.notifyDataSetChanged();
    }
}