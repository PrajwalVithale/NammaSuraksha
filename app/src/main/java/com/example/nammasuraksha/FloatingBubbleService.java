package com.example.nammasuraksha;


import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.view.Gravity;


public class FloatingBubbleService extends Service {

    private static WindowManager windowManager;
    private static View bubbleView;
    private static ImageView bubbleImage;

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        bubbleView = LayoutInflater.from(this).inflate(R.layout.bubble_layout, null);

        bubbleImage = bubbleView.findViewById(R.id.bubbleImage);
        bubbleImage.setAlpha(0.5f);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 50; // Optional: horizontal margin
        params.y = 100; // Vertical margin (top distance)

        windowManager.addView(bubbleView, params);
    }

    public static void updateBubble(boolean isSafe) {
        if (bubbleImage != null) {
            if (isSafe) {
                bubbleImage.setImageResource(R.drawable.ic_safe);
            } else {
                bubbleImage.setImageResource(R.drawable.ic_unsafe);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bubbleView != null) windowManager.removeView(bubbleView);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
