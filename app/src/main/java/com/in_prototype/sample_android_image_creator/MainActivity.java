package com.in_prototype.sample_android_image_creator;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Dawid Konarkowski on 20.01.2016.
 */

public class MainActivity extends ActionBarActivity {

    static int RESULT_LOAD_IMAGE = 8798;
    View decorView;
    ArrayList<MoveView> items = new ArrayList<>();
    FrameLayout main_work;
    LinearLayout main_layers;

    void openGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    void removeItem(MoveView item) {
        main_work.removeView(item);
        main_layers.removeView(item.main_image);
        items.remove(item);
    }

    void removeAllItems() {
        for (MoveView item : items) {
            main_work.removeView(item);
            main_layers.removeView(item.main_image);
        }
        items.clear();
    }

    void setItemTop(MoveView item) {
        main_work.removeView(item);
        main_work.addView(item);

        main_layers.removeView(item.main_image);
        main_layers.addView(item.main_image, 0);
    }

    void hideAll() {
        for (MoveView object : items) {
            object.hideMenu();
        }
    }

    void addImage(String picturePath) {
        Bitmap bmp = ImageUnit.getBitmapFile(picturePath, 1);
        final MoveView item = new MoveView(this, this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int) ImageUnit.dp_px(200, this), (int) ImageUnit.dp_px(200, this));
        item.setLayoutParams(lp);
        item.setImage(bmp);
        main_work.addView(item);
        for (MoveView object : items) {
            object.hideMenu();
        }
        items.add(item);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams((int) ImageUnit.dp_px(60, this), (int) ImageUnit.dp_px(60, this));
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(lp2);
        imageView.setImageBitmap(bmp);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.showMenu();
            }
        });

        item.main_image = imageView;

        main_layers.addView(imageView, 0);

    }

    //======== Init Activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        decorView = w.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_main);
        main_layers = (LinearLayout) findViewById(R.id.main_layers);
        main_work = (FrameLayout) findViewById(R.id.main_work);
        findViewById(R.id.main_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        findViewById(R.id.main_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageUnit.saveImage(main_work, MainActivity.this);
            }
        });
        main_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAll();
            }
        });

        Log.e("onCreate", "======");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            if (picturePath != null) addImage(picturePath);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.e("onSaveInstanceState", savedInstanceState != null ? "1" : "0");

        ArrayList<Parcelable> array = new ArrayList<>();
        for (MoveView object : items) {
            array.add(object.onSaveInstanceState());
        }
        savedInstanceState.putParcelableArrayList("items", array);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.e("onRestoreInstanceState", savedInstanceState != null ? "1" : "0");
        super.onRestoreInstanceState(savedInstanceState);

        ArrayList<Parcelable> array = savedInstanceState.getParcelableArrayList("items");
        for (Parcelable object : array) {
            MoveView item = new MoveView(this, this);
            item.onRestoreInstanceState(object);
            items.add(item);
        }
    }






}
