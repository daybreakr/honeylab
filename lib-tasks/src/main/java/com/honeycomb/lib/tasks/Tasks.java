package com.honeycomb.lib.tasks;

import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class Tasks {

    private Tasks() {
    }

    public static <TResult> Task<TResult> forResult(TResult result) {
        TaskImpl<TResult> task = new TaskImpl<>();
        task.setResult(result);
        return task;
    }

    public static <TResult> Task<TResult> forException(@NonNull Exception exception) {
        TaskImpl<TResult> task = new TaskImpl<>();
        task.setException(exception);
        return task;
    }

    public static <TResult> Task<TResult> call(@NonNull Callable<TResult> callable) {
        return call(TaskExecutors.DEFAULT, callable);
    }

    public static <TResult> Task<TResult> call(@NonNull Executor executor,
                                               @NonNull final Callable<TResult> callable) {
        requireNonNull(executor, "Executor must not be null");
        requireNonNull(executor, "Callable must not be null");

        final TaskImpl<TResult> task = new TaskImpl<>();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    task.setResult(callable.call());
                } catch (Exception e) {
                    task.setException(e);
                }
            }
        });
        return task;
    }

    public static <TResult> TResult await(@NonNull Task<TResult> task) throws ExecutionException,
            InterruptedException {
        requireNonNull(task, "Task must not be null");

        if (!task.isComplete()) {
            new TaskExecution(task).await();
        }
        return getResult(task);
    }

    public static <TResult> TResult await(@NonNull Task<TResult> task, long timeout,
                                         @NonNull TimeUnit timeUnit)
            throws ExecutionException, InterruptedException, TimeoutException {
        requireNonNull(task);
        requireNonNull(timeUnit);

        if (!task.isComplete()) {
            if (!new TaskExecution(task).await(timeout, timeUnit)) {
                throw new TimeoutException("Time out waiting for Task");
            }
        }
        return getResult(task);
    }

    public static Task<Void> whenAll(Task<?>... tasks) {
        // No task specified, success immediately.
        if (tasks == null || tasks.length == 0) {
            return forResult(null);
        }

        return whenAll(Arrays.asList(tasks));
    }

    public static Task<Void> whenAll(Collection<? extends Task<?>> tasks) {
        // No task specified, success immediately.
        if (tasks.isEmpty()) {
            return forResult(null);
        }

        // Check tasks not null.
        for (Task<?> task : tasks) {
            if (task == null) {
                throw new NullPointerException("null task are not accepted");
            }
        }

        TaskImpl<Void> batchTask = new TaskImpl<>();
        new BatchTaskExecution(tasks, batchTask);
        return batchTask;
    }

    private static <TResult> TResult getResult(Task<TResult> task) throws ExecutionException {
        if (task.isSuccessful()) {
            return task.getResult();
        } else {
            throw new ExecutionException(task.getException());
        }
    }

    private static void bindTaskListeners(Task<?> task, TaskListener listener) {
        task.addOnSuccessListener(listener);
        task.addOnFailureListener(listener);
    }

    @SuppressWarnings("UnusedReturnValue")
    private static <T> T requireNonNull(T object, Object... message) {
        if (object == null) {
            Object cookie = (message == null || message.length < 1) ? "null reference" : message[0];
            throw new NullPointerException(String.valueOf(cookie));
        } else {
            return object;
        }
    }

    interface TaskListener extends OnFailureListener, OnSuccessListener<Object> {
    }

    static final class TaskExecution implements TaskListener {
        private final CountDownLatch mLatch;

        private TaskExecution() {
            mLatch = new CountDownLatch(1);
        }

        private TaskExecution(Task<?> task) {
            this();
            bindTaskListeners(task, this);
        }

        final void await() throws InterruptedException {
            mLatch.await();
        }

        final boolean await(long timeout, TimeUnit timeUnit) throws InterruptedException {
            return mLatch.await(timeout, timeUnit);
        }

        @Override
        public final void onSuccess(Object result) {
            mLatch.countDown();
        }

        @Override
        public final void onFailure(Exception exception) {
            mLatch.countDown();
        }
    }

    static final class BatchTaskExecution implements TaskListener {
        private final int mTaskCount;
        private final TaskImpl<Void> mBatchTask;

        private final Object mLock = new Object();

        private int mSuccessCount;
        private int mFailureCount;
        private Exception mException;

        BatchTaskExecution(Collection<? extends Task<?>> tasks, TaskImpl<Void> batchTask) {
            this(tasks.size(), batchTask);
            for (Task<?> task : tasks) {
                bindTaskListeners(task, this);
            }
        }

        BatchTaskExecution(int taskCount, TaskImpl<Void> batchTask) {
            mTaskCount = taskCount;
            mBatchTask = batchTask;
        }

        @Override
        public final void onSuccess(Object result) {
            synchronized (mLock) {
                mSuccessCount++;

                tryDeliverBatchComplete();
            }
        }

        @Override
        public final void onFailure(Exception exception) {
            synchronized (mLock) {
                mFailureCount++;
                mException = exception;

                tryDeliverBatchComplete();
            }
        }

        private void tryDeliverBatchComplete() {
            if (mSuccessCount + mFailureCount == mTaskCount) {
                if (mException == null) {
                    mBatchTask.setResult(null);
                } else {
                    final String msg = mFailureCount + " out of " + mTaskCount
                            + " underlying tasks failed";
                    mBatchTask.setException(new ExecutionException(msg, mException));
                }
            }
        }
    }
}
