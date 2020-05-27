package com.neo.baselib.glide;

//import android.content.Context;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.Priority;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
//
//import hik.business.fp.ccrphone.R;
//import hik.business.fp.ccrphone.main.ui.widget.GlideRoundTransform;


/**
 * @author neo.duan
 * @date 2020/04/02 3:59 PM
 * @desc Glide图片显示工具类
 */
//public class ImageLoaderUtils {
//
//    public static void loadImage(Context context, String url, ImageView imageView) {
//        loadImage(context, url, R.mipmap.ic_fp_placeholder, imageView);
//    }
//
//    public static void loadImage(Context context, String url, int placeholder, ImageView imageView) {
//        loadImage(context, url, 0, placeholder, imageView);
//    }
//
//    public static void loadImage(Context context, String url, int radius, int placeholder, ImageView imageView) {
//        RequestOptions options = new RequestOptions()
//                .priority(Priority.HIGH)
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
//
//        if (placeholder > 0) {
//            options = options.placeholder(placeholder);
//            options = options.error(placeholder);
//        }
//
//        if (radius > 0) {
//            options = options.transform(new GlideRoundTransform(radius));
//        }
//        Glide.with(context)
//                .applyDefaultRequestOptions(options)
//                .load(url)
//                .into(imageView);
//    }
//}
