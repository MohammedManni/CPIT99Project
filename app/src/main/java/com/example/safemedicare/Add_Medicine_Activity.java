package com.example.safemedicare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.ArrayList;

public class Add_Medicine_Activity extends AppCompatActivity {
    private String name, type;
    EditText medName;
    // text recognition varibles//
    Button addBT;
    ImageView imageView;
    TextView note;
    /////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_medicin);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
              name = extras.getString("USERNAME");
              type = extras.getString("TYPE");
            //name = "manni";
            // type = "patient";
            Button SecondB = findViewById(R.id.SecondB);
            if (type.matches("caregiver")) {
                SecondB.setVisibility(View.GONE);
            }
        }
        ////////////////////////   TOOL BAR //////////////////////////////////
        toolbar();
        ImageButton cam = findViewById(R.id.cameraButton);
        ImageButton voice = findViewById(R.id.imageButton2);
        ImageButton text = findViewById(R.id.imageButton3);
        note = findViewById(R.id.NOTE);
        medName = findViewById(R.id.nameOfMedicineET);
        addBT = findViewById(R.id.AddBT);

        note.setVisibility(View.INVISIBLE);
        medName.setVisibility(View.INVISIBLE);
        addBT.setVisibility(View.INVISIBLE);


        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordSpeech();
                note.setVisibility(View.VISIBLE);
                medName.setVisibility(View.VISIBLE);
                addBT.setVisibility(View.VISIBLE);
            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Medicine_Activity.this, Add_Medicine_Text.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });


        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (medName.getText().toString().isEmpty()) {
                    medName.setError("There is no name");
                } else {
                    Intent intent = new Intent(Add_Medicine_Activity.this, Add_Medicine_Text.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    intent.putExtra("medName", medName.getText().toString());
                    startActivity(intent);
                }

            }
        });


        ///////////////////////Text recognition from camera //////////////////////////////////////
        imageView = findViewById(R.id.image_view0);
        cam.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                //check app level permission is granted for Camera
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //grant the permission
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
                } else {
                    doProcess(view);
                }
            }
        });


    }

    //camera call method
    public void doProcess(View view) {
        //open the camera => create an Intent object
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }
    //voice call method
    private void recordSpeech() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {

            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(this, "Your device does not support Speech recognizer", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // detect text from the voice
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                medName.setText(text.get(0));

            }

        }
        // detect text from the picture
        else if (requestCode == 101) {
            Bundle bundle = data.getExtras();
            //from bundle, extract the image
            Bitmap bitmap = (Bitmap) bundle.get("data");
            //set image in imageview
            imageView.setImageBitmap(bitmap);
            //process the image
            //create a FirebaseVisionImage object from a Bitmap object
            FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
            //Get an instance of FirebaseVision
            FirebaseVision firebaseVision = FirebaseVision.getInstance();
            //Create an instance of FirebaseVisionTextRecognizer
            FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();
            //Create a task to process the image
            Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);

            task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                @Override
                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    String s = firebaseVisionText.getText();
                    note.setVisibility(View.VISIBLE);
                    medName.setVisibility(View.VISIBLE);
                    addBT.setVisibility(View.VISIBLE);
                    medName.setText(s);
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void toolbar() {
        // toolbar buttons
        Button Profile = findViewById(R.id.firstB);
        Button Schedule = findViewById(R.id.SecondB);
        Button Add = findViewById(R.id.thirdB);
        Button SOS = findViewById(R.id.SOS);
        ImageButton imageButton = findViewById(R.id.imageButton);

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Medicine_Activity.this, Patient_Profile_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Medicine_Activity.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Medicine_Activity.this, Add_Medicine_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Medicine_Activity.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")) {
                    Intent intent = new Intent(Add_Medicine_Activity.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                } else if (type.equalsIgnoreCase("caregiver")) {
                    Intent intent = new Intent(Add_Medicine_Activity.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }
            }
        });
    }

}

