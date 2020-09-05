package com.kavin.ucrop;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.IOException;

public class MainActivity extends CheckPermissionsActivity {


    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 101;
    private ImageView image;
    private Uri destinationUri;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.image);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == MainActivity.RESULT_OK) {   // 回传成功
            switch (requestCode) {

                case CAMERA_REQUEST_CODE: //从相机拍照返回
                    try {
                        // 裁剪
                        startUCrop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                // 选择请求码
                case GALLERY_REQUEST_CODE: //从相册选择返回
                    // 获取图片
                    try {
                        imageUri = data.getData();
                        if (imageUri != null) {
                            startUCrop();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case UCrop.REQUEST_CROP:  //经过UCrop裁剪后返回
                    // 裁剪照片
                    final Uri croppedUri = UCrop.getOutput(data);
                    try {
                        if (croppedUri != null) {
                            Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(croppedUri));
                            image.setImageBitmap(bit);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case UCrop.RESULT_ERROR:  //经过UCrop裁剪失败后返回
                    final Throwable cropError = UCrop.getError(data);
                    Log.i("RESULT_ERROR", "UCrop_RESULT_ERROR");
                    break;

            }
        }
    }

    /**
     * 打开相册选择
     */
    public void openPhoto(View view) {
        takePhoto();
    }

    /**
     * 打开相机拍照
     */
    public void openCamera(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }


    private void takePhoto() {
        // 跳转到系统的拍照界面
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定照片存储位置为sd卡本目录下
        // 这里设置为固定名字 这样就只会只有一张temp图 如果要所有中间图片都保存可以通过时间或者加其他东西设置图片的名称
        // File.separator为系统自带的分隔符 是一个固定的常量
        String mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";

        // 获取图片所在位置的Uri路径    *****这里为什么这么做参考问题2*****
        //因为不是同一个app进行共享数据所以不能使用下面注释这个
        /*imageUri = Uri.fromFile(new File(mTempPhotoPath));*/

        imageUri = FileProvider.getUriForFile(this,
                getApplicationContext().getPackageName() + ".fileprovider", //与AndroidManifest.xml中provider保持一致
                new File(mTempPhotoPath));

        //下面这句指定调用相机拍照后的照片存储的路径
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, CAMERA_REQUEST_CODE);
    }

    /**
     * 跳转UCropActivity开始裁剪
     */
    private void startUCrop() {
        //裁剪后保存到文件中
        destinationUri = Uri.fromFile(new File(getCacheDir(), "myCroppedImage.jpg"));
        UCrop uCrop = UCrop.of(imageUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //是否能调整裁剪框
        // options.setFreeStyleCropEnabled(true);
        uCrop.withOptions(options);
        uCrop.start(this);
    }

    /**
     * 获得选中相册的图片
     *
     * @param context 上下文
     * @param data    onActivityResult返回的Intent
     * @return bitmap
     */
    public static Bitmap getChoosedImage(Activity context, Intent data) {
        if (data == null) return null;
        Bitmap bm = null;
        ContentResolver cr = context.getContentResolver();
        Uri originalUri = data.getData();
        try {
            bm = MediaStore.Images.Media.getBitmap(cr, originalUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }


}
