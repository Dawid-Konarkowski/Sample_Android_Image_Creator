package com.in_prototype.sample_android_image_creator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Dawid Konarkowski on 20.01.2016.
 */

public class MoveView extends FrameLayout {

    boolean isMenu = true;
    boolean isMenuCut = false;
    MainActivity activity;
    OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View view) {
            showMenu();
        }
    };
    ImageView main_image;
    ImageView move_image;
    FrameLayout move_menu;
    FrameLayout move_menu_cut;
    IcoTextView move_cut;
    IcoTextView move_cut_yes;
    IcoTextView move_cut_no;
    IcoTextView move_trash;
    IcoTextView move_center;
    IcoTextView move_rotate;
    IcoTextView move_size;
    LayoutParams layoutParams;
    float rotation = 0;
    float centerX;
    float centerY;
    Bitmap showImage;
    Bitmap imageTemp;
    Bitmap imageStart;
    Context context_;

    public MoveView(Context context) {
        super(context);
        init(context);
    }

    public MoveView(Context context, MainActivity mainActivity) {
        super(context);
        activity = mainActivity;
        init(context);
    }

    public MoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public static Bitmap makeTransparent(Bitmap bit, int transparentColor, int amplitude) {
        int width = bit.getWidth();
        int height = bit.getHeight();
        Bitmap myBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] allpixels = new int[myBitmap.getHeight() * myBitmap.getWidth()];
        bit.getPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        myBitmap.setPixels(allpixels, 0, width, 0, 0, width, height);

        int //A,
                R, G, B;

        int //a = Color.alpha(transparentColor),
                r = Color.red(transparentColor),
                g = Color.green(transparentColor),
                b = Color.blue(transparentColor);

        for (int i = 0; i < myBitmap.getHeight() * myBitmap.getWidth(); i++) {

            //A = Color.alpha(allpixels[i]);
            R = Color.red(allpixels[i]);
            G = Color.green(allpixels[i]);
            B = Color.blue(allpixels[i]);

            if (allpixels[i] == transparentColor
                    || (
                    //(a-amplitude<=A && a+amplitude>=A)
                    (r - amplitude <= R && r + amplitude >= R)
                            && (g - amplitude <= G && g + amplitude >= G)
                            && (b - amplitude <= B && b + amplitude >= B)
            )
                    )

                allpixels[i] = Color.alpha(Color.TRANSPARENT);
        }

        myBitmap.setPixels(allpixels, 0, myBitmap.getWidth(), 0, 0, myBitmap.getWidth(), myBitmap.getHeight());
        return myBitmap;
    }

    void init(Context context) {
        context_ = context;

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        move_image = new ImageView(context);
        move_image.setLayoutParams(lp);
        move_image.setImageBitmap(showImage);
        addView(move_image);

        move_menu = new FrameLayout(context);
        move_menu.setLayoutParams(lp);
        addView(move_menu);

        move_menu_cut = new FrameLayout(context);
        move_menu_cut.setLayoutParams(lp);
        move_menu_cut.setVisibility(GONE);
        addView(move_menu_cut);

        lp = new LayoutParams((int) ImageUnit.dp_px(40, context), (int) ImageUnit.dp_px(40, context));
        lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
        move_size = new IcoTextView(context);
        move_size.setTextTypface("icomoon");
        move_size.setTextSize((int) ImageUnit.dp_px(15, context));
        move_size.setGravity(Gravity.CENTER);
        move_size.setText("");
        move_size.setTextColor(0xffffffff);
        move_size.setLayoutParams(lp);
        move_size.setBackgroundColor(0x66000000);
        move_menu.addView(move_size);

        lp = new LayoutParams((int) ImageUnit.dp_px(40, context), (int) ImageUnit.dp_px(40, context));
        lp.gravity = Gravity.CENTER;
        move_center = new IcoTextView(context);
        move_center.setTextTypface("icomoon");
        move_center.setTextSize((int) ImageUnit.dp_px(15, context));
        move_center.setGravity(Gravity.CENTER);
        move_center.setText("");
        move_center.setTextColor(0xffffffff);
        move_center.setLayoutParams(lp);
        move_center.setBackgroundColor(0x66000000);
        move_menu.addView(move_center);

        lp = new LayoutParams((int) ImageUnit.dp_px(40, context), (int) ImageUnit.dp_px(40, context));
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        move_rotate = new IcoTextView(context);
        move_rotate.setTextTypface("icomoon");
        move_rotate.setTextSize((int) ImageUnit.dp_px(15, context));
        move_rotate.setGravity(Gravity.CENTER);
        move_rotate.setText("");
        move_rotate.setTextColor(0xffffffff);
        move_rotate.setLayoutParams(lp);
        move_rotate.setBackgroundColor(0x66000000);
        move_menu.addView(move_rotate);

        lp = new LayoutParams((int) ImageUnit.dp_px(40, context), (int) ImageUnit.dp_px(40, context));
        move_cut = new IcoTextView(context);
        move_cut.setTextTypface("icomoon");
        move_cut.setTextSize((int) ImageUnit.dp_px(15, context));
        move_cut.setGravity(Gravity.CENTER);
        move_cut.setText("");
        move_cut.setTextColor(0xffffffff);
        move_cut.setLayoutParams(lp);
        move_cut.setBackgroundColor(0x66000000);
        move_menu.addView(move_cut);

        lp = new LayoutParams((int) ImageUnit.dp_px(40, context), (int) ImageUnit.dp_px(40, context));
        lp.gravity = Gravity.RIGHT;
        move_trash = new IcoTextView(context);
        move_trash.setTextTypface("icomoon");
        move_trash.setTextSize((int) ImageUnit.dp_px(15, context));
        move_trash.setGravity(Gravity.CENTER);
        move_trash.setText("");
        move_trash.setTextColor(0xffffffff);
        move_trash.setLayoutParams(lp);
        move_trash.setBackgroundColor(0x66000000);
        move_menu.addView(move_trash);

        lp = new LayoutParams((int) ImageUnit.dp_px(40, context), (int) ImageUnit.dp_px(40, context));
        move_cut_no = new IcoTextView(context);
        move_cut_no.setTextTypface("icomoon");
        move_cut_no.setTextSize((int) ImageUnit.dp_px(15, context));
        move_cut_no.setGravity(Gravity.CENTER);
        move_cut_no.setText("");
        move_cut_no.setTextColor(0xffffffff);
        move_cut_no.setLayoutParams(lp);
        move_cut_no.setBackgroundColor(0x66ff0000);
        move_menu_cut.addView(move_cut_no);

        lp = new LayoutParams((int) ImageUnit.dp_px(40, context), (int) ImageUnit.dp_px(40, context));
        lp.leftMargin = (int) ImageUnit.dp_px(40, context);
        move_cut_yes = new IcoTextView(context);
        move_cut_yes.setTextTypface("icomoon");
        move_cut_yes.setTextSize((int) ImageUnit.dp_px(15, context));
        move_cut_yes.setGravity(Gravity.CENTER);
        move_cut_yes.setText("");
        move_cut_yes.setTextColor(0xffffffff);
        move_cut_yes.setLayoutParams(lp);
        move_cut_yes.setBackgroundColor(0x6600ff00);
        move_menu_cut.addView(move_cut_yes);



        setOnClickListener(click);

        move_size.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    layoutParams = (LayoutParams) getLayoutParams();
                    centerX = (float) (layoutParams.leftMargin + getMeasuredWidth() / 2.0);
                    centerY = (float) (layoutParams.topMargin + getMeasuredHeight() / 2.0);
                }

                float alpha = direction(centerX, centerY + 120, motionEvent.getRawX(), motionEvent.getRawY());
                float rotate = (alpha + rotation) % 360;
                float vector = vector(centerX, centerY + 120, motionEvent.getRawX(), motionEvent.getRawY());
                float height = (float) (Math.cos(Math.toRadians(rotate)) * vector * 2 + move_size.getMeasuredWidth() / 2.0);
                float width = (float) (Math.sin(Math.toRadians(rotate)) * vector * 2 + move_size.getMeasuredHeight() / 2.0);
                if (width < ImageUnit.dp_px(150, context_)) width = ImageUnit.dp_px(150, context_);
                if (height < ImageUnit.dp_px(150, context_))
                    height = ImageUnit.dp_px(150, context_);

                resize(centerX, centerY, width, height);

                return true;
            }
        });


        move_center.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    layoutParams = (LayoutParams) getLayoutParams();
                }

                layoutParams.leftMargin = (int) (motionEvent.getRawX() - getWidth() / 2.0);
                layoutParams.topMargin = (int) (motionEvent.getRawY() - getHeight() / 2.0 - 120);

                setLayoutParams(layoutParams);

                return true;
            }
        });


        move_rotate.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    layoutParams = (LayoutParams) getLayoutParams();
                    centerX = (float) (layoutParams.leftMargin + getMeasuredWidth() / 2.0);
                    centerY = (float) (layoutParams.topMargin + getMeasuredHeight() / 2.0 + 120);
                }

                float alpha = direction(centerX, centerY, motionEvent.getRawX(), motionEvent.getRawY());
                float rotate = 360 - (alpha + 180) % 360;
                rotation = rotate;

                setRotation(rotation);

                return true;
            }
        });

        move_cut.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imageStart = showImage;
                showMenuCut();
                return true;
            }
        });

        move_cut_no.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                imageTemp = null;
                move_image.setImageBitmap(showImage);
                showMenuCut();
            }
        });

        move_cut_yes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showImage = imageTemp;
                imageTemp = null;
                move_image.setImageBitmap(showImage);
                showMenuCut();
            }
        });

        move_trash.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.removeItem(MoveView.this);
            }
        });

        move_menu_cut.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Bitmap viewBitmap = ImageUnit.getViewBitmap(MoveView.this);
                try {
                    if (viewBitmap != null) {
                        //Log.e("position", String.format("x = %8.2f | y = %8.2f", motionEvent.getX(), motionEvent.getY()));
                        int pixel = viewBitmap.getPixel((int) motionEvent.getX(), (int) motionEvent.getY());
                        //removeColor(imageTemp!=null?imageTemp:showImage,pixel);
                        imageTemp = makeTransparent(imageTemp != null ? imageTemp : imageStart, pixel, (int) (255 * 0.01));
                        //changeColor(imageTemp!=null?imageTemp:showImage,pixel+0xff000000,0x00000000);
                        move_image.setImageBitmap(imageTemp);
                    }
                } catch (Exception e) {
                }
                return true;
            }
        });

    }

    public void setImage(Bitmap bitmap) {
        showImage = bitmap;
        move_image.setImageBitmap(showImage);

        layoutParams = (LayoutParams) getLayoutParams();

        centerX = (float) (layoutParams.leftMargin + layoutParams.width / 2.0);
        centerY = (float) (layoutParams.topMargin + layoutParams.height / 2.0);

        resize(centerX, centerY + 120, layoutParams.width, layoutParams.height);
    }

    float vector(float xA, float yA, float xB, float yB) {
        float a, b = 0;
        a = xB - xA;
        b = yB - yA;

        return (float) Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }


    void resize(float centerX, float centerY, float wight, float height) {

        int w = showImage.getWidth();
        int h = showImage.getHeight();

        float resizeW = wight / w;
        float resizeH = height / h;

        float resize = Math.min(resizeW, resizeH);

        wight = resize * w;
        height = resize * h;

        layoutParams.leftMargin = (int) (centerX - (wight / 2.0));
        layoutParams.topMargin = (int) (centerY - (height / 2.0));
        layoutParams.height = (int) height;
        layoutParams.width = (int) wight;
        setLayoutParams(layoutParams);
    }


    float direction(float xA, float yA, float xB, float yB) {


        float beta = 0;
        float a, b = 0;

        a = xB - xA;
        b = yB - yA;

        beta = (float) (Math.atan2(a, b) * 180.0 / Math.PI);
        if (beta < 0.0)
            beta += 360.0;
        else if (beta > 360.0)
            beta -= 360;

        return beta;
    }

    void showMenu() {
        if (isMenu) {
            isMenu = false;
            move_menu.setVisibility(GONE);
        } else {
            if (activity != null)
                for (MoveView object : activity.items) {
                    object.hideMenu();
                }
            isMenu = true;
            move_menu.setVisibility(VISIBLE);
            activity.setItemTop(this);
        }
    }

    void hideMenu() {
        if (isMenu) {
            isMenu = false;
            move_menu.setVisibility(GONE);
        }
        if (isMenuCut) {
            isMenuCut = false;
            move_menu_cut.setVisibility(GONE);
        }
    }

    void showMenuCut() {
        if (isMenuCut) {
            isMenuCut = false;
            move_menu_cut.setVisibility(GONE);
            showMenu();
        } else {
            showMenu();
            isMenuCut = true;
            move_menu_cut.setVisibility(VISIBLE);
        }
    }


    ////

    @Override
    public Parcelable onSaveInstanceState() {

        Log.e("MoveView", "onSaveInstanceState");

        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putFloat("rotation", rotation);

        layoutParams = (LayoutParams) getLayoutParams();
        bundle.putInt("leftMargin", layoutParams.leftMargin);
        bundle.putInt("topMargin", layoutParams.topMargin);
        bundle.putInt("height", layoutParams.height);
        bundle.putInt("width", layoutParams.width);
        bundle.putParcelable("image", showImage);
        // ... save everything
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        Log.e("MoveView", "onRestoreInstanceState");

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            layoutParams = (LayoutParams) getLayoutParams();
            layoutParams.leftMargin = bundle.getInt("leftMargin");
            layoutParams.topMargin = bundle.getInt("topMargin");
            layoutParams.height = bundle.getInt("height");
            layoutParams.width = bundle.getInt("width");

            setLayoutParams(layoutParams);

            rotation = bundle.getFloat("rotation");
            setRotation(rotation);

            showImage = bundle.getParcelable("image");
            move_image.setImageBitmap(showImage);
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }


}
