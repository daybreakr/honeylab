package com.honeycomb.mod.keepalive.wakeup.recorder.impl;

import android.os.Environment;

import com.honeycomb.lib.common.AppCommon;
import com.honeycomb.mod.keepalive.wakeup.WakeupEvent;
import com.honeycomb.mod.keepalive.wakeup.WakeupListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WakeupFileRecorder implements WakeupListener {
    private static final String DIR_NAME = "HoneyLab";
    private static final String FILE_NAME_SUFFIX = ".wkp";

    private static final String START_TAG = "[START]";

    private FileWriter mWriter;

    public WakeupFileRecorder() {
        try {
            File file = getRecordFile();
            mWriter = new FileWriter(file, true);

            // Indicate that we start a record session.
            mWriter.append(getStartTag()).append("\n");

            mWriter.flush();
        } catch (IOException e) {
            mWriter = null;
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onWakeup(WakeupEvent wakeupEvent) {
        if (mWriter == null) {
            return;
        }

        try {
            mWriter.append(formatWakeup(wakeupEvent)).append("\n");
            mWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getStartTag() {
        return START_TAG + "," + System.currentTimeMillis();
    }

    private String formatWakeup(WakeupEvent wakeupEvent) {
        return wakeupEvent.receivedTime + "," + wakeupEvent.tag;
    }

    private File getRecordFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory(), DIR_NAME);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Failed to create record directory: " + dir);
            }
        }

        String packageName = AppCommon.getInstance().getApplicationContext().getPackageName();
        return new File(dir, packageName + FILE_NAME_SUFFIX);
    }
}
