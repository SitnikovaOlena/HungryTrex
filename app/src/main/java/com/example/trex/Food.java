package com.example.trex;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.trex.GameView.screenRationX;
import static com.example.trex.GameView.screenRationY;

public class Food {

    public int speed = 20;
    int x, y, width, height, foodCounter = 1;
    public boolean wasEaten = true;
    Bitmap burger, candy, icecream, cupcake;

    Food(Resources res){
        burger = BitmapFactory.decodeResource(res, R.drawable.burger);
//        candy = BitmapFactory.decodeResource(res, R.drawable.candy);
//        icecream = BitmapFactory.decodeResource(res, R.drawable.icecream);
//        cupcake = BitmapFactory.decodeResource(res, R.drawable.cupcake);

        width = burger.getWidth();
        height = burger.getHeight();

        width /= 9;
        height /= 9;
        width = (int) (width * screenRationX);
        height = (int) (height * screenRationY);

        burger = Bitmap.createScaledBitmap(burger, width, height, false);
//        candy = Bitmap.createScaledBitmap(candy, width, height, false);
//        icecream = Bitmap.createScaledBitmap(icecream, width, height, false);
//        cupcake = Bitmap.createScaledBitmap(cupcake, width, height, false);

        y = -height;

    }

    Bitmap getBurger(){
            return burger;
        }

    Rect getCollisionShape(){
        return new Rect(x, y, x + width, y + height);
    }
}

