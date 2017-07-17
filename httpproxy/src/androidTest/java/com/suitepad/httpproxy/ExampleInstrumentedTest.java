package com.suitepad.httpproxy;

import android.content.Context;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static final String TAG = "ExampleInstrumentedTest";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.suitepad.httpproxy", appContext.getPackageName());

        Uri uri = new Uri.Builder()
                .scheme("content")
                .authority("com.suitepad.datasource")
                .appendPath("get")
                .appendPath("test")
                .build();
        Log.d(TAG, "useAppContext: " + uri.toString());
    }

}
