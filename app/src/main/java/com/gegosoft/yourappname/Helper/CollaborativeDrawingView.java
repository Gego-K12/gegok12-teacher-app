package com.gegosoft.yourappname.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.twilio.video.RemoteParticipant;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CollaborativeDrawingView extends View {
    private static final float STROKE_WIDTH = 5f;
    private static final String TAG = "DrawingView";
    private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;

    private float lastTouchX;
    private float lastTouchY;
    private final RectF dirtyRect = new RectF();

    private Canvas mDrawCanvas;
    private Bitmap mCanvasBitmap;

    private Listener listener;

    private Path mDrawPath;
    private Paint mBackgroundPaint,canvasPaint;
    private Paint mDrawPaint;
    private int mBackgroundColor = 0xFFFFFFFF;
    private int mPaintColor = 0xFF660000;
    private int mStrokeWidth = 10;
    private boolean erase=false;

    private final Map<RemoteParticipant, Pair<Path, Paint>> remoteParticipantPalettes =
            new HashMap<>();

    public CollaborativeDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init()
    {
        mDrawPath = new Path();
        mBackgroundPaint = new Paint();
        initPaint();
    }
    private void initPaint()
    {
        mDrawPaint = new Paint();
        mDrawPaint.setColor(mPaintColor);
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setStrokeWidth(mStrokeWidth);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        mDrawCanvas = new Canvas(mCanvasBitmap);
    }

    public Bitmap getBitmap()
    {
        drawBackground(mDrawCanvas);
        drawPaths(mDrawCanvas);
        return mCanvasBitmap;
    }
    private void drawBackground(Canvas canvas)
    {
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), mBackgroundPaint);
    }

    public void setListener(@Nullable Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isEnabled()) {
            return;
        }

        drawBackground(canvas);
        canvas.drawBitmap(mCanvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(mDrawPath, mDrawPaint);

        for (Pair<Path, Paint> remoteParticipantPalette : remoteParticipantPalettes.values()) {
            remoteParticipantPalette.second.setColor(mPaintColor);
            canvas.drawPath(remoteParticipantPalette.first, remoteParticipantPalette.second);

        }
    }

    private void drawPaths(Canvas canvas) {
        canvas.drawPath(mDrawPath, mDrawPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }

        float eventX = event.getX();
        float eventY = event.getY();
        int actionEvent = event.getAction();

        if (listener != null) {
            listener.onTouchEvent(actionEvent, eventX, eventY);
        }

        switch (actionEvent) {
            case MotionEvent.ACTION_DOWN:
                mDrawPath.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY = eventY;
                return true;
            case MotionEvent.ACTION_MOVE:
                mDrawPath.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:
                mDrawCanvas.drawPath(mDrawPath, mDrawPaint);
                mDrawPath.reset();
                break;
            default:
                Log.d(TAG, "Ignored touch event: " + event.toString());
                return false;
        }

        invalidate(
                (int) (dirtyRect.left - HALF_STROKE_WIDTH),
                (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));
        lastTouchX = eventX;
        lastTouchY = eventY;
        return true;
    }

    private void expandDirtyRect(float historicalX, float historicalY) {
        if (historicalX < dirtyRect.left) {
            dirtyRect.left = historicalX;
        } else if (historicalX > dirtyRect.right) {
            dirtyRect.right = historicalX;
        }
        if (historicalY < dirtyRect.top) {
            dirtyRect.top = historicalY;
        } else if (historicalY > dirtyRect.bottom) {
            dirtyRect.bottom = historicalY;
        }
    }

    private Pair<Path, Paint> insertAndGetRemoteParticipantPalette(RemoteParticipant remoteParticipant) {
        Path path = new Path();
        Paint paint = getRemoteParticipantPaint(remoteParticipant);
        Pair<Path, Paint> remoteParticipantPalette = new Pair<>(path, paint);

        remoteParticipantPalettes.put(remoteParticipant, remoteParticipantPalette);

        return remoteParticipantPalette;
    }

    private Paint getRemoteParticipantPaint(RemoteParticipant remoteParticipant) {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setColor(getRandomColor());
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);
        return paint;
    }

    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void eraser(boolean isErase) {
        erase=isErase;
        if(erase)
        {
            mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else
            mDrawPaint.setXfermode(null);
    }

    public boolean isErase() {
        return erase;
    }

    public void clear() {
        mDrawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public interface Listener {
        void onTouchEvent(int actionEvent, float x, float y);
    }

    public void setPaintColor(int color)
    {
        init();
        mPaintColor = color;
        mDrawPaint.setColor(mPaintColor);
    }

    public void setViewBackgroundColor(int color)
    {
        mBackgroundColor = color;
        mBackgroundPaint.setColor(mBackgroundColor);
        invalidate();
    }

    public void setPaintStrokeWidth(int strokeWidth) {
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                strokeWidth, getResources().getDisplayMetrics());
        mStrokeWidth = strokeWidth;
        mDrawPaint.setStrokeWidth(mStrokeWidth);
    }
}