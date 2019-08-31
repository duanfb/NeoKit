package com.neo.kit.android;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.neo.baselib.util.MaterialDialogUtils;
import com.neo.kit.BuildConfig;
import com.neo.kit.R;
import com.neo.kit.main.BaseActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author neo.duan
 * @date 2019-08-30 14:23
 * @desc 相机相册调用
 */
public class CameraActivity extends BaseActivity {
    private static final int CAMERA_REQUEST = 0x111;
    private static final int PICK_REQUEST = 0x222;

    @BindView(R.id.iv_img)
    ImageView mIvImage;

    private Uri imageUri;

    public static void start(Context context) {
        Intent starter = new Intent(context, CameraActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_camera;
    }

    @Override
    protected void initTop() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.tv_camera, R.id.tv_album})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_camera:
                new RxPermissions(this).request(Manifest.permission.CAMERA)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                openCamera();
                            } else {
                                // 部分权限未获取
                                MaterialDialogUtils.showSingleButton(mContext, "Storage permission" +
                                        "is needed to import images", null);
                            }
                        });
                break;
            case R.id.tv_album:
                openGallery();
                break;
            default:
                break;
        }
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        //创建File对象，用于存储拍照后的照片
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //从Android7.0系统开始，直接使用本地真实路径的Uri被认为是不安全的，
        //会抛出一个FileUriExposedException异常。
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".FileProvider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    /**
     * 打开Gallery选择图片
     */
    private void openGallery() {
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        //true表示获取权限成功（android6.0以下默认为true）
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);
                    } else {
                        // 部分权限未获取
                        MaterialDialogUtils.showSingleButton(mContext, "Storage permission" +
                                "is needed to import images", null);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    break;
                case PICK_REQUEST:
                    imageUri = data.getData();
                    break;
                default:
                    break;
            }

            Bitmap bitmap = null;
            if ((BuildConfig.APPLICATION_ID + ".FileProvider").equals(imageUri.getHost())) {
                try {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = BitmapFactory.decodeFile(handleImageOnKitKat(imageUri).getPath());
            }

            if (bitmap != null) {
                mIvImage.setImageBitmap(bitmap);
            }
        }
    }

    @TargetApi(19)
    private Uri handleImageOnKitKat(Uri uri) {
        String imagePath = null;
        if (DocumentsContract.isDocumentUri(mContext, uri)) {
            //如果是Document类型的Uri，则通过DocumentId处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.download.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是File类型uri, 直接获取图片路径即可
            imagePath = uri.getPath();
        }
        return Uri.parse(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

}
