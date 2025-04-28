package com.example.nammasuraksha;

import android.content.Context;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SafeBrowsingChecker {

    private static final String API_KEY = "AIzaSyDbCR7sfVsy5K2HpX10d3BRkObEmen8DXQ";
    private static final String TAG = "SafeBrowsingChecker";
    private static final String SAFE_BROWSING_URL = "https://safebrowsing.googleapis.com/v4/threatMatches:find?key=" + API_KEY;

    public static void checkUrlSafety(Context context, String urlToCheck) {
        RequestQueue queue = Volley.newRequestQueue(context);

        try {
            JSONObject client = new JSONObject();
            client.put("clientId", "LinkSecureApp");
            client.put("clientVersion", "1.0");

            JSONArray threatTypes = new JSONArray();
            threatTypes.put("MALWARE");
            threatTypes.put("SOCIAL_ENGINEERING");
            threatTypes.put("UNWANTED_SOFTWARE");

            JSONArray platformTypes = new JSONArray();
            platformTypes.put("ANY_PLATFORM");

            JSONArray threatEntryTypes = new JSONArray();
            threatEntryTypes.put("URL");

            JSONObject threatEntry = new JSONObject();
            threatEntry.put("url", urlToCheck);

            JSONArray threatEntries = new JSONArray();
            threatEntries.put(threatEntry);

            JSONObject threatInfo = new JSONObject();
            threatInfo.put("threatTypes", threatTypes);
            threatInfo.put("platformTypes", platformTypes);
            threatInfo.put("threatEntryTypes", threatEntryTypes);
            threatInfo.put("threatEntries", threatEntries);

            JSONObject postData = new JSONObject();
            postData.put("client", client);
            postData.put("threatInfo", threatInfo);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    SAFE_BROWSING_URL,
                    postData,
                    response -> {
                        Log.d(TAG, "API Response: " + response.toString());

                        if (response.length() != 0) {
                            Log.d(TAG, "Dangerous URL detected!");
                            FloatingBubbleService.updateBubble(false); // 🔴 Turn Red
                        } else {
                            Log.d(TAG, "Safe URL detected.");
                            FloatingBubbleService.updateBubble(true); // 🟢 Turn Green
                        }
                    },
                    error -> {
                        Log.e(TAG, "API Error: " + error.toString());
                        // (Optional) You can notify user if API fails.
                        // Assuming safe if error
                        FloatingBubbleService.updateBubble(true);
                    }
            );

            // Add Retry Policy (important for network stability)
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000, // 5 seconds timeout
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            Log.e(TAG, "JSON Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
