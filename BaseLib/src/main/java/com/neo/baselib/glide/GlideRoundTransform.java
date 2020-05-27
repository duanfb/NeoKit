package com.neo.baselib.glide;

//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapShader;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.RectF;
//
//import androidx.annotation.NonNull;
//
//import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
//import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
//import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
//import com.bumptech.glide.util.Util;
//
//import java.nio.ByteBuffer;
//import java.security.MessageDigest;

/**
 * @author neo.duan
 * @date 2020/3/25
 * @desc Glide 圆角 Transform
 */
//public class GlideRoundTransform extends BitmapTransformation {
//
//    private final float radius;
//    private final String ID = "com. bumptech.glide.transformations.FillSpace";
//    private final byte[] ID_ByTES = ID.getBytes(CHARSET);
//
//    /**
//     * 构造函数 默认圆角半径 4dp
//     */
//    public GlideRoundTransform() {
//        this(3);
//    }
//
//    public GlideRoundTransform(int dp) {
//        super();
//        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
//    }
//
//    @Override
//    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
//        Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
//        return roundCrop(pool, bitmap);
//    }
//
//    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
//        if (source == null) {
//            return null;
//        }
//        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
//        if (result == null) {
//            return Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
//        }
//
//        Canvas canvas = new Canvas(result);
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
//        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
//        canvas.drawRoundRect(rectF, radius, radius, paint);
//
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (o instanceof GlideRoundTransform) {
//            GlideRoundTransform other = (GlideRoundTransform) o;
//            return radius == other.radius;
//        }
//        return false;
//    }
//
//    @Override
//    public int hashCode() {
//        return Util.hashCode(ID.hashCode(),
//                Util.hashCode(radius));
//    }
//
//    @Override
//    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
//        messageDigest.update(ID_ByTES);
//        byte[] radiusData = ByteBuffer.allocate(4).putInt((int) radius).array();
//        messageDigest.update(radiusData);
//    }
//}
