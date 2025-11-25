package com.gegosoft.yourappname.Helper;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;

import com.twilio.video.Camera2Capturer;
import com.twilio.video.CameraCapturer;
import com.twilio.video.VideoCapturer;

import tvi.webrtc.Camera2Enumerator;

public class CameraCapturerCompat {
    private static final String TAG = "CameraCapturerCompat";

    private CameraCapturer camera1Capturer;
    private Camera2Capturer camera2Capturer;
    private Pair<CameraCapturer.CameraSource, String> frontCameraPair;
    private Pair<CameraCapturer.CameraSource, String> backCameraPair;
    private CameraManager cameraManager;

    public CameraCapturerCompat(Context context, CameraCapturer.CameraSource cameraSource) {
        if (Camera2Capturer.isSupported(context) && isLollipopApiSupported()) {
            cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            setCameraPairs(context);
            Camera2Capturer.Listener camera2Listener =
                    new Camera2Capturer.Listener() {
                        @Override
                        public void onFirstFrameAvailable() {
                            Log.i(TAG,"onFirstFrameAvailable");
                        }

                        @Override
                        public void onCameraSwitched(@NonNull String newCameraId) {
                            Log.i(TAG, "onCameraSwitched: newCameraId = " + newCameraId);
                        }

                        @Override
                        public void onError(
                                @NonNull Camera2Capturer.Exception camera2CapturerException) {
                            Log.e(TAG, camera2CapturerException.toString());
                        }
                    };
            camera2Capturer =
                    new Camera2Capturer(context, getCameraId(cameraSource), camera2Listener);
        } else {
            camera1Capturer = new CameraCapturer(context, cameraSource);
        }
    }

    public CameraCapturer.CameraSource getCameraSource() {
        if (usingCamera1()) {
            return camera1Capturer.getCameraSource();
        } else {
            return getCameraSource(camera2Capturer.getCameraId());
        }
    }

    public void switchCamera() {
        if (usingCamera1()) {
            camera1Capturer.switchCamera();
        } else {
            CameraCapturer.CameraSource cameraSource =
                    getCameraSource(camera2Capturer.getCameraId());

            if (cameraSource == CameraCapturer.CameraSource.FRONT_CAMERA) {
                camera2Capturer.switchCamera(backCameraPair.second);
            } else {
                camera2Capturer.switchCamera(frontCameraPair.second);
            }
        }
    }


    public VideoCapturer getVideoCapturer() {
        if (usingCamera1()) {
            return camera1Capturer;
        } else {
            return camera2Capturer;
        }
    }

    private boolean usingCamera1() {
        return camera1Capturer != null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setCameraPairs(Context context) {
        Camera2Enumerator camera2Enumerator = new Camera2Enumerator(context);
        for (String cameraId : camera2Enumerator.getDeviceNames()) {
            if (isCameraIdSupported(cameraId)) {
                if (camera2Enumerator.isFrontFacing(cameraId)) {
                    frontCameraPair =
                            new Pair<>(CameraCapturer.CameraSource.FRONT_CAMERA, cameraId);
                }
                if (camera2Enumerator.isBackFacing(cameraId)) {
                    backCameraPair = new Pair<>(CameraCapturer.CameraSource.BACK_CAMERA, cameraId);
                }
            }
        }
    }

    private String getCameraId(CameraCapturer.CameraSource cameraSource) {
        if (frontCameraPair.first == cameraSource) {
            return frontCameraPair.second;
        } else {
            return backCameraPair.second;
        }
    }

    private CameraCapturer.CameraSource getCameraSource(String cameraId) {
        if (frontCameraPair.second.equals(cameraId)) {
            return frontCameraPair.first;
        } else {
            return backCameraPair.first;
        }
    }

    private boolean isLollipopApiSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private boolean isCameraIdSupported(String cameraId) {
        boolean isMonoChromeSupported = false;
        boolean isPrivateImageFormatSupported = false;
        CameraCharacteristics cameraCharacteristics;
        try {
            cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        final StreamConfigurationMap streamMap =
                cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

        if (streamMap != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isPrivateImageFormatSupported = streamMap.isOutputSupportedFor(ImageFormat.PRIVATE);
        }

        Integer colorFilterArrangement =
                cameraCharacteristics.get(
                        CameraCharacteristics.SENSOR_INFO_COLOR_FILTER_ARRANGEMENT);
        colorFilterArrangement = colorFilterArrangement == null ? -1 : colorFilterArrangement;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isMonoChromeSupported =
                    colorFilterArrangement
                            == CameraMetadata.SENSOR_INFO_COLOR_FILTER_ARRANGEMENT_MONO
                            || colorFilterArrangement
                            == CameraMetadata.SENSOR_INFO_COLOR_FILTER_ARRANGEMENT_NIR;
        }
        return isPrivateImageFormatSupported && !isMonoChromeSupported;
    }
}

