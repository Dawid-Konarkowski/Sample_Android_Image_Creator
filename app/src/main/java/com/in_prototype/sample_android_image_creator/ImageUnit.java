package com.in_prototype.sample_android_image_creator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * Created by Dawid Konarkowski on 20.01.2016.
 */

public class ImageUnit {
    static public Bitmap getBitmapFile(String path, int type) {

        if (path != null && path.length() > 3) {
            try {
                final int IMAGE_MAX_SIZE;
                switch (type) {
                    case 0:
                        IMAGE_MAX_SIZE = 1200000;
                        break;
                    case 1:
                        IMAGE_MAX_SIZE = 262144;
                        break;
                    default:
                        IMAGE_MAX_SIZE = 1200000;
                        break;
                }

                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, o);

                int scale = 1;
                while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                        IMAGE_MAX_SIZE) {
                    scale++;
                }


                Bitmap b = null;
                if (scale > 1) {
                    scale--;
                    o = new BitmapFactory.Options();
                    o.inSampleSize = scale;
                    b = BitmapFactory.decodeFile(path, o);

                    int height = b.getHeight();
                    int width = b.getWidth();

                    double y = Math.sqrt(IMAGE_MAX_SIZE
                            / (((double) width) / height));
                    double x = (y / height) * width;

                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                            (int) y, true);
                    b.recycle();
                    b = scaledBitmap;

                    System.gc();
                } else {
                    b = BitmapFactory.decodeFile(path);
                }


                return b;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    static float dp_px(float someDpValue, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        float px = someDpValue * density;
        return px;
    }

    static float px_dp(float somePxValue, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        float dp = somePxValue / density;
        return dp;
    }

    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            Log.e("getViewBitmap", "failed getViewBitmap(" + v + ")", new RuntimeException());
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }


    static File getAppStorage(Context context) {
        String myPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS) + File.separator + context.getString(R.string.app_name);
        File myDir = new File(myPath);
        try {
            myDir.mkdirs();
        } catch (Exception ex) {
            Toast.makeText(context, "error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return myDir;
    }

    static void saveImage(View main_work, Context context) {
        try {
            Bitmap image = ImageUnit.getViewBitmap(main_work);

            String name = "AIC_" + String.format("%tF", new Date());
            File file = new File(
                    ImageUnit.getAppStorage(context),
                    name + ".jpg"
            );

            FileOutputStream out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), name, "");

            Toast.makeText(context, R.string.ok_save, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.error_save, Toast.LENGTH_SHORT).show();
        }
    }
}
