package com.honeycomb.lib.tasks;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskExecutors {
    public static final Executor DEFAULT = new SerialExecutor();

    private TaskExecutors() {
    }

    static final class SerialExecutor implements Executor {
        private final Executor mExecutor = Executors.newSingleThreadExecutor();

        @Override
        public void execute(@NonNull Runnable command) {
            mExecutor.execute(command);
        }
    }
}
