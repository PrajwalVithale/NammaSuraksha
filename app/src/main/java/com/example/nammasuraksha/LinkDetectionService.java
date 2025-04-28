package com.example.nammasuraksha;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LinkDetectionService extends AccessibilityService {

    private static final String TAG = "LinkDetectionService";
    private RequestQueue queue;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        queue = Volley.newRequestQueue(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getText() != null) {
            for (CharSequence sequence : event.getText()) {
                String text = sequence.toString();
                if (!TextUtils.isEmpty(text) && text.startsWith("http")) {
                    // 🔥 Instead of fake check, use real Safe Browsing API
                    SafeBrowsingChecker.checkUrlSafety(this, text);
                }
            }
        }
    }



    private void checkLinkSafety(String url) {
        String safeBrowsingApiUrl = "https://safebrowsing.googleapis.com/v4/threatMatches:find?key=YOUR_API_KEY";

        // Normally you'd use POST with a JSON body for Safe Browsing.
        // Here just simulating: assume safe if it contains "safe", unsafe otherwise for demo.

        if (url.contains("safe")) {
            FloatingBubbleService.updateBubble(true);
        } else {
            FloatingBubbleService.updateBubble(false);
        }
    }

    @Override
    public void onInterrupt() {}
}
