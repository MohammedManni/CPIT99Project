package com.example.safemedicare;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Add_Medicine_Activity extends AppCompatActivity {
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
        setContentView(R.layout.add_medicin);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");

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
        CheckBox checkBox2 = findViewById(R.id.checkBox2);
        CheckBox checkBox3 = findViewById(R.id.checkBox3);
        CheckBox checkBox4 = findViewById(R.id.checkBox4);
        CheckBox checkBox5 = findViewById(R.id.checkBox5);
        TextView note  = findViewById(R.id.NOTE);
        EditText medName =findViewById(R.id.nameOfMedicineET);
        Button addBT = findViewById(R.id.AddBT);

        checkBox2.setVisibility(View.INVISIBLE);
        checkBox3.setVisibility(View.INVISIBLE);
        checkBox4.setVisibility(View.INVISIBLE);
        checkBox5.setVisibility(View.INVISIBLE);
        note.setVisibility(View.INVISIBLE);
        medName.setVisibility(View.INVISIBLE);
        addBT.setVisibility(View.INVISIBLE);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox2.setVisibility(View.VISIBLE);
                checkBox3.setVisibility(View.VISIBLE);
                checkBox4.setVisibility(View.VISIBLE);
                checkBox5.setVisibility(View.VISIBLE);
                note.setVisibility(View.VISIBLE);
                medName.setVisibility(View.VISIBLE);
                addBT.setVisibility(View.VISIBLE);


            }
        });
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox2.setVisibility(View.VISIBLE);
                checkBox3.setVisibility(View.VISIBLE);
                checkBox4.setVisibility(View.VISIBLE);
                checkBox5.setVisibility(View.VISIBLE);
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
        medName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medName.setText("");
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBox2.isChecked()){
                    medName.setText(medName.getText().toString()+" "+checkBox2.getText().toString());
                }
            }
        });

        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBox3.isChecked()){
                    medName.setText(medName.getText().toString()+" "+checkBox3.getText().toString());
                }
            }
        });
        checkBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBox4.isChecked()){
                    medName.setText(medName.getText().toString()+" "+checkBox4.getText().toString());
                }
            }
        });
        checkBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBox5.isChecked()){
                    medName.setText(medName.getText().toString()+" "+checkBox5.getText().toString());
                }
            }
        });



        addBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (medName.getText().toString().isEmpty()){
                    medName.setError("Please chose the name from above");
                }else {
                    Intent intent = new Intent(Add_Medicine_Activity.this, Add_Medicine_Text.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);
                }

            }
        });












        /*



        ///////////////////////Text recognition//////////////////////////////////////
        captureImageButton = findViewById(R.id.capture_image);
        detectText = findViewById(R.id.detect_text);
        imageView = findViewById(R.id.image_view);
        textView = findViewById(R.id.text_display);

        captureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (checkPermission()){
                   // dispatchTakePictureIntent();
              //  }else{
                   // requestPermission();
              //  }
                doProcess(view);
            }
        });
        
        detectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // detectTextFromImage();

            }
        });

        //check app level permission is granted for Camera
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //grant the permission
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
        }
    }


    public void doProcess(View view) {
        //open the camera => create an Intent object
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        //from bundle, extract the image
        Bitmap bitmap = (Bitmap) bundle.get("data");
        //set image in imageview
        imageView.setImageBitmap(bitmap);
        //process the image
        //1. create a FirebaseVisionImage object from a Bitmap object
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        //2. Get an instance of FirebaseVision
        FirebaseVision firebaseVision = FirebaseVision.getInstance();
        //3. Create an instance of FirebaseVisionTextRecognizer
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();
        //4. Create a task to process the image
        Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);
        //5. if task is success
        task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                String s = firebaseVisionText.getText();
                textView.setText(s);
            }
        });
        //6. if task is failure
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }





         */

    }

/*
    // methods from developers////

        //////////////////METHOD TO ALLOW USER OPEN PHONE CAMERA/////////////////////////
        private void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                //Toast.makeText(Add_Activity.this,"aaaaaaaaaaaaaaa",Toast.LENGTH_SHORT).show();
               startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            //ystartActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            //Toast.makeText(Add_Activity.this,"bbbbbbbbb",Toast.LENGTH_SHORT).show();
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
            String elementText;
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
                             elementText =element.getText();
                            result.append(elementText+" ");
                        }
                        textView.setText(result);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Add_Activity.this,"Error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



 */
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
  //  }

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
                Intent intent = new Intent(Add_Medicine_Activity.this, Profile_Activity.class);
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



