package com.mvcoder.mnistdemo.mnist;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

public class MnistTF {

    private static final String TAG = MnistTF.class.getSimpleName();
    private static final String INPUT_NAME = "inputImage:0";
    private static final String OUTPUT_TENSOR_NAME = "number:0";
    private TensorFlowInferenceInterface tf;

    public MnistTF(AssetManager assetManager, String model){
        tf = new TensorFlowInferenceInterface(assetManager, model);
        Log.d(TAG, "load tensorflow model success");
    }

    public int[] predicate(Bitmap bitmap){
        tf.feed(INPUT_NAME, bitmapToFloatArray(bitmap, 28, 28), 1,  28 * 28);
        //运行tensorflow
        String[] outputNames = new String[] {OUTPUT_TENSOR_NAME};
        tf.run(outputNames);
        ///获取输出节点的输出信息
        int[] outputs = new int[1]; //用于存储模型的输出数据
        tf.fetch(OUTPUT_TENSOR_NAME, outputs);
        return outputs;
    }

    /**
     * 将bitmap转为（按行优先）一个float数组，并且每个像素点都归一化到0~1之间。
     * @param bitmap 输入被测试的bitmap图片
     * @param rx 将图片缩放到指定的大小（列）->28
     * @param ry 将图片缩放到指定的大小（行）->28
     * @return   返回归一化后的一维float数组 ->28*28
     */
    public static float[] bitmapToFloatArray(Bitmap bitmap, int rx, int ry){
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        // 计算缩放比例
        float scaleWidth = ((float) rx) / width;
        float scaleHeight = ((float) ry) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        Log.i(TAG,"bitmap width:"+bitmap.getWidth()+",height:"+bitmap.getHeight());
        Log.i(TAG,"bitmap.getConfig():"+bitmap.getConfig());
        height = bitmap.getHeight();
        width = bitmap.getWidth();
        float[] result = new float[height*width];
        int k = 0;
        //行优先
        for(int j = 0;j < height;j++){
            for (int i = 0;i < width;i++){
                int argb = bitmap.getPixel(i,j);
                int r = Color.red(argb);
                int g = Color.green(argb);
                int b = Color.blue(argb);
                int a = Color.alpha(argb);
                //由于是灰度图，所以r,g,b分量是相等的。
                assert(r==g && g==b);
//                Log.i(TAG,i+","+j+" : argb = "+argb+", a="+a+", r="+r+", g="+g+", b="+b);
                result[k++] = r / 255.0f;
            }
        }
        return result;
    }

}
