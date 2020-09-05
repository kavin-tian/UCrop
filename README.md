# UCrop



![readme.gif](.\readme.gif)



Android裁剪图片（UCrop）使用说明



Android如何打开拍照 打开系统相册参考：
http://blog.csdn.net/weixin_37577039/article/details/79186183

使用的是UCrop的一个开源库
参考地址：https://github.com/Yalantis/uCrop

大致流程：
1 project gradle中
```

allprojects {
   repositories {
      jcenter()
      maven { url "https://jitpack.io" }
   }
}
```

2 app gradle中添加依赖
```

compile 'com.github.yalantis:ucrop:2.2.1'
```
1
最新版本参考github

3 权限配置

```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/> 
<uses-permission
 android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
```

4 manifest.xml中配置

<activity
    android:name="com.yalantis.ucrop.UCropActivity"
    android:screenOrientation="portrait"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar"/>

说明：
```
android:screenOrientation=”landscape”是限制此页面横屏显示，
android:screenOrientation=”portrait”是限制此页面数竖屏显示。
```

5 在拍照后的回传中设置

```
@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        fragment4ImageView0 = findViewById(R.id.fragment4ImageView0);
        if (resultCode == MainActivity.RESULT_OK) {   // 回传成功
                switch (requestCode) {                // 选择请求码
                case GlobalVariable.CAMERA_REQUEST_CODE: {
                    try {
                        // 裁剪
                        startUCrop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
```

6 在相册选择后的回传中设置

```
case GlobalVariable.GALLERY_REQUEST_CODE: {
                    // 获取图片
                    try {
                        imageUri = data.getData();
                        if(imageUri!=null) {
                          startUCrop();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
```

7 设置Ucrop方法

```
 private void startUCrop(){
        //裁剪后保存到文件中
         destinationUri = Uri.fromFile(new File(getCacheDir(), "myCroppedImage.jpg"));
        UCrop uCrop = UCrop.of(imageUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.orange2));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.orange2));
        //是否能调整裁剪框
        // options.setFreeStyleCropEnabled(true);
        uCrop.withOptions(options);
        uCrop.start(this);
    }
```

UCrop.of(imageUri, destinationUri) 原图片URI地址 生成图片的URI地址
要能自定义调节裁剪框大小，options.setFreeStyleCropEnabled(true);
destinationUri 地址为：file:///data/user/0/包名/cache/myCroppedImage.jpg

8 设置UCrop回传 处理裁剪后的图片

```
case UCrop.REQUEST_CROP: {
 // 裁剪照片
   final Uri croppedUri = UCrop.getOutput(data);
   try {
       if(croppedUri!=null) {
           Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(croppedUri));
           fragment4ImageView0.setImageBitmap(bit);
       }
   } catch (Exception e) {
       e.printStackTrace();
   }
   break;
}

case UCrop.RESULT_ERROR: {
   final Throwable cropError = UCrop.getError(data);
   Log.i("RESULT_ERROR","UCrop_RESULT_ERROR");
   break;
}
```

[1599295153554.gif](.\1599295153554.gif)

Android裁剪图片（UCrop）使用说明



Android如何打开拍照 打开系统相册参考：
http://blog.csdn.net/weixin_37577039/article/details/79186183

使用的是UCrop的一个开源库
参考地址：https://github.com/Yalantis/uCrop

大致流程：
1 project gradle中
```

allprojects {
   repositories {
      jcenter()
      maven { url "https://jitpack.io" }
   }
}
```

2 app gradle中添加依赖
```

compile 'com.github.yalantis:ucrop:2.2.1'
```
1
最新版本参考github

3 权限配置

```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/> 
<uses-permission
 android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
```

4 manifest.xml中配置

<activity
    android:name="com.yalantis.ucrop.UCropActivity"
    android:screenOrientation="portrait"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar"/>

说明：
```
android:screenOrientation=”landscape”是限制此页面横屏显示，
android:screenOrientation=”portrait”是限制此页面数竖屏显示。
```

5 在拍照后的回传中设置

```
@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        fragment4ImageView0 = findViewById(R.id.fragment4ImageView0);
        if (resultCode == MainActivity.RESULT_OK) {   // 回传成功
                switch (requestCode) {                // 选择请求码
                case GlobalVariable.CAMERA_REQUEST_CODE: {
                    try {
                        // 裁剪
                        startUCrop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
```

6 在相册选择后的回传中设置

```
case GlobalVariable.GALLERY_REQUEST_CODE: {
                    // 获取图片
                    try {
                        imageUri = data.getData();
                        if(imageUri!=null) {
                          startUCrop();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
```

7 设置Ucrop方法

```
 private void startUCrop(){
        //裁剪后保存到文件中
         destinationUri = Uri.fromFile(new File(getCacheDir(), "myCroppedImage.jpg"));
        UCrop uCrop = UCrop.of(imageUri, destinationUri);
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.orange2));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.orange2));
        //是否能调整裁剪框
        // options.setFreeStyleCropEnabled(true);
        uCrop.withOptions(options);
        uCrop.start(this);
    }
```

UCrop.of(imageUri, destinationUri) 原图片URI地址 生成图片的URI地址
要能自定义调节裁剪框大小，options.setFreeStyleCropEnabled(true);
destinationUri 地址为：file:///data/user/0/包名/cache/myCroppedImage.jpg

8 设置UCrop回传 处理裁剪后的图片

```
case UCrop.REQUEST_CROP: {
 // 裁剪照片
   final Uri croppedUri = UCrop.getOutput(data);
   try {
       if(croppedUri!=null) {
           Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(croppedUri));
           fragment4ImageView0.setImageBitmap(bit);
       }
   } catch (Exception e) {
       e.printStackTrace();
   }
   break;
}

case UCrop.RESULT_ERROR: {
   final Throwable cropError = UCrop.getError(data);
   Log.i("RESULT_ERROR","UCrop_RESULT_ERROR");
   break;
}
```

