package com.example.newcam;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;

import java.util.List;

import static android.content.Context.CAMERA_SERVICE;

public class MainActivity extends AppCompatActivity {
    Button cam;
    private final static int REQUEST_IMAGE_CAPTURE=124;
    //String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String result="";
        cam=findViewById(R.id.cameraButton);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
                }else{
                    Toast.makeText(MainActivity.this,"Cant Connect To Camera",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK){
            Bundle extra = data.getExtras();
            Bitmap bitmap = (Bitmap)extra.get("data");
            InputImage image = InputImage.fromBitmap(bitmap, 0);
            detectFace(image);
            //FaceDetector detector = FaceDetection.getClient();
            
        }
    }

    private void detectFace(InputImage image) {
        FaceDetector detector = FaceDetection.getClient();
        Task<List<Face>>result=detector.process(image).addOnSuccessListener(new OnSuccessListener<List<Face>>() {
            @Override
            public void onSuccess(@NonNull List<Face> faces) {
                String str="Number Of Faces in Image: ";
                String str2="  Smile Probability: ";
                int i=0;
                for(Face face:faces){
                    i++;
                    //str2+=String.valueOf(face.getSmilingProbability()*100);
                }
                str+=String.valueOf(i);
                //str+=str2;
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error in ML",Toast.LENGTH_LONG).show();
            }
        });
    }

}
