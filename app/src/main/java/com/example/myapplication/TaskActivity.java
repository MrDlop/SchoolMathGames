package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskActivity extends AppCompatActivity {

    private static final int TAKE_PICTURE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageView;
    Uri photoURI;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        String task_name =
                (String) getIntent().getSerializableExtra("nameTask");
        JSONObject jsonObject = globalClass.socketClient.getTask(task_name);
        int typeConditional;
        int typeSending;
        try {
            typeConditional = jsonObject.getInt("typeConditional");
            typeSending = jsonObject.getInt("typeSending");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout linearLayout = findViewById(R.id.linearLayout);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView = findViewById(R.id.textView2);
        textView.setText(task_name);
        if (typeConditional == 1) {
            ImageView conditionalView = new ImageView(this);
            Bitmap bmp = BitmapFactory.decodeByteArray(globalClass.bytes,
                    0,
                    globalClass.bytes.length);
            conditionalView.setImageBitmap(Bitmap.createScaledBitmap(bmp,
                    conditionalView.getWidth(),
                    conditionalView.getHeight(),
                    false));
            linearLayout.addView(conditionalView);
        } else {
            TextView conditionalView = new TextView(this);
            try {
                textView.setText(jsonObject.getString("conditional"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            linearLayout.addView(conditionalView);
        }

        LinearLayout linearLayout0 = new LinearLayout(this);
        linearLayout0.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout0.setGravity(Gravity.CENTER);
        TextView textView0 = new TextView(this);
        textView0.setText("Введите свой ответ");
        EditText shortAnswer = new EditText(this);
        shortAnswer.setText("Input your answer");
        Button sendShortAnswer = new Button(this);
        sendShortAnswer.setText("send");
        sendShortAnswer.setOnClickListener(view -> {
            globalClass.socketClient.sendTask(task_name,
                    globalClass.teamName,
                    String.valueOf(shortAnswer.getText()),
                    0);
        });
        linearLayout0.addView(textView0);
        linearLayout0.addView(shortAnswer);
        linearLayout0.addView(sendShortAnswer);

        LinearLayout linearLayout1 = new LinearLayout(this);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout1.setGravity(Gravity.CENTER);
        TextView textView1 = new TextView(this);
        textView1.setText("Введите своё решение");
        EditText longAnswer = new EditText(this);
        longAnswer.setText("Input your answer");
        Button sendLongAnswer = new Button(this);
        sendLongAnswer.setText("send");
        sendLongAnswer.setOnClickListener(view -> {
            globalClass.socketClient.sendTask(task_name,
                    globalClass.teamName,
                    String.valueOf(shortAnswer.getText()),
                    1);
        });
        linearLayout1.addView(textView1);
        linearLayout1.addView(longAnswer);
        linearLayout1.addView(sendLongAnswer);

        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout2.setGravity(Gravity.CENTER);
        TextView textView2 = new TextView(this);
        textView2.setText("Сфотографируйте своё решение");
        Button imageAnswer = new Button(this);
        imageAnswer.setText("photo");
        imageAnswer.setOnClickListener(view -> {
            dispatchTakePictureIntent();
        });
        Button sendImageAnswer = new Button(this);
        sendImageAnswer.setText("send");
        sendImageAnswer.setOnClickListener(view -> {
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageInByte = baos.toByteArray();
            globalClass.socketClient.sendTask(task_name, globalClass.teamName, imageInByte);
        });
        imageView = new ImageView(this);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        linearLayout2.addView(textView2);
        linearLayout2.addView(imageAnswer);
        linearLayout2.addView(sendImageAnswer);

        if (typeSending == 0) {
            linearLayout.addView(linearLayout0);
        } else if (typeSending == 1) {
            linearLayout.addView(linearLayout1);
            linearLayout.addView(linearLayout2);
            linearLayout.addView(imageView);
        } else {
            linearLayout.addView(linearLayout0);
            linearLayout.addView(linearLayout1);
            linearLayout.addView(linearLayout2);
            linearLayout.addView(imageView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
            imageView.setImageURI(photoURI);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
