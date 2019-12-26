package com.mvcoder.mnistdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mvcoder.mnistdemo.mnist.MnistTF;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button btRec;
    private TextView tvResult;
    private ImageView ivNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btRec = findViewById(R.id.btRec);
        tvResult = findViewById(R.id.tvPreResult);
        ivNumber = findViewById(R.id.ivNumber);
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.number0);
        //放大20倍图片进行显示
        Matrix matrix = new Matrix();
        matrix.setScale(20, 20);
        Bitmap magBitmap = bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        ivNumber.setImageBitmap(magBitmap);
        btRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recPic(bitmap);
            }
        });
    }

    private void recPic(Bitmap bitmap){
        long loadModelTime = System.currentTimeMillis();
        MnistTF mnistTF = new MnistTF(getAssets(), "file:///android_asset/mnist.pb");
        Log.d(TAG, "load model waste time : " + (System.currentTimeMillis() - loadModelTime) + "ms");
        long startTime = System.currentTimeMillis();
        int[] predicate = mnistTF.predicate(bitmap);
        long wasteTime = System.currentTimeMillis() - startTime;
        if(predicate != null && predicate.length > 0){
            tvResult.setText("识别结果：" + predicate[0] + ",waste time : " + wasteTime);
        }
    }
}
