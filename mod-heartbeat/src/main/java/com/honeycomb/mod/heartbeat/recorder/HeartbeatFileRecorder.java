package com.honeycomb.mod.heartbeat.recorder;

import android.content.Context;
import android.os.Environment;

import com.honeycomb.sdk.common.AppCommon;
import com.honeycomb.mod.heartbeat.HeartbeatEvent;
import com.honeycomb.mod.heartbeat.HeartbeatListener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class HeartbeatFileRecorder implements HeartbeatListener {
    private static final String[] REQUIRED_PERMISSIONS = {
            Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE};

    private static final String DIR_NAME = "HoneyLab";
    private static final String FILE_NAME_SUFFIX = ".ecg";

    private static final String START_TAG = "[START]";

    private FileWriter mWriter;

    private final AtomicBoolean mIsFirstHeartbeatArrived = new AtomicBoolean(false);
    private long mFirstHeartbeatTime;
    private HeartbeatBufferedRecorder mBuffer;

    @Override
    public void onHeartbeat(HeartbeatEvent heartbeat) {
        if (mIsFirstHeartbeatArrived.compareAndSet(false, true)) {
            mFirstHeartbeatTime = System.currentTimeMillis();

            tryStartRecording();
        }

        if (mWriter == null) {
            if (mBuffer == null) {
                mBuffer = new HeartbeatBufferedRecorder(this);
            }
            mBuffer.onHeartbeat(heartbeat);
            return;
        }

        record(heartbeat);
    }

    private void tryStartRecording() {
        if (hasRequiredPermissions()) {
            startRecording(System.currentTimeMillis());
        } else {
            requestPermissions();
        }
    }

    private boolean hasRequiredPermissions() {
        Context context = AppCommon.getInstance().getApplicationContext();
        return AndPermission.hasPermissions(context, REQUIRED_PERMISSIONS);
    }

    private void requestPermissions() {
        Context context = AppCommon.getInstance().getApplicationContext();
        AndPermission.with(context).runtime().permission(REQUIRED_PERMISSIONS)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        onPermissionGranted();
                    }
                }).start();
    }

    private void onPermissionGranted() {
        long startTime = mIsFirstHeartbeatArrived.get()
                ? mFirstHeartbeatTime
                : System.currentTimeMillis();
        startRecording(startTime);

        if (mBuffer != null) {
            mBuffer.flush();
            mBuffer = null;
        }
    }

    private void startRecording(long startTime) {
        try {
            File file = getRecordFile();
            FileWriter writer = new FileWriter(file, true);

            // Indicate that we start a record session.
            String startTag = getStartTag(startTime);
            writer.append(startTag).append("\n");
            writer.flush();

            mWriter = writer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void record(HeartbeatEvent heartbeat) {
        try {
            String record = formatHeartbeat(heartbeat);
            mWriter.append(record).append("\n");
            mWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private String getStartTag(long startTime) {
        return START_TAG + "," + startTime;
    }

    private String formatHeartbeat(HeartbeatEvent heartbeat) {
        return heartbeat.timestamp + "," + heartbeat.dt;
    }
}
