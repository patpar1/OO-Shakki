package com.example.chess.game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Date;

public class GameInformation implements Serializable {

    private String fileName;
    private boolean isFinished;
    private Date date;
    private byte[] imageByteArray;

    GameInformation() {
    }

    public void setGameInformation(String fileName, boolean isFinished) {
        this.fileName = fileName;
        this.isFinished = isFinished;
    }

    public void setFinished(boolean finished) {
        this.isFinished = finished;
    }

    public void setImageByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.imageByteArray = stream.toByteArray();
    }

    public void updateTimeStamp() {
        date = new Date();
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Date getDate() {
        return date;
    }

    public Bitmap getBitmap() {
        return BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
    }
}
