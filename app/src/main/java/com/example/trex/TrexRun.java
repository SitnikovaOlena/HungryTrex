package com.example.trex;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.trex.GameView.screenRationX;
import static com.example.trex.GameView.screenRationY;

public class TrexRun {

    public boolean isJumping = false;
    public int toEat = 0, eatCounter =1;
    public boolean isEating = false;
    int x, y, width, height, legsCounter = 0;
    Bitmap run1, run2, jump, eat1, eat2, eat3, dead;
    private GameView gameView;
    TrexRun (GameView gameView, int screenY, Resources res) {
        this.gameView = gameView;
        run1 = BitmapFactory.decodeResource(res, R.drawable.trex_move1);
        run2 = BitmapFactory.decodeResource(res, R.drawable.trex_move2);

        eat1 = BitmapFactory.decodeResource(res, R.drawable.trex_eat1);
        eat2 = BitmapFactory.decodeResource(res, R.drawable.trex_eat2);
        eat3 = BitmapFactory.decodeResource(res, R.drawable.trex_eat3);


        dead = BitmapFactory.decodeResource(res, R.drawable.trex_dead);

        jump = BitmapFactory.decodeResource(res, R.drawable.trex_jump);

        width = run1.getWidth();
        height = run1.getHeight();

        width /= 2;
        height /= 2;

        width = (int) (width * screenRationX);
        height = (int) (height * screenRationY);

        run1 = Bitmap.createScaledBitmap(run1, width, height, false);
        run2 = Bitmap.createScaledBitmap(run2, width, height, false);

        eat1 = Bitmap.createScaledBitmap(eat1, width, height, false);
        eat2 = Bitmap.createScaledBitmap(eat2, width, height, false);
        eat3 = Bitmap.createScaledBitmap(eat3, width, height, false);

        dead = Bitmap.createScaledBitmap(dead, width, height, false);

        jump = Bitmap.createScaledBitmap(jump, width, height, false);
        y = (int) (screenY / 2.25);
//        x = (int) (14 * screenRationX);
    }

    Bitmap getRun(){
        if(toEat != 0){
            if(eatCounter ==1){
                eatCounter++;
                return eat1;
            }
            if(eatCounter ==2){
                eatCounter++;
                return eat1;
            }
            if(eatCounter ==3){
                eatCounter++;
                return eat1;
            }
            if(eatCounter ==4){
                eatCounter++;
                return eat2;

            }
            if(eatCounter ==5){
                eatCounter++;
                return eat2;
            }
            if(eatCounter ==6){
                eatCounter++;
                return eat2;
            }
            eatCounter =1;
            toEat--;
            gameView.newEat();
            return eat3;

        }

        if (legsCounter == 0){
            legsCounter++;
            return run1;
        }
        if (legsCounter == 1){
            legsCounter++;
            return run1;
        }
        if (legsCounter == 2){
            legsCounter++;
            return run2;
        }
        legsCounter = 0;
        return run2;
    }

    Rect getCollisionShape(){
        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getDead(){
        return dead;
    }

    Bitmap getJump(){
        return jump;
    }
}
