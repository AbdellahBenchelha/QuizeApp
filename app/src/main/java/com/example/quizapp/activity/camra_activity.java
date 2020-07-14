package com.example.quizapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class camra_activity extends AppCompatActivity {
    ImageView imageView;
    Bitmap eyePatchBitmap;
    Bitmap flowerLine;
    Canvas canvas;
    Bitmap tempBitmap;
    Bitmap bitmap;
    Paint rectPaint = new Paint();
    ProgressBar progressScan;
    TextView txtmessage;
    Button btncamera;
    Button btntStart;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camra_activity);

        btncamera= (Button)findViewById(R.id.btntake);
        btntStart= (Button)findViewById(R.id.btnstart);
        imageView = (ImageView)findViewById(R.id.imageView);
        progressScan=findViewById(R.id.progressScan);
        txtmessage=findViewById(R.id.tv_message);


        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        databaseReference= FirebaseDatabase.getInstance().getReference("uploads");

        rectPaint.setStrokeWidth(5);
        rectPaint.setColor(Color.WHITE);
        rectPaint.setStyle(Paint.Style.STROKE);


        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
            }
        });
        btntStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(camra_activity.this, QuizActivity.class));
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressScan.setVisibility(View.VISIBLE);
        filePath=data.getData();
         bitmap = (Bitmap)data.getExtras().get("data");

        if(filePath==null)
        {

              Log.e("null","nuu");
        }

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        imageView.setImageBitmap(bitmap);

        tempBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.RGB_565);
        canvas  = new Canvas(tempBitmap);
        canvas.drawBitmap(bitmap,0,0,null);
        scanFace();
    }
    @SuppressLint("ResourceAsColor")
    private  void scanFace()
    {
        FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        if(!faceDetector.isOperational())
        {
            Toast.makeText(camra_activity.this, "Face Detector could not be set up on your device", Toast.LENGTH_SHORT).show();
            return;
        }
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<Face> sparseArray = faceDetector.detect(frame);
        int cpt=0;
        for(int i=0;i<sparseArray.size();i++)
        {
            Face face = sparseArray.valueAt(i);
            float x1=face.getPosition().x;
            float y1 =face.getPosition().y;
            float x2 = x1+face.getWidth();
            float y2=y1+face.getHeight();
            RectF rectF = new RectF(x1,y1,x2,y2);
            cpt++;
            canvas.drawRoundRect(rectF,2,2,rectPaint);




        }
        if(cpt==0)
        {
            txtmessage.setTextColor(getResources().getColor(R.color.color_message_red));
            txtmessage.setText("No face detect in the image");
        }else
        {
            txtmessage.setTextColor(getResources().getColor(R.color.color_message_green));
            txtmessage.setText("The face was successfully detected");
            btncamera.setText("Change Image");
            btntStart.setVisibility(View.VISIBLE);
        }
        imageView.setImageDrawable(new BitmapDrawable(getResources(),tempBitmap));
        progressScan.setVisibility(View.INVISIBLE);

    }
    private String getFileExtension(Uri uri)
    {
        return  ".jpg";
    }
    private void uploadImage()
    {
        if(filePath!=null)
        {


        }else
        {
            Toast.makeText(camra_activity.this,"No image detected",Toast.LENGTH_LONG).show();
        }
    }



}
