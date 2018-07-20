package com.honeycomb.lab.lifecycle;

import android.util.Log;

public class LifecycleLogger {
    private final int mLevel;
    private final String mTag;
    private final String mPrefix;
    private final String mSuffix;

    private LifecycleLogger(Builder builder) {
        mLevel = builder.level;
        mTag = builder.tag;
        mPrefix = builder.prefix;
        mSuffix = builder.suffix;
    }

    public void method(Object... args) {
        log(getCallingMethodName(), args);
    }

    public void methodStart(Object... args) {
        log(getCallingMethodName() + " {", args);
    }

    public void methodEnd(Object... args) {
        log("} " + getCallingMethodName(), args);
    }

    public void log(String msg, Object... args) {
        StringBuilder content = new StringBuilder();

        // prefix
        if (mPrefix != null) {
            content.append(mPrefix);
        }

        // message
        content.append(msg);

        // args
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                String value = arg == null ? "null" : arg.toString();
                content.append(", arg").append(i + 1).append("=").append(value);
            }
        }

        // suffix
        if (mSuffix != null) {
            content.append(mSuffix);
        }

        Log.println(mLevel, mTag, content.toString());
    }

    private String getCallingMethodName() {
        return new Exception().getStackTrace()[2].getMethodName();
    }

    public static Builder buildUpon() {
        return new Builder();
    }

    public static class Builder {
        int level = Log.DEBUG;
        String tag;
        String prefix;
        String suffix;

        public Builder level(int level) {
            this.level = level;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public LifecycleLogger build() {
            this.level = Math.max(Log.VERBOSE, Math.min(Log.ASSERT, this.level));
            if (this.tag == null) {
                this.tag = Constants.TAG;
            }
            return new LifecycleLogger(this);
        }
    }
}
