package com.gegosoft.schoolteacherapp.Helper;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class MotionMessage {
    private static final String TAG = "MotionMessage";

    public final boolean actionDown;

    public final Pair<Float, Float> coordinates;
    public boolean isErase = false ;

    public MotionMessage(boolean actionDown, float x, float y,boolean isErase) {
        this.actionDown = actionDown;
        this.coordinates = new Pair<>(x, y);
        this.isErase = isErase;
    }

    public static @Nullable
    MotionMessage fromJson(String json) {
        MotionMessage motionMessage = null;

        try {
            JSONObject motionMessageJsonObject = new JSONObject(json);
            boolean actionDown = motionMessageJsonObject.getBoolean("mouseDown");
            JSONObject coordinates = motionMessageJsonObject.getJSONObject("mouseCoordinates");
            float x = Double.valueOf(coordinates.getDouble("x")).floatValue();
            float y = Double.valueOf(coordinates.getDouble("y")).floatValue();

            boolean isErase = motionMessageJsonObject.getBoolean("isErase");
            motionMessage = new MotionMessage(actionDown, x, y,isErase);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return motionMessage;
    }

    public String toJsonString() {
        JSONObject motionMessageJson = new JSONObject();
        JSONObject coordinatesJson = new JSONObject();

        try {
            motionMessageJson.put("mouseDown", actionDown);
            coordinatesJson.put("x", coordinates.first.doubleValue());
            coordinatesJson.put("y", coordinates.second.doubleValue());
            motionMessageJson.put("mouseCoordinates", coordinatesJson);
            motionMessageJson.put("isErase",isErase);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        return motionMessageJson.toString();
    }
}
