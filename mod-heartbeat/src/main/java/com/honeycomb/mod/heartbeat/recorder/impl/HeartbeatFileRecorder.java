package com.honeycomb.mod.heartbeat.recorder.impl;

import android.os.Environment;

import com.honeycomb.lib.common.AppCommon;
import com.honeycomb.mod.heartbeat.HeartbeatEvent;
import com.honeycomb.mod.heartbeat.HeartbeatListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HeartbeatFileRecorder implements HeartbeatListener {
    private static final String DIR_NAME = "HoneyLab";
    private static final String FILE_NAME_SUFFIX = ".ecg";

    private FileWriter mWriter;

    public HeartbeatFileRecorder() {
        try {
            File file = getRecordFile();
            mWriter = new FileWriter(file, true);

            // Indicate that we start a record session.
            mWriter.append("\t\n");

        } catch (IOException e) {
            mWriter = null;
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onHeartbeat(HeartbeatEvent heartbeat) {
        if (mWriter == null) {
            return;
        }

        try {
            String content = formatHeartbeat(heartbeat);
            mWriter.append(content).append("\n");
            mWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatHeartbeat(HeartbeatEvent heartbeat) {
        return heartbeat.timestamp + "," + heartbeat.dt;
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
