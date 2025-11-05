package com.gegosoft.schoolteacherapp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gegosoft.schoolteacherapp.Helper.ApiClient;
import com.gegosoft.schoolteacherapp.Fragment.ChatFragment;
import com.gegosoft.schoolteacherapp.Helper.CameraCapturerCompat;
import com.gegosoft.schoolteacherapp.Helper.CollaborativeDrawingView;
import com.gegosoft.schoolteacherapp.Helper.Dialog;
import com.gegosoft.schoolteacherapp.Helper.DialogPdfViewer;
import com.gegosoft.schoolteacherapp.Helper.MotionMessage;
import com.gegosoft.schoolteacherapp.Helper.PhoneStateReceiver;
import com.gegosoft.schoolteacherapp.Helper.ScreenCapturerManager;
import com.gegosoft.schoolteacherapp.Helper.StrokeSelectorDialog;
import com.gegosoft.schoolteacherapp.Interface.Api;
import com.gegosoft.schoolteacherapp.Models.ApiSuccessModel;
import com.gegosoft.schoolteacherapp.Models.InvitesModel;
import com.gegosoft.schoolteacherapp.R;
import com.gegosoft.schoolteacherapp.Storage.UserDetailsSharedPref;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.twilio.audioswitch.AudioDevice;
import com.twilio.audioswitch.AudioSwitch;
import com.twilio.video.AspectRatio;
import com.twilio.video.AudioCodec;
import com.twilio.video.CameraCapturer;
import com.twilio.video.ConnectOptions;
import com.twilio.video.EncodingParameters;
import com.twilio.video.G722Codec;
import com.twilio.video.H264Codec;
import com.twilio.video.IsacCodec;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalDataTrack;
import com.twilio.video.LocalParticipant;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.NetworkQualityConfiguration;
import com.twilio.video.NetworkQualityLevel;
import com.twilio.video.NetworkQualityVerbosity;
import com.twilio.video.OpusCodec;
import com.twilio.video.PcmaCodec;
import com.twilio.video.PcmuCodec;
import com.twilio.video.RemoteAudioTrack;
import com.twilio.video.RemoteAudioTrackPublication;
import com.twilio.video.RemoteDataTrack;
import com.twilio.video.RemoteDataTrackPublication;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.RemoteVideoTrack;
import com.twilio.video.RemoteVideoTrackPublication;
import com.twilio.video.Room;
import com.twilio.video.ScreenCapturer;
import com.twilio.video.TwilioException;
import com.twilio.video.Video;
import com.twilio.video.VideoCodec;
import com.twilio.video.VideoConstraints;
import com.twilio.video.VideoDimensions;
import com.twilio.video.VideoRenderer;
import com.twilio.video.VideoScaleType;
import com.twilio.video.VideoTextureView;
import com.twilio.video.VideoView;
import com.twilio.video.Vp8Codec;
import com.twilio.video.Vp9Codec;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import co.intentservice.chatui.models.ChatMessage;
import kotlin.Unit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoActivity extends AppCompatActivity  {
    private static final int CAMERA_MIC_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "VideoActivity";
    private static final int PICK_PDF_FILE = 2;
    byte[] bytes;


    Api api;
    UserDetailsSharedPref userDetailsSharedPref;
    Map<String, String> headermap;
    List<InvitesModel.Datum> invitedStudentList;
    List<ChatMessage> chatMessages = new ArrayList<>();

    private static final String LOCAL_AUDIO_TRACK_NAME = "mic";
    private static final String LOCAL_VIDEO_TRACK_NAME = "camera";

    private static final String TWILIO_ACCESS_TOKEN = "AC9ed987a221fcfc958016b2b36ddab44d";
    private static final int PICKFILE_RESULT_CODE = 2;

    private String accessToken, roomName;
    ParticipantListAdapter participantListAdapter;
    private int roomId;

    private Room room;
    private LocalParticipant localParticipant;

    private AudioCodec audioCodec;
    private VideoCodec videoCodec;

    private EncodingParameters encodingParameters;

    private VideoView primaryVideoView;

    private SharedPreferences preferences;

    private CameraCapturerCompat cameraCapturerCompat;
    private LocalAudioTrack localAudioTrack;
    private LocalVideoTrack localVideoTrack;
    private LocalDataTrack localDataTrack;
    private FloatingActionButton connectActionFab;
    private FloatingActionButton switchCameraActionFab;
    private FloatingActionButton localVideoActionFab;
    private FloatingActionButton muteActionFab;
    private ProgressBar reconnectingProgressBar;
    private AlertDialog connectDialog;
    private String remoteParticipantIdentity;
    RecyclerView thumbnail_recyclerview;
    ThumbnailAdapter thumbnailAdapter;
    int mCurrentBackgroundColor = Color.WHITE;
    private int mCurrentColor = Color.BLACK;
    private int mCurrentStroke = 10;

    private static final int MAX_STROKE_WIDTH = 50;
    int conferenceid;

    private AudioSwitch audioSwitch;

    private int savedVolumeControlStream;
    private MenuItem audioDeviceMenuItem;

    private MenuItem chatMenuItem;

    private VideoRenderer localVideoView;
    private boolean disconnectedFromOnDestroy;
    private boolean enableAutomaticSubscription;
    List<RemoteParticipant> remoteParticipantList;
    int screenheight, screenwidth;

    private static final String DATA_TRACK_MESSAGE_THREAD_NAME = "DataTrackMessages";

    private final HandlerThread dataTrackMessageThread =
            new HandlerThread(DATA_TRACK_MESSAGE_THREAD_NAME);
    private Handler dataTrackMessageThreadHandler;
    private final Map<RemoteDataTrack, RemoteParticipant> dataTrackRemoteParticipantMap =
            new HashMap<>();
    boolean isOnCall = false;
    AudioManager audioManager;
    TextView video_status_textview;
    private ScreenCapturer screenCapturer;
    private MenuItem screenCaptureMenuItem;
    private ScreenCapturerManager screenCapturerManager;
    private static final int REQUEST_MEDIA_PROJECTION = 100;
    boolean isScreenshare = false;
    AlertDialog whiteboard_dialog;
    CollaborativeDrawingView collaborativeDrawingView;
    private Path mDrawPath;
    private Canvas mDrawCanvas;
    private boolean isCameraOn = true;

    ChatFragment chatFragment;
    private final CollaborativeDrawingView.Listener drawingViewListener =
            new CollaborativeDrawingView.Listener() {
                @Override
                public void onTouchEvent(int actionEvent, float x, float y) {
                    Log.d(TAG, String.format("onTouchEvent: actionEvent=%d, x=%f, y=%f",
                            actionEvent, x, y));
                    boolean actionDown = (actionEvent == MotionEvent.ACTION_DOWN);
                    float normalizedX = x / (float) collaborativeDrawingView.getWidth();
                    float normalizedY = y / (float) collaborativeDrawingView.getHeight();
                    MotionMessage motionMessage = new MotionMessage(actionDown,
                            normalizedX,
                            normalizedY, collaborativeDrawingView.isErase());
                    if (localDataTrack != null) {
                        localDataTrack.send(motionMessage.toJsonString());
                    }
                }
            };

    private final ScreenCapturer.Listener screenCapturerListener = new ScreenCapturer.Listener() {
        @Override
        public void onScreenCaptureError(String errorDescription) {
            stopScreenCapture();
            Toast.makeText(VideoActivity.this, R.string.screen_capture_error,
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFirstFrameAvailable() {

        }
    };

    private PhoneStateReceiver phoneStateReceiver = new PhoneStateReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            try {
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

                    if (room.getRemoteParticipants().size() != 0) {
                        if (localAudioTrack != null) {
                            localAudioTrack.enable(false);
                            muteActionFab.setImageDrawable(ContextCompat.getDrawable(
                                    VideoActivity.this, R.drawable.ic_mic_off_black_24dp));


                        }
                    }

                    isOnCall = true;
                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    if (localAudioTrack != null) {
                        localAudioTrack.enable(true);
                        muteActionFab.setImageDrawable(ContextCompat.getDrawable(
                                VideoActivity.this, R.drawable.ic_mic_white_24dp));
                    }

                    isOnCall = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    ActivityResultLauncher<Intent> pickPdfFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if(data != null){
                        JSONObject parent = new JSONObject();
                        JSONObject sharefile = new JSONObject();

                        try {
                            sharefile.put("name", "name");
                            sharefile.put("extension", "pdf");
                            sharefile.put("filetype", "application/pdf");
                            parent.put("sharefile", sharefile);
                            Log.i(TAG, "onActivityResult: " + parent.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        localDataTrack.send(parent.toString());
                        Log.i(TAG, "onActivityResult: " + localDataTrack.toString());

                        Uri pdfUri = data.getData();
                        InputStream iStream = null;
                        try {
                            iStream = getContentResolver().openInputStream(pdfUri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        try {
                            byte[] inputData = getBytes(iStream);
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("Total Bytes", String.valueOf(inputData.length));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            localDataTrack.send(jsonObject.toString());
                            DialogPdfViewer dialogPdfViewer = new DialogPdfViewer();
                            dialogPdfViewer.setCancelable(false);
                            Bundle bundle = new Bundle();
                            bundle.putByteArray("byte", inputData);
                            dialogPdfViewer.setArguments(bundle);
                            dialogPdfViewer.show(getSupportFragmentManager(), "loading screen");
                            List<byte[]> bytes = getFileChunks(inputData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    ActivityResultLauncher<Intent> pickImageFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if(data != null){
                        Uri imgUri = data.getData();

                    }
                }
            }
    );
    ActivityResultLauncher<Intent> requestScreenCapturePermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        screenCapturer = new ScreenCapturer(this, result.getResultCode(), data, screenCapturerListener);
                        startScreenCapture();
                    }
                } else {
                    Toast.makeText(this, R.string.screen_capture_permission_not_granted, Toast.LENGTH_LONG).show();
                }
            }
    );

    OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            AlertDialog.Builder builder = new AlertDialog.Builder(VideoActivity.this);
            builder.setTitle("")
                    .setMessage("Do you want to quit")
                    .setCancelable(true)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.videochat_activity);

        api = ApiClient.getClient().create(Api.class);
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");
        conferenceid = getIntent().getExtras().getInt("video_conference_id");
        getInvitedStudentsList();
        video_status_textview = findViewById(R.id.video_status_textview);
        primaryVideoView = findViewById(R.id.primary_video_view);
        reconnectingProgressBar = findViewById(R.id.reconnecting_progress_bar);
        connectActionFab = findViewById(R.id.connect_action_fab);
        switchCameraActionFab = findViewById(R.id.switch_camera_action_fab);
        localVideoActionFab = findViewById(R.id.local_video_action_fab);
        muteActionFab = findViewById(R.id.mute_action_fab);
        thumbnail_recyclerview = findViewById(R.id.thumbnail_recyclerview);
        thumbnail_recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        remoteParticipantList = new ArrayList<>();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenheight = displayMetrics.heightPixels;
        screenwidth = displayMetrics.widthPixels;

        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        audioSwitch = new AudioSwitch(VideoActivity.this);
        savedVolumeControlStream = getVolumeControlStream();
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        audioManager.setSpeakerphoneOn(true);

        if (!checkPermissionForCameraAndMicrophone()) {
            requestPermissionForCameraAndMicrophone();
        } else {
            createAudioAndVideoTracks();
            setAccessToken();
        }
        if (Build.VERSION.SDK_INT >= 29) {
            screenCapturerManager = new ScreenCapturerManager(this);
        }

        intializeUI();
    }



    private void getInvitedStudentsList() {
        Call<InvitesModel> modelCall = api.getInvites(headermap, conferenceid);
        modelCall.enqueue(new Callback<InvitesModel>() {
            @Override
            public void onResponse(Call<InvitesModel> call, Response<InvitesModel> response) {
                if (response.isSuccessful()) {
                    invitedStudentList = response.body().getData();
                    Collections.reverse(invitedStudentList);
                }
            }

            @Override
            public void onFailure(Call<InvitesModel> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_video_activity, menu);
        audioDeviceMenuItem = menu.findItem(R.id.menu_audio_device);
        screenCaptureMenuItem = menu.findItem(R.id.share_screen_menu_item);
        chatMenuItem = menu.findItem(R.id.chat);


        audioSwitch.start((audioDevices, audioDevice) -> {
            updateAudioDeviceIcon(audioDevice);
            return Unit.INSTANCE;
        });
        connectToRoom(roomName);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id ==  R.id.menu_settings){
        startActivity(new Intent(this, SettingsActivity.class));
        return true;
        }else if (id == R.id.white_board){
        showWhiteBoard();
        return true;
        }else if (id == R.id.file_share){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes = {"application/pdf"};
        chooseFile.setType("*/*");
        chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        pickPdfFileLauncher.launch(chooseFile);
        return true;
        }else if (id == R.id.student_list){
        showParticipantList();
        return true;
        }else if (id == R.id.image_share){
        Intent chooseImage = new Intent(Intent.ACTION_GET_CONTENT);
        String[] mimeTypess = {"image/*"};
        chooseImage.setType("*/*");
        chooseImage.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypess);
        chooseImage = Intent.createChooser(chooseImage, "Choose a file");
        pickImageFileLauncher.launch(chooseImage);
        }else if (id == R.id.chat){
        chatFragment = new ChatFragment();
        chatFragment.setCancelable(true);
        Bundle bundle = new Bundle();
        bundle.putString("name", userDetailsSharedPref.getString("name"));
        bundle.putString("room_name", roomName);
        bundle.putSerializable("messages", (Serializable) chatMessages);
        chatFragment.setArguments(bundle);
        chatFragment.show(getSupportFragmentManager(), "loading screen");
        chatMenuItem.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        return true;
        }else if (id == R.id.share_screen_menu_item){
        String shareScreen = getString(R.string.share_screen);
        if (item.getTitle().equals(shareScreen)) {
            if (Build.VERSION.SDK_INT >= 29) {
                screenCapturerManager.startForeground();
            }
            if (screenCapturer == null) {
                requestScreenCapturePermission();
            } else {
                startScreenCapture();
            }
        } else {
            if (Build.VERSION.SDK_INT >= 29) {
                screenCapturerManager.endForeground();
            }
            stopScreenCapture();
        }
        return true;
        }else if (id == R.id.menu_audio_device){
        showAudioDevices();
        return true;
        }else if (id == R.id.menu_mute_all) {
            JSONObject jsonObject = new JSONObject();
            String title = item.getTitle().toString();
            if (title.equalsIgnoreCase("Mute all")) {
                try {
                    jsonObject.put("All are Muted", true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "All are Muted!", Toast.LENGTH_SHORT).show();
                item.setTitle("Unmute all");
            } else {
                Toast.makeText(this, "All are UnMuted!", Toast.LENGTH_SHORT).show();
                try {
                    jsonObject.put("All are UnMuted", true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                item.setTitle("Mute all");
            }
            localDataTrack.send(jsonObject.toString());

            for (int i = 0; i < remoteParticipantList.size(); i++) {
                RemoteAudioTrackPublication remoteAudioTrackPublication = remoteParticipantList.get(i)
                        .getRemoteAudioTracks().get(0);
                if (remoteAudioTrackPublication.getRemoteAudioTrack().isPlaybackEnabled()) {
                    remoteAudioTrackPublication.getRemoteAudioTrack().enablePlayback(false);
                } else {
                    remoteAudioTrackPublication.getRemoteAudioTrack().enablePlayback(true);
                }

            }
            thumbnailAdapter.notifyDataSetChanged();
            return true;
        }else {
            return false;
        }
        return false;
    }

    private void showWhiteBoard() {

        AlertDialog.Builder builder
                = new AlertDialog.Builder(this, R.style.full_screen_alert_dialog);
        builder.setCancelable(false);
        final View customLayout = getLayoutInflater().inflate(R.layout.white_board,
                null);
        builder.setView(customLayout);

        ImageView close_image = customLayout.findViewById(R.id.close_image);
        ImageView image_eraser = customLayout.findViewById(R.id.image_eraser);
        ImageView image_eraser2 = customLayout.findViewById(R.id.image_eraser2);
        ImageView main_fill_iv = customLayout.findViewById(R.id.main_color_iv);
        ImageView main_color_iv = customLayout.findViewById(R.id.main_fill_iv);
        ImageView main_stroke_iv = customLayout.findViewById(R.id.main_stroke_iv);


        collaborativeDrawingView = customLayout.findViewById(R.id.collaborativeDrawingView);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Show White Board", true);
            localDataTrack.send(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        collaborativeDrawingView.setListener(drawingViewListener);
        collaborativeDrawingView.setEnabled(true);

        whiteboard_dialog
                = builder.create();
        whiteboard_dialog.show();


        image_eraser2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    collaborativeDrawingView.eraser(true);
                    localDataTrack.send(jsonObject.toString());
                    jsonObject.put("Clear Specific View", true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        image_eraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Clear View", true);
                    collaborativeDrawingView.clear();
                    localDataTrack.send(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        close_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whiteboard_dialog.dismiss();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("close white board", true);
                    localDataTrack.send(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        main_fill_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] colors = getResources().getIntArray(R.array.palette);

                ColorPickerDialog colorPickerDialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                        colors,
                        mCurrentBackgroundColor,
                        5,
                        ColorPickerDialog.SIZE_SMALL);

                colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

                    @Override
                    public void onColorSelected(int color) {
                        mCurrentBackgroundColor = color;
                        collaborativeDrawingView.setViewBackgroundColor(mCurrentBackgroundColor);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("backgroundcolor", mCurrentBackgroundColor);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        localDataTrack.send(jsonObject.toString());
                    }
                });
                colorPickerDialog.show(getFragmentManager(), "ColorPickerDialog");
            }
        });

        main_color_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] colors = getResources().getIntArray(R.array.palette);

                ColorPickerDialog colorPickerDialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                        colors,
                        mCurrentColor,
                        5,
                        ColorPickerDialog.SIZE_SMALL);

                colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

                    @Override
                    public void onColorSelected(int color) {
                        mCurrentColor = color;
                        collaborativeDrawingView.eraser(false);
                        collaborativeDrawingView.setPaintColor(mCurrentColor);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("paintColor", mCurrentColor);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        localDataTrack.send(jsonObject.toString());
                    }

                });

                colorPickerDialog.show(getFragmentManager(), "ColorPickerDialog");
            }
        });

        main_stroke_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrokeSelectorDialog strokeSelectorDialog = StrokeSelectorDialog.newInstance(mCurrentStroke, MAX_STROKE_WIDTH);

                strokeSelectorDialog.setOnStrokeSelectedListener(new StrokeSelectorDialog.OnStrokeSelectedListener() {
                    @Override

                    public void onStrokeSelected(int stroke) {
                        mCurrentStroke = stroke;
                        collaborativeDrawingView.setPaintStrokeWidth(mCurrentStroke);
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("stroke", mCurrentStroke);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        localDataTrack.send(jsonObject.toString());
                    }
                });

                strokeSelectorDialog.show(getSupportFragmentManager(), "StrokeSelectorDialog");
            }
        });
    }

    private void requestScreenCapturePermission() {
        Log.d(TAG, "Requesting permission to capture screen");
        MediaProjectionManager mediaProjectionManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mediaProjectionManager = (MediaProjectionManager)
                    getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            requestScreenCapturePermissionLauncher.launch(mediaProjectionManager.createScreenCaptureIntent());
        }
    }

    private void startScreenCapture() {
        isScreenshare = true;

        if (localVideoTrack != null) {
            localVideoTrack.removeRenderer(localVideoView);
            if (localParticipant != null) {
                localParticipant.unpublishTrack(localVideoTrack);
            }
            localVideoTrack.release();
            localVideoTrack = null;
        }
        VideoConstraints videoConstraints = new VideoConstraints.Builder()
                .aspectRatio(AspectRatio.ASPECT_RATIO_16_9)
                .minVideoDimensions(VideoDimensions.CIF_VIDEO_DIMENSIONS)
                .maxVideoDimensions(VideoDimensions.HD_720P_VIDEO_DIMENSIONS)
                .minFps(5)
                .maxFps(20)
                .build();
        localVideoTrack = LocalVideoTrack.create(this, true, screenCapturer, videoConstraints);
        screenCaptureMenuItem.setIcon(R.drawable.ic_stop_screen_share_white_24dp);
        screenCaptureMenuItem.setTitle(R.string.stop_screen_share);


        localVideoTrack.addRenderer(primaryVideoView);
        localVideoView = primaryVideoView;
        if (localParticipant != null) {
            localParticipant.publishTrack(localVideoTrack);
        }
    }

    private void stopScreenCapture() {
        isScreenshare = false;
        if (localVideoTrack != null) {
            localVideoTrack.removeRenderer(localVideoView);
            if (localParticipant != null) {
                localParticipant.unpublishTrack(localVideoTrack);
            }

            localVideoTrack = null;
            screenCaptureMenuItem.setIcon(R.drawable.ic_screen_share_white_24dp);
            screenCaptureMenuItem.setTitle(R.string.share_screen);
            if (localVideoTrack == null && checkPermissionForCameraAndMicrophone()) {
                VideoConstraints videoConstraints = new VideoConstraints.Builder()
                        .aspectRatio(AspectRatio.ASPECT_RATIO_16_9)
                        .minVideoDimensions(VideoDimensions.CIF_VIDEO_DIMENSIONS)
                        .maxVideoDimensions(VideoDimensions.HD_720P_VIDEO_DIMENSIONS)
                        .minFps(5)
                        .maxFps(20)
                        .build();
                localVideoTrack = LocalVideoTrack.create(this,
                        true,
                        cameraCapturerCompat.getVideoCapturer(), videoConstraints,
                        LOCAL_VIDEO_TRACK_NAME);
                localVideoTrack.addRenderer(primaryVideoView);
                localVideoView = primaryVideoView;


                if (localParticipant != null) {
                    localParticipant.publishTrack(localVideoTrack);

                }

                if (isCameraOn) {
                    localVideoTrack.enable(true);
                } else {
                    localVideoTrack.enable(false);

                }
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_MIC_PERMISSION_REQUEST_CODE) {
            boolean cameraAndMicPermissionGranted = true;

            for (int grantResult : grantResults) {
                cameraAndMicPermissionGranted &= grantResult == PackageManager.PERMISSION_GRANTED;
            }

            if (cameraAndMicPermissionGranted) {
                createAudioAndVideoTracks();
                setAccessToken();
            } else {
                Toast.makeText(this,
                        R.string.permissions_needed,
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(phoneStateReceiver, new IntentFilter(PhoneStateReceiver.ACTION_PHONE), Context.RECEIVER_NOT_EXPORTED); // or Context.RECEIVER_EXPORTED if needed
        } else {
            registerReceiver(phoneStateReceiver, new IntentFilter(PhoneStateReceiver.ACTION_PHONE));
        }


        audioCodec = getAudioCodecPreference(SettingsActivity.PREF_AUDIO_CODEC,
                SettingsActivity.PREF_AUDIO_CODEC_DEFAULT);
        videoCodec = getVideoCodecPreference(SettingsActivity.PREF_VIDEO_CODEC,
                SettingsActivity.PREF_VIDEO_CODEC_DEFAULT);
        enableAutomaticSubscription = getAutomaticSubscriptionPreference(SettingsActivity.PREF_ENABLE_AUTOMATIC_SUBSCRIPTION,
                SettingsActivity.PREF_ENABLE_AUTOMATIC_SUBSCRIPTION_DEFAULT);

        final EncodingParameters newEncodingParameters = getEncodingParameters();


        if (localVideoTrack == null && checkPermissionForCameraAndMicrophone()) {

            VideoConstraints videoConstraints = new VideoConstraints.Builder()

                    .maxVideoDimensions(VideoDimensions.VGA_VIDEO_DIMENSIONS)
                    .minFps(5)
                    .maxFps(20)
                    .build();

            localVideoTrack = LocalVideoTrack.create(this,
                    true,
                    cameraCapturerCompat.getVideoCapturer(), videoConstraints,
                    LOCAL_VIDEO_TRACK_NAME);
            localVideoTrack.addRenderer(localVideoView);

            if (localParticipant != null) {
                localParticipant.publishTrack(localVideoTrack);


                if (!newEncodingParameters.equals(encodingParameters)) {
                    localParticipant.setEncodingParameters(newEncodingParameters);
                }
            }
        }


        encodingParameters = newEncodingParameters;


        if (room != null) {
            reconnectingProgressBar.setVisibility((room.getState() != Room.State.RECONNECTING) ?
                    View.GONE :
                    View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {

        if (!isScreenshare) {
            if (localVideoTrack != null) {

                if (localParticipant != null) {
                    localParticipant.unpublishTrack(localVideoTrack);
                }

                localVideoTrack.release();
                localVideoTrack = null;
            }

        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        audioSwitch.stop();
        setVolumeControlStream(savedVolumeControlStream);


        if (room != null && room.getState() != Room.State.DISCONNECTED) {
            room.disconnect();
            disconnectedFromOnDestroy = true;
        }
        dataTrackMessageThread.quit();
        if (localDataTrack != null) {
            localDataTrack.release();
            localDataTrack = null;
        }

        if (localAudioTrack != null) {
            localAudioTrack.release();
            localAudioTrack = null;
        }
        if (localVideoTrack != null) {
            localVideoTrack.release();
            localVideoTrack = null;
        }

        if (Build.VERSION.SDK_INT >= 29) {
            screenCapturerManager.unbindService();
        }
        super.onDestroy();
    }

    private boolean checkPermissionForCameraAndMicrophone() {
        int resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int resultPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        return resultCamera == PackageManager.PERMISSION_GRANTED &&
                resultMic == PackageManager.PERMISSION_GRANTED && resultPhoneState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionForCameraAndMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.RECORD_AUDIO) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE},
                    CAMERA_MIC_PERMISSION_REQUEST_CODE);

        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE},
                    CAMERA_MIC_PERMISSION_REQUEST_CODE);
        }
    }

    private void createAudioAndVideoTracks() {
        localAudioTrack = LocalAudioTrack.create(this, true, LOCAL_AUDIO_TRACK_NAME);

        cameraCapturerCompat = new CameraCapturerCompat(this, getAvailableCameraSource());
        VideoConstraints videoConstraints = new VideoConstraints.Builder()
                .maxVideoDimensions(VideoDimensions.VGA_VIDEO_DIMENSIONS)
                .minFps(5)
                .maxFps(20)
                .build();
        localVideoTrack = LocalVideoTrack.create(this,
                true,
                cameraCapturerCompat.getVideoCapturer(), videoConstraints,
                LOCAL_VIDEO_TRACK_NAME);
        primaryVideoView.setMirror(true);
        localVideoTrack.addRenderer(primaryVideoView);
        localVideoView = primaryVideoView;

    }

    private CameraCapturer.CameraSource getAvailableCameraSource() {
        return (CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.FRONT_CAMERA)) ?
                (CameraCapturer.CameraSource.FRONT_CAMERA) :
                (CameraCapturer.CameraSource.BACK_CAMERA);
    }

    private void setAccessToken() {
        if (getIntent().getExtras() != null) {
            accessToken = getIntent().getExtras().getString("accessToken");
            roomName = getIntent().getExtras().getString("roomName");
        }

    }

    private void connectToRoom(String roomName) {
        audioSwitch.activate();
        localDataTrack = LocalDataTrack.create(this);
        dataTrackMessageThread.start();
        dataTrackMessageThreadHandler = new Handler(dataTrackMessageThread.getLooper());
        NetworkQualityConfiguration configuration =
                new NetworkQualityConfiguration(
                        NetworkQualityVerbosity.NETWORK_QUALITY_VERBOSITY_MINIMAL,
                        NetworkQualityVerbosity.NETWORK_QUALITY_VERBOSITY_MINIMAL);
        ConnectOptions.Builder connectOptionsBuilder = new ConnectOptions.Builder(accessToken)
                .roomName(roomName)
                .enableNetworkQuality(true)
                .networkQualityConfiguration(configuration);

        if (localAudioTrack != null) {
            connectOptionsBuilder
                    .audioTracks(Collections.singletonList(localAudioTrack));
        }
        if (localVideoTrack != null) {
            connectOptionsBuilder.videoTracks(Collections.singletonList(localVideoTrack));
        }
        connectOptionsBuilder.preferAudioCodecs(Collections.singletonList(audioCodec));
        connectOptionsBuilder.preferVideoCodecs(Collections.singletonList(videoCodec));
        connectOptionsBuilder.dataTracks(Collections.singletonList(localDataTrack));
        connectOptionsBuilder.encodingParameters(new EncodingParameters(16, 0));

        connectOptionsBuilder.enableAutomaticSubscription(enableAutomaticSubscription);

        room = Video.connect(this, connectOptionsBuilder.build(), roomListener());

        setDisconnectAction();
    }

    private void intializeUI() {
        connectActionFab.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_video_call_white_24dp));
        connectActionFab.show();
        connectActionFab.setOnClickListener(connectActionClickListener());
        switchCameraActionFab.show();
        switchCameraActionFab.setOnClickListener(switchCameraClickListener());
        localVideoActionFab.show();
        localVideoActionFab.setOnClickListener(localVideoClickListener());
        muteActionFab.show();
        muteActionFab.setOnClickListener(muteClickListener());
        remoteParticipantList.clear();
        thumbnailAdapter = new ThumbnailAdapter();
        thumbnail_recyclerview.setAdapter(thumbnailAdapter);
    }

    private void showAudioDevices() {
        AudioDevice selectedDevice = audioSwitch.getSelectedAudioDevice();
        List<AudioDevice> availableAudioDevices = audioSwitch.getAvailableAudioDevices();

        if (selectedDevice != null) {
            int selectedDeviceIndex = availableAudioDevices.indexOf(selectedDevice);

            ArrayList<String> audioDeviceNames = new ArrayList<>();
            for (AudioDevice a : availableAudioDevices) {
                audioDeviceNames.add(a.getName());
            }

            new AlertDialog.Builder(this)
                    .setTitle(R.string.room_screen_select_device)
                    .setSingleChoiceItems(
                            audioDeviceNames.toArray(new CharSequence[0]),
                            selectedDeviceIndex,
                            (dialog, index) -> {
                                dialog.dismiss();
                                AudioDevice selectedAudioDevice = availableAudioDevices.get(index);
                                updateAudioDeviceIcon(selectedAudioDevice);
                                audioSwitch.selectDevice(selectedAudioDevice);
                            }).create().show();
        }
    }


    private void updateAudioDeviceIcon(AudioDevice selectedAudioDevice) {
        int audioDeviceMenuIcon = R.drawable.ic_phonelink_ring_white_24dp;

        if (selectedAudioDevice instanceof AudioDevice.BluetoothHeadset) {
            audioDeviceMenuIcon = R.drawable.ic_bluetooth_white_24dp;
        } else if (selectedAudioDevice instanceof AudioDevice.WiredHeadset) {
            audioDeviceMenuIcon = R.drawable.ic_headset_mic_white_24dp;
        } else if (selectedAudioDevice instanceof AudioDevice.Earpiece) {
            audioDeviceMenuIcon = R.drawable.ic_phonelink_ring_white_24dp;
        } else if (selectedAudioDevice instanceof AudioDevice.Speakerphone) {
            audioDeviceMenuIcon = R.drawable.ic_volume_up_white_24dp;
        }

        audioDeviceMenuItem.setIcon(audioDeviceMenuIcon);
    }

    private AudioCodec getAudioCodecPreference(String key, String defaultValue) {
        final String audioCodecName = preferences.getString(key, defaultValue);
        switch (audioCodecName) {
            case IsacCodec.NAME:
                return new IsacCodec();
            case OpusCodec.NAME:
                return new OpusCodec();
            case PcmaCodec.NAME:
                return new PcmaCodec();
            case PcmuCodec.NAME:
                return new PcmuCodec();
            case G722Codec.NAME:
                return new G722Codec();
            default:
                return new OpusCodec();
        }
    }

    private VideoCodec getVideoCodecPreference(String key, String defaultValue) {
        final String videoCodecName = preferences.getString(key, defaultValue);

        switch (videoCodecName) {
            case Vp8Codec.NAME:
                boolean simulcast = preferences.getBoolean(SettingsActivity.PREF_VP8_SIMULCAST,
                        SettingsActivity.PREF_VP8_SIMULCAST_DEFAULT);
                return new Vp8Codec(simulcast);
            case H264Codec.NAME:
                return new H264Codec();
            case Vp9Codec.NAME:
                return new Vp9Codec();
            default:
                return new Vp8Codec();
        }
    }

    private boolean getAutomaticSubscriptionPreference(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    private EncodingParameters getEncodingParameters() {
        final int maxAudioBitrate = Integer.parseInt(
                preferences.getString(SettingsActivity.PREF_SENDER_MAX_AUDIO_BITRATE,
                        SettingsActivity.PREF_SENDER_MAX_AUDIO_BITRATE_DEFAULT));
        final int maxVideoBitrate = Integer.parseInt(
                preferences.getString(SettingsActivity.PREF_SENDER_MAX_VIDEO_BITRATE,
                        SettingsActivity.PREF_SENDER_MAX_VIDEO_BITRATE_DEFAULT));

        return new EncodingParameters(maxAudioBitrate, maxVideoBitrate);
    }

    private void setDisconnectAction() {
        connectActionFab.setImageDrawable(ContextCompat.getDrawable(this,
                R.drawable.ic_call_end_white_24px));
        connectActionFab.show();
        connectActionFab.setOnClickListener(disconnectClickListener());
    }

    private void showConnectDialog() {
        EditText roomEditText = new EditText(this);

        connectDialog = Dialog.createConnectDialog(roomEditText,
                connectClickListener(roomEditText),
                cancelConnectDialogClickListener(),
                this);
        connectDialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void addRemoteParticipant(RemoteParticipant remoteParticipant) {

        remoteParticipantIdentity = remoteParticipant.getIdentity();

        remoteParticipantList.add(remoteParticipant);
        if (participantListAdapter != null) {
            participantListAdapter.notifyDataSetChanged();
        }
        thumbnailAdapter.addThumbnail(remoteParticipant);

        if (remoteParticipant.getRemoteVideoTracks().size() > 0) {

            RemoteVideoTrackPublication remoteVideoTrackPublication =
                    remoteParticipant.getRemoteVideoTracks().get(0);
        }

        remoteParticipant.setListener(remoteParticipantListener());
        for (final RemoteDataTrackPublication remoteDataTrackPublication :
                remoteParticipant.getRemoteDataTracks()) {
            if (remoteDataTrackPublication.isTrackSubscribed()) {
                dataTrackMessageThreadHandler.post(() -> addRemoteDataTrack(remoteParticipant,
                        remoteDataTrackPublication.getRemoteDataTrack()));
            }
        }
        video_status_textview.setText(String.valueOf(room.getRemoteParticipants().size()));
    }

    private void addRemoteDataTrack(RemoteParticipant remoteParticipant,
                                    RemoteDataTrack remoteDataTrack) {
        dataTrackRemoteParticipantMap.put(remoteDataTrack, remoteParticipant);
        remoteDataTrack.setListener(remoteDataTrackListener());

        if (whiteboard_dialog != null && whiteboard_dialog.isShowing()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Show White Board", true);
                localDataTrack.send(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private void removeRemoteParticipant(RemoteParticipant remoteParticipant) {

        for (int i = 0; i < remoteParticipantList.size(); i++) {

            if (remoteParticipant.getIdentity().equals(remoteParticipantList.get(i).getIdentity())) {
                if (!remoteParticipant.isConnected()) {
                    remoteParticipantList.remove(remoteParticipantList.get(i));
                    thumbnailAdapter.notifyItemRemoved(i);
                    if (participantListAdapter != null) {
                        participantListAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
        moveLocalVideoToPrimaryView();
    }

    private void moveLocalVideoToPrimaryView() {

        if (remoteParticipantList.size() == 0) {
            if (localVideoTrack != null) {
                thumbnailAdapter.notifyDataSetChanged();
                localVideoTrack.addRenderer(primaryVideoView);
            }
            localVideoView = primaryVideoView;
            primaryVideoView.setMirror(cameraCapturerCompat.getCameraSource() ==
                    CameraCapturer.CameraSource.FRONT_CAMERA);
        }

    }

    @SuppressLint("SetTextI18n")
    private Room.Listener roomListener() {
        return new Room.Listener() {
            @Override
            public void onConnected(Room room) {
                localParticipant = room.getLocalParticipant();
                localParticipant.publishTrack(localDataTrack);
                setTitle(room.getName());

                for (RemoteParticipant remoteParticipant : room.getRemoteParticipants()) {
                    addRemoteParticipant(remoteParticipant);
                }

            }

            @Override
            public void onReconnecting(@NonNull Room room, @NonNull TwilioException twilioException) {
                reconnectingProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReconnected(@NonNull Room room) {
                reconnectingProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onConnectFailure(Room room, TwilioException e) {
                Toast.makeText(VideoActivity.this, e.getExplanation(), Toast.LENGTH_LONG).show();
                audioSwitch.deactivate();
                intializeUI();
            }

            @Override
            public void onDisconnected(Room room, TwilioException e) {
                localParticipant = null;
                reconnectingProgressBar.setVisibility(View.GONE);
                VideoActivity.this.room = null;
                if (!disconnectedFromOnDestroy) {
                    audioSwitch.deactivate();
                    intializeUI();
                    moveLocalVideoToPrimaryView();
                    finish();
                }
            }


            @Override
            public void onParticipantConnected(Room room, RemoteParticipant remoteParticipant) {
                localParticipant.publishTrack(localDataTrack);
                addRemoteParticipant(remoteParticipant);
                Toast.makeText(VideoActivity.this, remoteParticipant.getIdentity() + " has Joined", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onParticipantDisconnected(Room room, RemoteParticipant remoteParticipant) {
                removeRemoteParticipant(remoteParticipant);
                for (InvitesModel.Datum datum : invitedStudentList) {
                    if (remoteParticipant.getIdentity().equalsIgnoreCase(datum.getUserFullname())) {
                        video_status_textview.setText(String.valueOf(room.getRemoteParticipants().size()));
                        break;
                    }
                }

                Toast.makeText(VideoActivity.this, remoteParticipant.getIdentity() + " has left", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onRecordingStarted(Room room) {

                Log.d(TAG, "onRecordingStarted");
            }

            @Override
            public void onRecordingStopped(Room room) {

                Log.d(TAG, "onRecordingStopped");
            }
        };
    }

    @SuppressLint("SetTextI18n")
    private RemoteParticipant.Listener remoteParticipantListener() {
        return new RemoteParticipant.Listener() {
            @Override
            public void onAudioTrackPublished(RemoteParticipant remoteParticipant,
                                              RemoteAudioTrackPublication remoteAudioTrackPublication) {
                Log.i(TAG, String.format("onAudioTrackPublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrackPublication.getTrackSid(),
                        remoteAudioTrackPublication.isTrackEnabled(),
                        remoteAudioTrackPublication.isTrackSubscribed(),
                        remoteAudioTrackPublication.getTrackName()));
            }

            @Override
            public void onAudioTrackUnpublished(RemoteParticipant remoteParticipant,
                                                RemoteAudioTrackPublication remoteAudioTrackPublication) {
                Log.i(TAG, String.format("onAudioTrackUnpublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrackPublication.getTrackSid(),
                        remoteAudioTrackPublication.isTrackEnabled(),
                        remoteAudioTrackPublication.isTrackSubscribed(),
                        remoteAudioTrackPublication.getTrackName()));
            }

            @Override
            public void onDataTrackPublished(RemoteParticipant remoteParticipant,
                                             RemoteDataTrackPublication remoteDataTrackPublication) {
                Log.i(TAG, String.format("onDataTrackPublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrackPublication.getTrackSid(),
                        remoteDataTrackPublication.isTrackEnabled(),
                        remoteDataTrackPublication.isTrackSubscribed(),
                        remoteDataTrackPublication.getTrackName()));
            }

            @Override
            public void onDataTrackUnpublished(RemoteParticipant remoteParticipant,
                                               RemoteDataTrackPublication remoteDataTrackPublication) {
                Log.i(TAG, String.format("onDataTrackUnpublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrackPublication.getTrackSid(),
                        remoteDataTrackPublication.isTrackEnabled(),
                        remoteDataTrackPublication.isTrackSubscribed(),
                        remoteDataTrackPublication.getTrackName()));
            }

            @Override
            public void onVideoTrackPublished(RemoteParticipant remoteParticipant,
                                              RemoteVideoTrackPublication remoteVideoTrackPublication) {
                Log.i(TAG, String.format("onVideoTrackPublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrackPublication.getTrackSid(),
                        remoteVideoTrackPublication.isTrackEnabled(),
                        remoteVideoTrackPublication.isTrackSubscribed(),
                        remoteVideoTrackPublication.getTrackName()));
            }

            @Override
            public void onVideoTrackUnpublished(RemoteParticipant remoteParticipant,
                                                RemoteVideoTrackPublication remoteVideoTrackPublication) {
                Log.i(TAG, String.format("onVideoTrackUnpublished: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrackPublication: sid=%s, enabled=%b, " +
                                "subscribed=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrackPublication.getTrackSid(),
                        remoteVideoTrackPublication.isTrackEnabled(),
                        remoteVideoTrackPublication.isTrackSubscribed(),
                        remoteVideoTrackPublication.getTrackName()));
            }

            @Override
            public void onAudioTrackSubscribed(RemoteParticipant remoteParticipant,
                                               RemoteAudioTrackPublication remoteAudioTrackPublication,
                                               RemoteAudioTrack remoteAudioTrack) {
                Log.i(TAG, String.format("onAudioTrackSubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrack: enabled=%b, playbackEnabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrack.isEnabled(),
                        remoteAudioTrack.isPlaybackEnabled(),
                        remoteAudioTrack.getName()));
            }

            @Override
            public void onAudioTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                 RemoteAudioTrackPublication remoteAudioTrackPublication,
                                                 RemoteAudioTrack remoteAudioTrack) {
                Log.i(TAG, String.format("onAudioTrackUnsubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrack: enabled=%b, playbackEnabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrack.isEnabled(),
                        remoteAudioTrack.isPlaybackEnabled(),
                        remoteAudioTrack.getName()));
            }

            @Override
            public void onAudioTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                       RemoteAudioTrackPublication remoteAudioTrackPublication,
                                                       TwilioException twilioException) {
                Log.i(TAG, String.format("onAudioTrackSubscriptionFailed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteAudioTrackPublication: sid=%b, name=%s]" +
                                "[TwilioException: code=%d, message=%s]",
                        remoteParticipant.getIdentity(),
                        remoteAudioTrackPublication.getTrackSid(),
                        remoteAudioTrackPublication.getTrackName(),
                        twilioException.getCode(),
                        twilioException.getMessage()));
            }

            @Override
            public void onDataTrackSubscribed(RemoteParticipant remoteParticipant,
                                              RemoteDataTrackPublication remoteDataTrackPublication,
                                              RemoteDataTrack remoteDataTrack) {
                Log.i(TAG, String.format("onDataTrackSubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrack: enabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrack.isEnabled(),
                        remoteDataTrack.getName()));
                dataTrackMessageThreadHandler.post(() -> addRemoteDataTrack(remoteParticipant, remoteDataTrack));

            }

            @Override
            public void onDataTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                RemoteDataTrackPublication remoteDataTrackPublication,
                                                RemoteDataTrack remoteDataTrack) {
                Log.i(TAG, String.format("onDataTrackUnsubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrack: enabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrack.isEnabled(),
                        remoteDataTrack.getName()));
            }

            @Override
            public void onDataTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                      RemoteDataTrackPublication remoteDataTrackPublication,
                                                      TwilioException twilioException) {
                Log.i(TAG, String.format("onDataTrackSubscriptionFailed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteDataTrackPublication: sid=%b, name=%s]" +
                                "[TwilioException: code=%d, message=%s]",
                        remoteParticipant.getIdentity(),
                        remoteDataTrackPublication.getTrackSid(),
                        remoteDataTrackPublication.getTrackName(),
                        twilioException.getCode(),
                        twilioException.getMessage()));
            }

            @Override
            public void onVideoTrackSubscribed(RemoteParticipant remoteParticipant,
                                               RemoteVideoTrackPublication remoteVideoTrackPublication,
                                               RemoteVideoTrack remoteVideoTrack) {
                Log.i(TAG, String.format("onVideoTrackSubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrack: enabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrack.isEnabled(),
                        remoteVideoTrack.getName()));

                if (remoteParticipant.isConnected()) {
                    thumbnailAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onVideoTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                 RemoteVideoTrackPublication remoteVideoTrackPublication,
                                                 RemoteVideoTrack remoteVideoTrack) {
                Log.i(TAG, String.format("onVideoTrackUnsubscribed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrack: enabled=%b, name=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrack.isEnabled(),
                        remoteVideoTrack.getName()));
                if (remoteParticipant.isConnected()) {
                    thumbnailAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onVideoTrackSubscriptionFailed(RemoteParticipant remoteParticipant,
                                                       RemoteVideoTrackPublication remoteVideoTrackPublication,
                                                       TwilioException twilioException) {
                Log.i(TAG, String.format("onVideoTrackSubscriptionFailed: " +
                                "[RemoteParticipant: identity=%s], " +
                                "[RemoteVideoTrackPublication: sid=%b, name=%s]" +
                                "[TwilioException: code=%d, message=%s]",
                        remoteParticipant.getIdentity(),
                        remoteVideoTrackPublication.getTrackSid(),
                        remoteVideoTrackPublication.getTrackName(),
                        twilioException.getCode(),
                        twilioException.getMessage()));
                Snackbar.make(connectActionFab,
                        String.format("Failed to subscribe to %s video track",
                                remoteParticipant.getIdentity()),
                        Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onAudioTrackEnabled(RemoteParticipant remoteParticipant,
                                            RemoteAudioTrackPublication remoteAudioTrackPublication) {
                Log.d("Message ", "Audio Enabled");
                if (remoteParticipant.isConnected()) {
                    thumbnailAdapter.notifyDataSetChanged();
                }

            }


            @Override
            public void onAudioTrackDisabled(RemoteParticipant remoteParticipant,
                                             RemoteAudioTrackPublication remoteAudioTrackPublication) {
                Log.d("Message ", "Audio Disabled");
                if (remoteParticipant.isConnected()) {
                    thumbnailAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onVideoTrackEnabled(RemoteParticipant remoteParticipant,
                                            RemoteVideoTrackPublication remoteVideoTrackPublication) {
                Log.d("Message ", "Video Disabled");
                if (remoteParticipant.isConnected()) {
                    thumbnailAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onVideoTrackDisabled(RemoteParticipant remoteParticipant,
                                             RemoteVideoTrackPublication remoteVideoTrackPublication) {
                Log.d("Message ", "Video Enabled");
                if (remoteParticipant.isConnected()) {
                    thumbnailAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onNetworkQualityLevelChanged(@NonNull RemoteParticipant remoteParticipant, @NonNull NetworkQualityLevel networkQualityLevel) {

            }
        };
    }

    public void showPrimaryView(RemoteParticipant remoteParticipant) {

        AlertDialog.Builder builder
                = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.participant_primary_view,
                null);
        builder.setView(customLayout);

        VideoTextureView video_view = customLayout.findViewById(R.id.video);
        video_view.setVideoScaleType(VideoScaleType.ASPECT_FIT);
        if (remoteParticipant.getRemoteVideoTracks().size() > 0) {
            RemoteVideoTrackPublication remoteVideoTrackPublication =
                    remoteParticipant.getRemoteVideoTracks().get(0);

            if (remoteVideoTrackPublication.isTrackSubscribed()) {
                remoteVideoTrackPublication.getVideoTrack().addRenderer(video_view);


            }
        }

        AlertDialog dialog
                = builder.create();
        dialog.show();


    }

    public void showParticipantList() {

        AlertDialog.Builder builder
                = new AlertDialog.Builder(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        final View customLayout = getLayoutInflater().inflate(R.layout.recyclerview,
                null);
        builder.setView(customLayout);
        RecyclerView recyclerView = customLayout.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(false);
        participantListAdapter = new ParticipantListAdapter();
        recyclerView.setAdapter(participantListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        AlertDialog dialog
                = builder.create();
        dialog.show();
    }

    private DialogInterface.OnClickListener connectClickListener(final EditText roomEditText) {
        return (dialog, which) -> {

        };
    }

    private View.OnClickListener disconnectClickListener() {
        return v -> {

            recordvideoclass();
        };
    }

    private View.OnClickListener connectActionClickListener() {
        return v -> showConnectDialog();
    }

    private DialogInterface.OnClickListener cancelConnectDialogClickListener() {
        return (dialog, which) -> {
            intializeUI();
            connectDialog.dismiss();
        };
    }

    private View.OnClickListener switchCameraClickListener() {
        return v -> {
            if (cameraCapturerCompat != null) {
                CameraCapturer.CameraSource cameraSource = cameraCapturerCompat.getCameraSource();
                cameraCapturerCompat.switchCamera();
                thumbnailAdapter.notifyItemChanged(0);
                primaryVideoView.setMirror(cameraSource == CameraCapturer.CameraSource.BACK_CAMERA);


            }
        };
    }

    private View.OnClickListener localVideoClickListener() {
        return v -> {

            if (localVideoTrack != null) {
                boolean enable = !localVideoTrack.isEnabled();
                isCameraOn = enable;
                localVideoTrack.enable(enable);
                int icon;
                if (enable) {
                    icon = R.drawable.ic_videocam_white_24dp;
                    switchCameraActionFab.show();
                } else {
                    icon = R.drawable.ic_videocam_off_black_24dp;
                    switchCameraActionFab.hide();
                }
                localVideoActionFab.setImageDrawable(
                        ContextCompat.getDrawable(VideoActivity.this, icon));
            }
        };
    }

    private View.OnClickListener muteClickListener() {
        return v -> {

            if (localAudioTrack != null) {
                boolean enable = !localAudioTrack.isEnabled();
                localAudioTrack.enable(enable);
                int icon = enable ?
                        R.drawable.ic_mic_white_24dp  : R.drawable.ic_mic_off_black_24dp;
                muteActionFab.setImageDrawable(ContextCompat.getDrawable(
                        VideoActivity.this, icon));
            }
        };
    }

    private class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.MyViewHoler> {
        private final Map<MyViewHoler, RemoteVideoTrack> viewHolderMap = new HashMap<>();

        @NonNull
        @Override
        public ThumbnailAdapter.MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_thumbnail, parent, false);
            return new ThumbnailAdapter.MyViewHoler(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ThumbnailAdapter.MyViewHoler holder, int position) {

            if (viewHolderMap.containsKey(holder)) {
                viewHolderMap.get(holder).removeRenderer(holder.thumbnail_video_view);
            }

            holder.participant_name.setText(remoteParticipantList.get(position).getIdentity());

            if (remoteParticipantList.get(position).getRemoteVideoTracks().size() > 0) {
                RemoteVideoTrackPublication remoteVideoTrackPublication =
                        remoteParticipantList.get(position).getRemoteVideoTracks().get(0);
                if (!remoteVideoTrackPublication.isTrackEnabled()) {
                    holder.video_disabled.setVisibility(View.VISIBLE);
                } else {
                    holder.video_disabled.setVisibility(View.GONE);
                }

                if (remoteVideoTrackPublication.isTrackSubscribed()) {
                    remoteVideoTrackPublication.getVideoTrack().addRenderer(holder.thumbnail_video_view);
                    viewHolderMap.put(holder, remoteVideoTrackPublication.getRemoteVideoTrack());

                    holder.progressBar.setVisibility(View.GONE);

                } else {

                    holder.progressBar.setVisibility(View.VISIBLE);

                }

            }
            RemoteAudioTrackPublication remoteAudioTrackPublication = remoteParticipantList.get(position).getRemoteAudioTracks().get(0);
            if (remoteAudioTrackPublication.getRemoteAudioTrack() != null) {
                if (remoteAudioTrackPublication.getAudioTrack().isEnabled()) {
                    if (remoteAudioTrackPublication.getRemoteAudioTrack().isPlaybackEnabled()) {

                        holder.mute_audio.setImageResource(R.drawable.ic_mic_white_24dp);


                    } else {
                        holder.mute_audio.setImageResource(R.drawable.ic_mic_off_black_24dp);

                    }
                } else {
                    holder.mute_audio.setImageResource(R.drawable.ic_mic_off_black_24dp);
                }
            }

            holder.mute_audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jsonObject = new JSONObject();

                    if (remoteAudioTrackPublication.getRemoteAudioTrack().isPlaybackEnabled()) {
                        holder.mute_audio.setImageResource(R.drawable.ic_mic_off_black_24dp);
                        try {
                            jsonObject.put("Individual Muted", true);
                            Log.d(TAG, jsonObject+"teacherapp");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        remoteAudioTrackPublication.getRemoteAudioTrack().enablePlayback(false);
                    } else {

                        holder.mute_audio.setImageResource(R.drawable.ic_mic_white_24dp);
                        try {
                            jsonObject.put("Individual UnMuted", true);
                            Log.d(TAG, jsonObject+"teacherapp");
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        remoteAudioTrackPublication.getRemoteAudioTrack().enablePlayback(true);
                    }
                 localDataTrack.send(jsonObject.toString());
                }

            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPrimaryView(remoteParticipantList.get(holder.getBindingAdapterPosition()));
                }
            });

        }

        @Override
        public int getItemCount() {
            return remoteParticipantList.size();
        }

        public class MyViewHoler extends RecyclerView.ViewHolder {
            VideoTextureView thumbnail_video_view;
            ProgressBar progressBar;
            TextView text_onanothercall, text_strength, participant_name;
            ImageView video_disabled, mute_audio;

            public MyViewHoler(@NonNull View itemView) {
                super(itemView);
                thumbnail_video_view = itemView.findViewById(R.id.thumbnail_video_view);
                progressBar = itemView.findViewById(R.id.progressBar);
                text_onanothercall = itemView.findViewById(R.id.text_onanothercall);
                video_disabled = itemView.findViewById(R.id.video_disabled);
                mute_audio = itemView.findViewById(R.id.mute_audio);
                text_strength = itemView.findViewById(R.id.text_strength);
                participant_name = itemView.findViewById(R.id.participant_name);
            }
        }

        public void addThumbnail(RemoteParticipant remoteParticipant) {

            thumbnailAdapter.notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }

    public List<byte[]> getFileChunks(byte[] mainFile) {

        int sizeMB = 16000;
        List<byte[]> chunks = new ArrayList<>();
        for (int i = 0; i < mainFile.length; ) {
            byte[] chunk = new byte[Math.min(sizeMB, mainFile.length - i)];
            for (int j = 0; j < chunk.length; j++, i++) {
                chunk[j] = mainFile[i];
            }
            localDataTrack.send(ByteBuffer.wrap(chunk));
            chunks.add(chunk);

        }
        JSONObject sharefile = new JSONObject();
        try {
            sharefile.put("FileShare", "Done!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        localDataTrack.send(sharefile.toString());
        return chunks;
    }

    private byte[] getBytes(InputStream iStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = iStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);

        }
        return byteBuffer.toByteArray();
    }

    private RemoteDataTrack.Listener remoteDataTrackListener() {
        return new RemoteDataTrack.Listener() {
            @Override
            public void onMessage(RemoteDataTrack remoteDataTrack, ByteBuffer byteBuffer) {
                Log.d(TAG, "onMessage: " + byteBuffer);
            }

            @Override
            public void onMessage(RemoteDataTrack remoteDataTrack, String message) {
                Log.d(TAG, "onMessage: " + message);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonObject.has("chat")) {
                    try {
                        JSONObject object = jsonObject.getJSONObject("chat");
                        Gson gson = new Gson();
                        ChatMessage chatMessage = gson.fromJson(object.toString(), ChatMessage.class);
                        chatMessage.setType(ChatMessage.Type.RECEIVED);

                        Drawable drawable = chatMenuItem.getIcon();
                        drawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

                        if (chatFragment == null) {
                            chatMessages.add(chatMessage);
                        } else {
                            chatMessages.add(chatMessage);
                            if (chatFragment.isVisible()) {
                                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                                chatFragment.addMessage(chatMessage);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (jsonObject.has("Delete Chat")) {
                    try {
                        JSONObject object = jsonObject.getJSONObject("Delete Chat");
                        Gson gson = new Gson();
                        ChatMessage chatMessage = gson.fromJson(object.toString(), ChatMessage.class);
                        chatMessage.setType(ChatMessage.Type.RECEIVED);
                        if (chatFragment == null) {
                            for (int i = 0; i < chatMessages.size(); i++) {
                                if (chatMessage.getTimestamp() == (chatMessages.get(i).getTimestamp())) {
                                    chatMessages.remove(i);
                                    break;
                                }
                            }
                        } else {
                            if (chatFragment.isVisible()) {
                                chatFragment.removeMessage(chatMessage);
                            } else {
                                for (int i = 0; i < chatMessages.size(); i++) {
                                    if (chatMessage.getTimestamp() == (chatMessages.get(i).getTimestamp())) {
                                        chatMessages.remove(i);
                                        break;
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private class ParticipantListAdapter extends RecyclerView.Adapter<ParticipantListAdapter.MyViewHolder> {

        @NonNull
        @Override
        public ParticipantListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_partcipant_list, parent, false);
            return new ParticipantListAdapter.MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ParticipantListAdapter.MyViewHolder holder, int position) {

            holder.name.setText(invitedStudentList.get(position).getUserFullname());
            holder.image_online.setColorFilter(Color.RED);
            holder.image_mute.setVisibility(View.GONE);

            try {
                thumbnailAdapter.notifyDataSetChanged();
                notifyItemChanged(position);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < remoteParticipantList.size(); i++) {
                if (invitedStudentList.get(position).getUserFullname().equalsIgnoreCase(remoteParticipantList.get(i).getIdentity())) {

                    holder.image_online.setColorFilter(Color.GREEN);
                    holder.image_mute.setVisibility(View.VISIBLE);

                    RemoteAudioTrackPublication remoteAudioTrackPublication = remoteParticipantList.get(i).getRemoteAudioTracks().get(0);
                    if (remoteAudioTrackPublication.getRemoteAudioTrack() != null) {
                        if (remoteAudioTrackPublication.getAudioTrack().isEnabled()) {
                            if (remoteAudioTrackPublication.getRemoteAudioTrack().isPlaybackEnabled()) {

                                holder.image_mute.setImageResource(R.drawable.ic_mic_white_24dp);
                            } else {
                                holder.image_mute.setImageResource(R.drawable.ic_mic_off_black_24dp);
                            }
                        } else {
                            holder.image_mute.setImageResource(R.drawable.ic_mic_off_black_24dp);
                        }
                    }
                    break;
                }
            }

            holder.image_mute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < remoteParticipantList.size(); i++) {
                        if (invitedStudentList.get(position).getUserFullname().equalsIgnoreCase(remoteParticipantList.get(i).getIdentity())) {
                            RemoteAudioTrackPublication remoteAudioTrackPublication = remoteParticipantList.get(i).getRemoteAudioTracks().get(0);

                            JSONObject jsonObject = new JSONObject();

                            if (remoteAudioTrackPublication.getRemoteAudioTrack().isPlaybackEnabled())
                            {
                                try {
                                    jsonObject.put("Individual Mute2",true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                holder.image_mute.setImageResource(R.drawable.ic_mic_off_black_24dp);

                                remoteAudioTrackPublication.getRemoteAudioTrack().enablePlayback(false);
                            } else {
                                try {
                                    jsonObject.put("Individual UnMute2",true);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                holder.image_mute.setImageResource(R.drawable.ic_mic_white_24dp);
                                remoteAudioTrackPublication.getRemoteAudioTrack().enablePlayback(true);
                            }
                            localDataTrack.send(jsonObject.toString());
                            thumbnailAdapter.notifyDataSetChanged();
                            break;
                        }
                    }

                }
            });

        }

        @Override
        public int getItemCount() {
            return invitedStudentList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            ImageView image_mute, image_online;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                image_mute = itemView.findViewById(R.id.image_mute);
                image_online = itemView.findViewById(R.id.image_online);
            }
        }
    }

    private void recordvideoclass() {
        userDetailsSharedPref = UserDetailsSharedPref.getInstance(this);
        api = ApiClient.getClient().create(Api.class);
        headermap = new HashMap<>();
        headermap.put("Authorization", "Bearer " + userDetailsSharedPref.getString("token"));
        headermap.put("Accept", "application/json");


        Call<ApiSuccessModel> call = api.getvideorecord(headermap, conferenceid);
        call.enqueue(new Callback<ApiSuccessModel>() {
            @Override
            public void onResponse(Call<ApiSuccessModel> call, Response<ApiSuccessModel> response) {
                if (response.isSuccessful()) {
                    if (room != null) {
                        room.disconnect();
                    }
                    setResult(RESULT_OK);
                    intializeUI();


                } else if (response.code() == 302) {
                    try {
                        String s = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(s);
                        String error = jsonObject.getString("errors");
                        JSONObject jsonObject1 = new JSONObject(error);
                        Iterator keys = jsonObject1.keys();
                        while (keys.hasNext()) {
                            Object key = keys.next();
                            JSONArray value = jsonObject1.getJSONArray((String) key);
                            String errormessage = value.getString(0);
                            Toast.makeText(VideoActivity.this,errormessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<ApiSuccessModel> call, Throwable t) {
                Toast.makeText(VideoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}