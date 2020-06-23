package com.example.trex;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.trex.GameView.screenRationX;
import static com.example.trex.GameView.screenRationY;

public class Tumbleweed  {

    public int speedTumbleweed= 20;
    int x, y, width, height, tumbleweedCounter = 1;
    Bitmap tumbleweed;

    Tumbleweed(Resources res){
        tumbleweed = BitmapFactory.decodeResource(res, R.drawable.tumbleweed);


        width = tumbleweed.getWidth();
        height = tumbleweed.getHeight();

        width /= 11;
        height /= 11;
        width = (int) (width * screenRationX);
        height = (int) (height * screenRationY);

        tumbleweed = Bitmap.createScaledBitmap(tumbleweed, width, height, false);


        y = -height;

    }

    Bitmap getTumbleweed(){
        if (tumbleweedCounter ==1){
            tumbleweedCounter++;
            return tumbleweed;
        }
        tumbleweedCounter =1;
        return tumbleweed;
    }
    Rect getCollisionShape(){
        return new Rect(x, y, x + width, y + height);
    }
}

