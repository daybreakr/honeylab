package com.honeycomb.mod.process.monitor.impl;

import android.util.Log;

import com.honeycomb.mod.process.monitor.ProcessMonitorThread;
import com.honeycomb.util.IoUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LogcatMonitor extends ProcessMonitorThread {
    private static final String TAG = LogcatMonitor.class.getSimpleName();

    private static final String[] CMD = {
            "logcat", "-s", "ActivityManager:I",
    };

    @Override
    public void run() {
        BufferedReader reader = null;
        Process process = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(CMD);
            process = processBuilder.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while (!isStopped() && ((line = reader.readLine()) != null)) {

                // TODO: Filter START tag
                printLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IoUtils.closeQuietly(reader);
            if (process != null) {
                process.destroy();
            }
        }
    }

    private void printLine(String line) {
        Log.i(TAG, line);
    }
}
