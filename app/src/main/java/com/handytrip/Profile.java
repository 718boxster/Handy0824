package com.handytrip;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.handytrip.Utils.AutoLayout;
import com.handytrip.Utils.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Profile extends BaseActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.profile_picture)
    ImageView profilePicture;
    @BindView(R.id.profile_mypic)
    LinearLayout profileMypic;
    @BindView(R.id.profile_nickname)
    LinearLayout profileNickname;
    @BindView(R.id.profile_user_name)
    LinearLayout profileUserName;
    @BindView(R.id.profile_account)
    LinearLayout profileAccount;

    private Intent pictureActionIntent = null;
    Bitmap bitmap;
    String selectedImagePath;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        AutoLayout.setResizeView(this);
        intent = new Intent();

        if(! TextUtils.isEmpty(pref.getProfileImg())){
            Glide.with(this).load(Uri.parse(pref.getProfileImg())).apply(new RequestOptions().circleCrop()).into(profilePicture);
            profilePicture.setPadding(10, 10, 10, 10);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        try{
            Log.d("IMAGE_URL", selectedImagePath);
        } catch (Exception e){
            e.printStackTrace();
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick({R.id.back, R.id.profile_mypic, R.id.profile_nickname, R.id.profile_user_name, R.id.profile_account})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.profile_mypic:
                startDialog();
                break;
            case R.id.profile_nickname:
                ProfileDialog nickDialog = new ProfileDialog(this, "NICKNAME");
                nickDialog.setOnDialogResult(new ProfileDialog.OnDialogResult() {
                    @Override
                    public void done(String done) {
                        TextView nickname = findViewById(R.id.my_nick);
                        nickname.setVisibility(View.VISIBLE);
                        if(" ".equals(pref.getUserNick())){
                            nickname.setVisibility(View.GONE);
                        }
                        nickname.setText(pref.getUserNick());
                    }
                });
                nickDialog.show();
                nickDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                break;
            case R.id.profile_user_name:
                ProfileDialog nameDialog = new ProfileDialog(this, "NAME");
                nameDialog.setOnDialogResult(new ProfileDialog.OnDialogResult() {
                    @Override
                    public void done(String done) {
                        TextView name = findViewById(R.id.my_name);
                        name.setVisibility(View.VISIBLE);
                        name.setText(pref.getUserName());
                    }
                });
                nameDialog.show();
                nameDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                break;
            case R.id.profile_account:
                ProfileDialog accountDialog = new ProfileDialog(this, "ACCOUNT");
                accountDialog.show();
                accountDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                break;
        }
    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                this);
        myAlertDialog.setTitle("프로필 사진 설정");
        myAlertDialog.setMessage("사용할 앱을 선택해주세요.");
        myAlertDialog.setPositiveButton("갤러리",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = null;

                        pictureActionIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(
                                pictureActionIntent,
                                1);

                    }
                });

        myAlertDialog.setNegativeButton("카메라",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment
                                .getExternalStorageDirectory().toString(), "/Pictures/temp.jpg");
                        if(f.exists()){
                            f.delete();
                            try{
                                f.createNewFile();
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        } else{
                            try {
                                f.createNewFile();
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                FileProvider.getUriForFile(getApplicationContext(), "com.handytrip.fileprovider", f));
//                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        startActivityForResult(intent,
                                2);

                    }
                });
        myAlertDialog.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        bitmap = null;
        selectedImagePath = null;

        if (resultCode == RESULT_OK && requestCode == 2) {

            File f = new File(Environment.getExternalStorageDirectory().toString() + "/Pictures/temp.jpg");
//            for (File temp : f.listFiles()) {
//                if (temp.getName().equals("temp.jpg")) {
//                    f = temp;
//                    break;
//                }
//            }

            if (!f.exists()) {

//                Toast.makeText(getBaseContext(),
//
//                        "Error while capturing image", Toast.LENGTH_LONG)
//
//                        .show();

                return;

            }

            try {

                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());

                bitmap = Bitmap.createScaledBitmap(bitmap, 140, 140, true);

                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);

                Glide.with(this).load(bitmap).apply(new RequestOptions().circleCrop()).into(profilePicture);
                profilePicture.setScaleType(ImageView.ScaleType.FIT_XY);
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/Pictures");
                myDir.mkdirs();
//                Random generator = new Random();
//                int n = 10000;
//                n = generator.nextInt(n);
                String fname = "HandyTripProfile.jpg";
                File file = new File(myDir, fname);
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Uri cameraImg = FileProvider.getUriForFile(this, "com.handytrip.fileprovider", file);
                selectedImagePath = cameraImg.toString();

//                profilePicture.setImageBitmap(bitmap);
                //storeImageTosdCard(bitmap);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == 1) {
            if (data != null) {

                Uri selectedImage = data.getData();
                selectedImagePath = selectedImage.toString();

//                String[] filePath = { MediaStore.Images.Media.DATA };
//                Cursor c = getContentResolver().query(selectedImage, filePath,
//                        null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                selectedImagePath = c.getString(columnIndex);
//                c.close();

//                if (selectedImagePath != null) {
//                    txt_image_path.setText(selectedImagePath);
//                }

                bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
                // preview image
                bitmap = Bitmap.createScaledBitmap(bitmap, 140, 140, false);



//                profilePicture.setImageBitmap(bitmap);
                Glide.with(this).load(bitmap).apply(new RequestOptions().circleCrop()).into(profilePicture);
                profilePicture.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
//                Toast.makeText(getApplicationContext(), "Cancelled",
//                        Toast.LENGTH_SHORT).show();
            }
        }
        pref.setProfileImg(selectedImagePath);
        intent.putExtra("imgUri", selectedImagePath);
    }
}
