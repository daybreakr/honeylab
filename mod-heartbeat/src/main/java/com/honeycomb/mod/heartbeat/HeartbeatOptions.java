package com.honeycomb.mod.heartbeat;

public class HeartbeatOptions {

    public long heartbeatInterval = 2 * 1000; // 2 seconds

    public RecorderOptions recorderOptions = new RecorderOptions();

    public static class RecorderOptions {

        public boolean enableLog = true;

        public boolean enableBroadcast = true;

        public boolean saveToFile = true;
    }
}
