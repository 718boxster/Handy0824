package com.handytrip.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class AutoLayout {
    private static int disPlayWidth = 0;
    private static int disPlayHeight = 0;

    public static Typeface fontM = null;
    public static Typeface fontBold= null;
    public static Typeface fontL = null;
    public static Typeface fontNumber = null;
    public static Typeface fontMyNumber = null;

    public static int getDisPlayWidth(){
        return disPlayWidth;
    }
    public static void setResizeView(Context mContext) {
        AppCompatActivity activity = (AppCompatActivity)mContext;
        if(disPlayWidth == 0){
            disPlayWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            disPlayHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        }

//        if(fontM == null){
//            fontM = Typeface.createFromAsset(mContext.getAssets(), "rixgom.ttf");
//            fontBold = Typeface.createFromAsset(mContext.getAssets(), "rixgob.ttf");
//            fontL = Typeface.createFromAsset(mContext.getAssets(), "rixgol.ttf");
//            fontNumber = Typeface.createFromAsset(mContext.getAssets(), "number.otf");
//            fontMyNumber = Typeface.createFromAsset(mContext.getAssets(), "engschrift.ttf");
//        }

        final ViewGroup tempView = (ViewGroup) ((ViewGroup)  activity
                .findViewById(android.R.id.content)).getChildAt(0);

        setByWidth(tempView);
        if (tempView instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) tempView).getChildCount(); i++) {
                View child = ((ViewGroup) tempView).getChildAt(i);
                setView(child);
            }
        }
    }

    private static void setFont(View tempView){
        if (tempView instanceof TextView) {
            if(tempView.getTag() == null){
                ((TextView) tempView).setTypeface(fontBold);
            }else if(tempView.getTag().toString().equals("M")){
                ((TextView) tempView).setTypeface(fontM);
            }else if(tempView.getTag().toString().equals("B")){
                ((TextView) tempView).setTypeface(Typeface.DEFAULT_BOLD);
            }else if(tempView.getTag().toString().equals("L")){
                ((TextView) tempView).setTypeface(fontL);
            }else{
                ((TextView) tempView).setTypeface(fontBold);
            }
        }else if (tempView instanceof Button) {
            if(tempView.getTag() == null){
                ((Button) tempView).setTypeface(fontBold);
            }else if(tempView.getTag().toString().equals("M")){
                ((Button) tempView).setTypeface(fontM);
            }else if(tempView.getTag().toString().equals("B")){
                ((Button) tempView).setTypeface(fontBold);
            }else if(tempView.getTag().toString().equals("L")){
                ((Button) tempView).setTypeface(fontL);
            }else{
                ((Button) tempView).setTypeface(fontBold);
            }
        }else if(tempView instanceof EditText) {
            if(tempView.getTag() == null){
                ((EditText) tempView).setTypeface(fontBold);
            }else if(tempView.getTag().toString().equals("M")){
                ((EditText) tempView).setTypeface(fontM);
            }else if(tempView.getTag().toString().equals("B")){
                ((EditText) tempView).setTypeface(fontBold);
            }else if(tempView.getTag().toString().equals("L")){
                ((EditText) tempView).setTypeface(fontL);
            }else{
                ((EditText) tempView).setTypeface(fontBold);
            }
        }
    }

    public static void setView(View tempView) {
        setByWidth(tempView);
        setFont(tempView);
        if (tempView instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) tempView).getChildCount(); i++) {
                View child = ((ViewGroup) tempView).getChildAt(i);
                setView(child);
            }
        }
    }

    private void set(View tempView) {
        ViewGroup.LayoutParams tempParam = tempView
                .getLayoutParams();

        if (tempParam instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams LParam = (LinearLayout.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calHeight(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calHeight(LParam.topMargin);
            LParam.bottomMargin = calHeight(LParam.bottomMargin);

        } else if (tempParam instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams LParam = (RelativeLayout.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calHeight(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calHeight(LParam.topMargin);
            LParam.bottomMargin = calHeight(LParam.bottomMargin);

        } else if (tempParam instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams LParam = (FrameLayout.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calHeight(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calHeight(LParam.topMargin);
            LParam.bottomMargin = calHeight(LParam.bottomMargin);

        } else if (tempParam instanceof ViewPager.LayoutParams) {
            ViewPager.LayoutParams LParam = (ViewPager.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calHeight(LParam.height);
        } else if (tempParam instanceof AbsListView.LayoutParams) {
            AbsListView.LayoutParams LParam = (AbsListView.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calHeight(LParam.height);
        } else if (tempParam instanceof ActionBar.LayoutParams) {
            ActionBar.LayoutParams LParam = (ActionBar.LayoutParams) tempParam;
            // LParam.width = calWidth(LParam.width);
            // LParam.height = calHeight(LParam.height);
            // LParam.leftMargin = calWidth(LParam.leftMargin);
            // LParam.rightMargin = calWidth(LParam.rightMargin);
            // LParam.topMargin = calHeight(LParam.topMargin);
            // LParam.bottomMargin = calHeight(LParam.bottomMargin);
            Log.e("test", "ActionBar no resize functions");
        } else if (tempParam instanceof ViewGroup.LayoutParams) {
            ViewGroup.LayoutParams LParam = (ViewGroup.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calHeight(LParam.height);
        } else if (tempParam instanceof WindowManager.LayoutParams) {
            WindowManager.LayoutParams LParam = (WindowManager.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calHeight(LParam.height);
        } else if (tempParam instanceof GridLayout.LayoutParams) {
            GridLayout.LayoutParams LParam = (GridLayout.LayoutParams) tempParam;
            // LParam.width = calWidth(LParam.width);
            // LParam.height = calHeight(LParam.height);
            // LParam.leftMargin = calWidth(LParam.leftMargin);
            // LParam.rightMargin = calWidth(LParam.rightMargin);
            // LParam.topMargin = calHeight(LParam.topMargin);
            // LParam.bottomMargin = calHeight(LParam.bottomMargin);
            Log.e("test", "GridLayout no resize functions");
        } else if (tempParam instanceof RadioGroup.LayoutParams) {
            RadioGroup.LayoutParams LParam = (RadioGroup.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calHeight(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calHeight(LParam.topMargin);
            LParam.bottomMargin = calHeight(LParam.bottomMargin);
        } else if (tempParam instanceof TableLayout.LayoutParams) {
            TableLayout.LayoutParams LParam = (TableLayout.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calHeight(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calHeight(LParam.topMargin);
            LParam.bottomMargin = calHeight(LParam.bottomMargin);
        } else if (tempParam instanceof TableRow.LayoutParams) {
            TableRow.LayoutParams LParam = (TableRow.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calHeight(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calHeight(LParam.topMargin);
            LParam.bottomMargin = calHeight(LParam.bottomMargin);
        } else if (tempParam == null) {
            Log.e("test", "param is null !!");
        } else {
            Log.e("test", "no resize functions !!");
        }

        int left = calWidth(tempView.getPaddingLeft());
        int right = calWidth(tempView.getPaddingRight());
        int top = calHeight(tempView.getPaddingTop());
        int bottom = calHeight(tempView.getPaddingBottom());
        tempView.setPadding(left, top, right, bottom);
        if (tempView instanceof TextView) {
            TextView textview = (TextView) tempView;
            textview.setTextSize(0, calHeight((int) textview.getTextSize()));
        }
    }

    public void setView1080(View tempView) {
        set1080(tempView);
        if (tempView instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) tempView).getChildCount(); i++) {
                View child = ((ViewGroup) tempView).getChildAt(i);
                setView1080(child);
            }
        }
    }

    private void set1080(View tempView) {
//		setFont(tempView);
        ViewGroup.LayoutParams tempParam = tempView
                .getLayoutParams();

        if (tempParam instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams LParam = (LinearLayout.LayoutParams) tempParam;
            LParam.width = calWidth1080(LParam.width);
            LParam.height = calHeight1080(LParam.height);
            LParam.leftMargin = calWidth1080(LParam.leftMargin);
            LParam.rightMargin = calWidth1080(LParam.rightMargin);
            LParam.topMargin = calHeight1080(LParam.topMargin);
            LParam.bottomMargin = calHeight1080(LParam.bottomMargin);

        } else if (tempParam instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams LParam = (RelativeLayout.LayoutParams) tempParam;
            LParam.width = calWidth1080(LParam.width);
            LParam.height = calHeight1080(LParam.height);
            LParam.leftMargin = calWidth1080(LParam.leftMargin);
            LParam.rightMargin = calWidth1080(LParam.rightMargin);
            LParam.topMargin = calHeight1080(LParam.topMargin);
            LParam.bottomMargin = calHeight1080(LParam.bottomMargin);

        } else if (tempParam instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams LParam = (FrameLayout.LayoutParams) tempParam;
            LParam.width = calWidth1080(LParam.width);
            LParam.height = calHeight1080(LParam.height);
            LParam.leftMargin = calWidth1080(LParam.leftMargin);
            LParam.rightMargin = calWidth1080(LParam.rightMargin);
            LParam.topMargin = calHeight1080(LParam.topMargin);
            LParam.bottomMargin = calHeight1080(LParam.bottomMargin);

        } else if (tempParam instanceof ViewPager.LayoutParams) {
            ViewPager.LayoutParams LParam = (ViewPager.LayoutParams) tempParam;
            LParam.width = calWidth1080(LParam.width);
            LParam.height = calHeight1080(LParam.height);
        } else if (tempParam instanceof AbsListView.LayoutParams) {
            AbsListView.LayoutParams LParam = (AbsListView.LayoutParams) tempParam;
            LParam.width = calWidth1080(LParam.width);
            LParam.height = calHeight1080(LParam.height);
        } else if (tempParam instanceof ActionBar.LayoutParams) {
            ActionBar.LayoutParams LParam = (ActionBar.LayoutParams) tempParam;
            Log.e("test", "ActionBar no resize functions");
        } else if (tempParam instanceof ViewGroup.LayoutParams) {
            ViewGroup.LayoutParams LParam = (ViewGroup.LayoutParams) tempParam;
            LParam.width = calWidth1080(LParam.width);
            LParam.height = calHeight1080(LParam.height);
        } else if (tempParam instanceof WindowManager.LayoutParams) {
            WindowManager.LayoutParams LParam = (WindowManager.LayoutParams) tempParam;
            LParam.width = calWidth1080(LParam.width);
            LParam.height = calHeight1080(LParam.height);
        } else if (tempParam instanceof GridLayout.LayoutParams) {
            GridLayout.LayoutParams LParam = (GridLayout.LayoutParams) tempParam;
            Log.e("test", "GridLayout no resize functions");
        } else if (tempParam instanceof RadioGroup.LayoutParams) {
            RadioGroup.LayoutParams LParam = (RadioGroup.LayoutParams) tempParam;
            LParam.width = calWidth1080(LParam.width);
            LParam.height = calHeight1080(LParam.height);
            LParam.leftMargin = calWidth1080(LParam.leftMargin);
            LParam.rightMargin = calWidth1080(LParam.rightMargin);
            LParam.topMargin = calHeight1080(LParam.topMargin);
            LParam.bottomMargin = calHeight1080(LParam.bottomMargin);
        } else if (tempParam instanceof TableLayout.LayoutParams) {
            TableLayout.LayoutParams LParam = (TableLayout.LayoutParams) tempParam;
            LParam.width = calWidth1080(LParam.width);
            LParam.height = calHeight1080(LParam.height);
            LParam.leftMargin = calWidth1080(LParam.leftMargin);
            LParam.rightMargin = calWidth1080(LParam.rightMargin);
            LParam.topMargin = calHeight1080(LParam.topMargin);
            LParam.bottomMargin = calHeight1080(LParam.bottomMargin);
        } else if (tempParam instanceof TableRow.LayoutParams) {
            TableRow.LayoutParams LParam = (TableRow.LayoutParams) tempParam;
            LParam.width = calWidth1080(LParam.width);
            LParam.height = calHeight1080(LParam.height);
            LParam.leftMargin = calWidth1080(LParam.leftMargin);
            LParam.rightMargin = calWidth1080(LParam.rightMargin);
            LParam.topMargin = calHeight1080(LParam.topMargin);
            LParam.bottomMargin = calHeight1080(LParam.bottomMargin);
        } else if (tempParam == null) {
            Log.e("test", "param is null !!");
        } else {
            Log.e("test", "no resize functions !!");
        }

        int left = calWidth1080(tempView.getPaddingLeft());
        int right = calWidth1080(tempView.getPaddingRight());
        int top = calHeight1080(tempView.getPaddingTop());
        int bottom = calHeight1080(tempView.getPaddingBottom());

        tempView.setPadding(left, top, right, bottom);
        if (tempView instanceof TextView) {
            TextView textview = (TextView) tempView;
            textview.setTextSize(0, calHeight1080((int) textview.getTextSize()));
        }
    }

    private int calWidth1080(int getwidth) {
        if (getwidth > 0) {
            return getwidth * disPlayWidth / 1080;
//             return getwidth * disPlayWidth / 720;
        } else {
            return getwidth;
        }
    }

    private int calHeight1080(int getheight) {
        if (getheight > 0) {
            return getheight * disPlayHeight / 1920;
            // return getheight * disPlayHeight / 1080;
        } else {
            return getheight;
        }
    }

    public void setWidthView(View tempView) {
        setByWidth(tempView);
        if (tempView instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) tempView).getChildCount(); i++) {
                View child = ((ViewGroup) tempView).getChildAt(i);
                setWidthView(child);
            }
        }
    }

    @SuppressLint("NewApi")
    private static void setByWidth(View tempView) {
//		setFont(tempView);
        ViewGroup.LayoutParams tempParam = tempView
                .getLayoutParams();
        if (tempView instanceof TableRow) {
            TableRow row = (TableRow) tempView;
            int minHeight = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                minHeight = row.getMinimumHeight();
                row.setMinimumHeight(calWidth(minHeight));
            }

        }
        if (tempParam instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams LParam = (LinearLayout.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calWidth(LParam.topMargin);
            LParam.bottomMargin = calWidth(LParam.bottomMargin);

        } else if (tempParam instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams LParam = (RelativeLayout.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calWidth(LParam.topMargin);
            LParam.bottomMargin = calWidth(LParam.bottomMargin);

        } else if (tempParam instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams LParam = (FrameLayout.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calWidth(LParam.topMargin);
            LParam.bottomMargin = calWidth(LParam.bottomMargin);

        } else if (tempParam instanceof ViewPager.LayoutParams) {
            ViewPager.LayoutParams LParam = (ViewPager.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
        } else if (tempParam instanceof AbsListView.LayoutParams) {
            AbsListView.LayoutParams LParam = (AbsListView.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
        } else if (tempParam instanceof ActionBar.LayoutParams) {
            ActionBar.LayoutParams LParam = (ActionBar.LayoutParams) tempParam;
            // LParam.width = calWidth(LParam.width);
            // LParam.height = calWidth(LParam.height);
            // LParam.leftMargin = calWidth(LParam.leftMargin);
            // LParam.rightMargin = calWidth(LParam.rightMargin);
            // LParam.topMargin = calWidth(LParam.topMargin);
            // LParam.bottomMargin = calWidth(LParam.bottomMargin);
            Log.e("test", "ActionBar no resize functions");
        } else if (tempParam instanceof ViewGroup.LayoutParams) {
            ViewGroup.LayoutParams LParam = (ViewGroup.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
        } else if (tempParam instanceof WindowManager.LayoutParams) {
            WindowManager.LayoutParams LParam = (WindowManager.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
        } else if (tempParam instanceof GridLayout.LayoutParams) {
            GridLayout.LayoutParams LParam = (GridLayout.LayoutParams) tempParam;
            // LParam.width = calWidth(LParam.width);
            // LParam.height = calWidth(LParam.height);
            // LParam.leftMargin = calWidth(LParam.leftMargin);
            // LParam.rightMargin = calWidth(LParam.rightMargin);
            // LParam.topMargin = calWidth(LParam.topMargin);
            // LParam.bottomMargin = calWidth(LParam.bottomMargin);
            Log.e("test", "GridLayout no resize functions");
        } else if (tempParam instanceof RadioGroup.LayoutParams) {
            RadioGroup.LayoutParams LParam = (RadioGroup.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calWidth(LParam.topMargin);
            LParam.bottomMargin = calWidth(LParam.bottomMargin);
        } else if (tempParam instanceof TableLayout.LayoutParams) {
            TableLayout.LayoutParams LParam = (TableLayout.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calWidth(LParam.topMargin);
            LParam.bottomMargin = calWidth(LParam.bottomMargin);
        } else if (tempParam instanceof TableRow.LayoutParams) {
            TableRow.LayoutParams LParam = (TableRow.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calWidth(LParam.topMargin);
            LParam.bottomMargin = calWidth(LParam.bottomMargin);
        } else if (tempParam instanceof TableRow.LayoutParams) {
            TableRow.LayoutParams LParam = (TableRow.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calWidth(LParam.topMargin);
            LParam.bottomMargin = calWidth(LParam.bottomMargin);

        } else if (tempParam == null) {
            Log.e("test", "param is null !!");
        } else {
            Log.e("test", "no resize functions !!");
        }

        int left = calWidth(tempView.getPaddingLeft());
        int right = calWidth(tempView.getPaddingRight());
        int top = calWidth(tempView.getPaddingTop());
        int bottom = calWidth(tempView.getPaddingBottom());
        tempView.setPadding(left, top, right, bottom);
        if (tempView instanceof TextView) {
            TextView textview = (TextView) tempView;
            textview.setTextSize(0, calWidth((int) textview.getTextSize()));
            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
            if (currentapiVersion >= 16){
                float spacing = textview.getLineSpacingExtra();
                textview.setLineSpacing(calWidth(spacing),1f);
            }


        }
    }

    private void setWidthStand(View tempView) {
        ViewGroup.LayoutParams tempParam = tempView
                .getLayoutParams();

        if (tempParam instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams LParam = (LinearLayout.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calWidth(LParam.topMargin);
            LParam.bottomMargin = calWidth(LParam.bottomMargin);

        } else if (tempParam instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams LParam = (RelativeLayout.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calWidth(LParam.topMargin);
            LParam.bottomMargin = calWidth(LParam.bottomMargin);

        } else if (tempParam instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams LParam = (FrameLayout.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
            LParam.leftMargin = calWidth(LParam.leftMargin);
            LParam.rightMargin = calWidth(LParam.rightMargin);
            LParam.topMargin = calWidth(LParam.topMargin);
            LParam.bottomMargin = calWidth(LParam.bottomMargin);

        } else if (tempParam instanceof ViewPager.LayoutParams) {
            ViewPager.LayoutParams LParam = (ViewPager.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);

        } else if (tempParam instanceof AbsListView.LayoutParams) {
            AbsListView.LayoutParams LParam = (AbsListView.LayoutParams) tempParam;
            LParam.width = calWidth(LParam.width);
            LParam.height = calWidth(LParam.height);
        } else {
            Log.e("test", "no resize functions");
        }

        int left = calWidth(tempView.getPaddingLeft());
        int right = calWidth(tempView.getPaddingRight());
        int top = calWidth(tempView.getPaddingTop());
        int bottom = calWidth(tempView.getPaddingBottom());
        tempView.setPadding(left, top, right, bottom);
    }

    private void setHeightStand(View tempView) {
        ViewGroup.LayoutParams tempParam = tempView
                .getLayoutParams();

        if (tempParam instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams LParam = (LinearLayout.LayoutParams) tempParam;
            LParam.width = calHeight(LParam.width);
            LParam.height = calHeight(LParam.height);
            LParam.leftMargin = calHeight(LParam.leftMargin);
            LParam.rightMargin = calHeight(LParam.rightMargin);
            LParam.topMargin = calHeight(LParam.topMargin);
            LParam.bottomMargin = calHeight(LParam.bottomMargin);

        } else if (tempParam instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams LParam = (RelativeLayout.LayoutParams) tempParam;
            LParam.width = calHeight(LParam.width);
            LParam.height = calHeight(LParam.height);
            LParam.leftMargin = calHeight(LParam.leftMargin);
            LParam.rightMargin = calHeight(LParam.rightMargin);
            LParam.topMargin = calHeight(LParam.topMargin);
            LParam.bottomMargin = calHeight(LParam.bottomMargin);

        } else if (tempParam instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams LParam = (FrameLayout.LayoutParams) tempParam;
            LParam.width = calHeight(LParam.width);
            LParam.height = calHeight(LParam.height);
            LParam.leftMargin = calHeight(LParam.leftMargin);
            LParam.rightMargin = calHeight(LParam.rightMargin);
            LParam.topMargin = calHeight(LParam.topMargin);
            LParam.bottomMargin = calHeight(LParam.bottomMargin);

        } else if (tempParam instanceof ViewPager.LayoutParams) {
            ViewPager.LayoutParams LParam = (ViewPager.LayoutParams) tempParam;
            LParam.width = calHeight(LParam.width);
            LParam.height = calHeight(LParam.height);

        } else if (tempParam instanceof AbsListView.LayoutParams) {
            AbsListView.LayoutParams LParam = (AbsListView.LayoutParams) tempParam;
            LParam.width = calHeight(LParam.width);
            LParam.height = calHeight(LParam.height);
        } else {
            Log.e("test", "no resize functions");
        }

        int left = calHeight(tempView.getPaddingLeft());
        int right = calHeight(tempView.getPaddingRight());
        int top = calHeight(tempView.getPaddingTop());
        int bottom = calHeight(tempView.getPaddingBottom());
        tempView.setPadding(left, top, right, bottom);
    }

    public static int calWidth(int getwidth) {
        if (getwidth > 0) {
            if(getwidth * disPlayWidth / 1080 < 1){
                return 1;
            }
            return getwidth * disPlayWidth / 1080;
        } else {
            return getwidth;
        }
    }

    private static float calWidth(float getwidth) {
        if (getwidth > 0) {
            if(getwidth * disPlayWidth / 1080 < 1){
                return 1;
            }
            return getwidth * disPlayWidth / 1080;
        } else {
            return getwidth;
        }
    }


    public static int calSize(int getwidth) {
        if (getwidth > 0) {
            return getwidth * disPlayWidth / 1080;
        } else {
            return getwidth;
        }
    }


    private static int calHeight(int getheight) {
        if (getheight > 0) {
            return getheight * disPlayHeight / 1920;
        } else {
            return getheight;
        }
    }
}
