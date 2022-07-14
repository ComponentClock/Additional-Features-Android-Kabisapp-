package com.radjago.drivergo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import com.radjago.drivergo.R;
import com.radjago.drivergo.constants.BaseApp;
import com.radjago.drivergo.models.User;

public class driverqr {
    private Bitmap textToImage(String text, int width, int height) throws WriterException, NullPointerException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.DATA_MATRIX.QR_CODE,
                    width, height, null);
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        int colorWhite = 0xFFFFFFFF;
        int colorBlack = 0xFF000000;

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public void showPopupWindow(final View view) {
        User loginUser = BaseApp.getInstance(view.getContext()).getLoginUser();
        //Create a View object yourself through inflater
        String token = loginUser.getToken();
        String Id = loginUser.getId();
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.layout_qr, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = true;
        ImageView MyQR = popupView.findViewById(R.id.myQR);
        CheckBox SetQR = popupView.findViewById(R.id.SetQr);
        TextView TxtTitle = popupView.findViewById(R.id.TxtTitle);
        TxtTitle.setText("Quick Accept Order");
        try {
            Bitmap bitmap = textToImage(token, 700, 700);
            if (bitmap != null) {
                MyQR.setImageBitmap(bitmap);
            }
        } catch (WriterException e) {

            Log.e("" + e, "Exception");
        }
        SetQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CompoundButton) view).isChecked()) {
                    TxtTitle.setText("Quick Accept Wallet");
                    try {
                        Bitmap bitmap = textToImage(Id, 700, 700);
                        if (bitmap != null) {
                            MyQR.setImageBitmap(bitmap);
                        }
                    } catch (WriterException e) {

                        Log.e("" + e, "Exception");
                    }
                } else {
                    TxtTitle.setText("Quick Accept Order");
                    try {
                        Bitmap bitmap = textToImage(token, 700, 700);
                        if (bitmap != null) {
                            MyQR.setImageBitmap(bitmap);
                        }
                    } catch (WriterException e) {

                        Log.e("" + e, "Exception");
                    }
                }
            }
        });


        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
