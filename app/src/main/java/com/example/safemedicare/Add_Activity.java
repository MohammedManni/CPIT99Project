package com.example.safemedicare;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;

import static android.Manifest.permission.CAMERA;

public class Add_Activity extends AppCompatActivity {
    Button Profile, Scheduale, Add, SOS, imageButton;
    private String name, type;

    // text recognition varibles//
    Button captureImageButton,detectText;
    ImageView imageView;
    TextView textView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    /////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_text);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");

            Button SecondB = findViewById(R.id.SecondB);
            if (type.matches("caregiver")) {
                SecondB.setVisibility(View.GONE);
            }
        }
        /////////////////////////////////////////////////////////////////////

        // toolbar buttons
        Button Profile = findViewById(R.id.firstB);
        Button Schedule = findViewById(R.id.SecondB);
        Button Add = findViewById(R.id.thirdB);
        Button SOS = findViewById(R.id.SOS);
        ImageButton imageButton = findViewById(R.id.imageButton);



        ////////////////////////////////////////////////////////////////////////////

        ///////////////////////Text recognition//////////////////////////////////////
        captureImageButton = findViewById(R.id.capture_image);
        detectText = findViewById(R.id.detect_text);
        imageView = findViewById(R.id.image_view);
        textView = findViewById(R.id.text_display);

        captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()){
                    dispatchTakePictureIntent();
                }else{
                    requestPermission();
                }

            }
        });
        
        detectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectTextFromImage();

            }
        });
    }


    // methods from developers////

        //////////////////METHOD TO ALLOW USER OPEN PHONE CAMERA/////////////////////////
        private void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                //Toast.makeText(Add_Activity.this,"aaaaaaaaaaaaaaa",Toast.LENGTH_SHORT).show();
               // startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            Toast.makeText(Add_Activity.this,"bbbbbbbbb",Toast.LENGTH_SHORT).show();
        }

    //////////////////METHOD TO GET DATA FROM PHONE CAMERA////////
    private boolean checkPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        int PERMISSION_CODE=200;
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},PERMISSION_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0){
            boolean cameraPermission=grantResults[0]==PackageManager.PERMISSION_GRANTED;
            if (cameraPermission){
                Toast.makeText(Add_Activity.this,"Permissions Granted",Toast.LENGTH_SHORT).show();
            dispatchTakePictureIntent();
            }else{
                Toast.makeText(Add_Activity.this,"Permissions denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
             imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void detectTextFromImage() {
        InputImage image=InputImage.fromBitmap(imageBitmap,0);
        TextRecognizer recognizer= TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result=recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(@NonNull Text text) {
                StringBuilder result=new StringBuilder();
                for (Text.TextBlock block: text.getTextBlocks()){
                    String blockText=block.getText();
                    Point[] blockCornerPoint = block.getCornerPoints();
                    Rect blockFrame = block.getBoundingBox();
                    for(Text.Line line:block.getLines()){
                        String lineText=line.getText();
                        Point[] lineCornerPoint = line.getCornerPoints();
                        Rect lineRect = line.getBoundingBox();
                        for(Text.Element element:line.getElements()){
                            String elementText =element.getText();
                            result.append(elementText);
                        }
                        textView.setText(blockText);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Add_Activity.this,"Error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        /*
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector firebaseVisionTextDetector= FirebaseVision.getInstance().getVisionTextDetector();
        firebaseVisionTextDetector.detectInImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Add_Activity.this,"Error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
         */
    }

    /*private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.Block> blockList=firebaseVisionText.getBlocks();
        if (blockList.size() == 0) {
            Toast.makeText(Add_Activity.this,"No text in image",Toast.LENGTH_SHORT).show();
        }else{
            for (FirebaseVisionText.Block block :firebaseVisionText.getBlocks() ){
                String text=block.getText();
                textView.setText(text);
            }
        }
     */

    }


