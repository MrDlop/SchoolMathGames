package com.example.myapplication;

import static androidx.core.content.FileProvider.getUriForFile;
import static com.example.myapplication.globalClass.activity;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskActivity extends AppCompatActivity {

    private static final int REQUEST_TAKE_PHOTO = 1;
    private String TAG = "tag_test_11";
    ImageView imageView;
    private String pictureImagePath = "";

    private Uri photoUri;
    private Uri outputFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        String task_name =
                (String) getIntent().getSerializableExtra("nameTask");
        boolean typeConditional =
                (boolean) getIntent().getSerializableExtra("typeConditional");
        int typeSending =
                (int) getIntent().getSerializableExtra("typeSending");
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout linearLayout = findViewById(R.id.linearLayout);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView = findViewById(R.id.textView2);
        textView.setText(task_name);
        if (typeConditional) {
            ImageView conditionalView = new ImageView(this);
            // TODO: get from server image & set image to conditionalView
            linearLayout.addView(conditionalView);
        } else {
            TextView conditionalView = new TextView(this);
            // TODO: get from server text & set text to conditionalView
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
            // send answer to server
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
            // TODO: send answer to server
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
            // TODO: open camera activity
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            File photo = null;
            try
            {
                // place where to store camera taken picture
                photo = this.createTemporaryFile("picture", ".jpg");
//                photo.delete();
            }
            catch(Exception e)
            {
                Log.v(TAG, "Can't create file to take picture!");
                Toast.makeText(activity, "Please check SD card! Image shot is impossible!", Toast.LENGTH_LONG).show();
//                return false;
            }
            photoUri = getUriForFile(getContext(), "com.mydomain.fileprovider", photo);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            //start camera intent
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        });
        Button sendImageAnswer = new Button(this);
        sendImageAnswer.setText("send");
        sendImageAnswer.setOnClickListener(view -> {
            // TODO: send image to server
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
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            imageView.setImageURI(outputFileUri);
        }
    }
    private File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= Environment.getExternalStorageDirectory();
        tempDir=new File(tempDir.getAbsolutePath()+"/.temp/");
        if(!tempDir.exists())
        {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }
}
