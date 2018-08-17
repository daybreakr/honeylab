package com.honeycomb.lib.tasks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class TasksTest {
    private static final long DEFAULT_RETENTION = 200;
    private static final long DEFAULT_VERIFICATION_DELAY = 300;

    private OnCompleteListener mOnCompleteListener;
    private OnSuccessListener mOnSuccessListener;
    private OnFailureListener mOnFailureListener;
    private Object mResult;
    private Exception mException;

    @Before
    public void setUp() {
        mOnCompleteListener = mock(OnCompleteListener.class);
        mOnSuccessListener = mock(OnSuccessListener.class);
        mOnFailureListener = mock(OnFailureListener.class);

        mResult = new Object();
        mException = new Exception();
    }

    @Test
    public void testForResult() {
        // Model side provides a observable task.
        Task task = Tasks.forResult(mResult);

        // Client side observe the task.
        verifySuccess(task, mResult);
    }

    @Test
    public void testForException() {
        // Model side provides a observable task.
        Task task = Tasks.forException(mException);

        // Client side observe the task.
        verifyFailure(task, mException);
    }

    @Test
    public void testCall_success() {
        Task task = Tasks.call(successCallable(mResult));

        verifySuccess(task, mResult);
    }

    @Test
    public void testCall_fail() {
        Task task = Tasks.call(failureCallable(mException));

        verifyFailure(task, mException);
    }

    @Test
    public void testCall_withExecutor() {
        Executor executor = spy(Executors.newCachedThreadPool());

        Task task = Tasks.call(executor, successCallable(mResult));

        verify(executor).execute(any(Runnable.class));
        verifySuccess(task, mResult);
    }

    @Test
    public void testAwait_success() throws ExecutionException, InterruptedException {
        Task task = successTask(mResult, -1);

        Object result = Tasks.await(task);

        assertEquals(result, mResult);
    }

    @Test
    public void testAwait_successDelayed() throws ExecutionException, InterruptedException {
        Task task = successTask(mResult, 1000);

        Object result = Tasks.await(task);

        assertEquals(result, mResult);
    }

    @Test
    public void testAwait_fail() throws InterruptedException {
        Task task = failureTask(mException, -1);

        try {
            Tasks.await(task);
            fail();
        } catch (ExecutionException e) {
            assertEquals(e.getCause(), mException);
        }
    }

    @Test
    public void testAwait_failDelayed() throws InterruptedException {
        Task task = failureTask(mException, 1000);

        try {
            Tasks.await(task);
            fail();
        } catch (ExecutionException e) {
            assertEquals(e.getCause(), mException);
        }
    }

    //==============================================================================================
    // Verifications
    //==============================================================================================

    private void verifySuccess(Task task, Object expectResult) {
        verifySuccess(task, expectResult, DEFAULT_VERIFICATION_DELAY);
    }

    private void verifySuccess(Task task, Object expectResult, long timeout) {
        bindListeners(task);

        if (timeout > 0) {
            verify(mOnCompleteListener, timeout(timeout)).onComplete(task);
            verify(mOnSuccessListener, timeout(timeout)).onSuccess(expectResult);
            verify(mOnFailureListener, after(timeout).never()).onFailure(any(Exception.class));
        } else {
            verify(mOnCompleteListener).onComplete(task);
            verify(mOnSuccessListener).onSuccess(expectResult);
            verify(mOnFailureListener, never()).onFailure(any(Exception.class));
        }
    }

    private void verifyFailure(Task task, Exception expectException) {
        verifyFailure(task, expectException, DEFAULT_VERIFICATION_DELAY);
    }

    private void verifyFailure(Task task, Exception expectException, long timeout) {
        bindListeners(task);

        if (timeout > 0) {
            verify(mOnCompleteListener, timeout(timeout)).onComplete(task);
            verify(mOnSuccessListener, after(timeout).never()).onSuccess(any());
            verify(mOnFailureListener, timeout(timeout)).onFailure(expectException);
        } else {
            verify(mOnCompleteListener).onComplete(task);
            verify(mOnSuccessListener, never()).onSuccess(any());
            verify(mOnFailureListener).onFailure(expectException);
        }
    }

    private void bindListeners(Task task) {
        task.addOnCompleteListener(mOnCompleteListener);
        task.addOnSuccessListener(mOnSuccessListener);
        task.addOnFailureListener(mOnFailureListener);
    }

    //==============================================================================================
    // Factory methods
    //==============================================================================================

    private static Task successTask(Object result) {
        return successTask(result, DEFAULT_RETENTION);
    }

    private static Task successTask(Object result, long retention) {
        if (retention < 0) {
            return Tasks.forResult(result);
        } else {
            return Tasks.call(successCallable(result, retention));
        }
    }

    private static Task failureTask(Exception exception) {
        return failureTask(exception, DEFAULT_RETENTION);
    }

    private static Task failureTask(Exception exception, long retention) {
        if (retention < 0) {
            return Tasks.forException(exception);
        } else {
            return Tasks.call(failureCallable(exception, retention));
        }
    }

    private static Callable successCallable(Object result) {
        return successCallable(result, DEFAULT_RETENTION);
    }

    private static Callable successCallable(final Object result, final long retention) {
        return new Callable() {
            @Override
            public Object call() throws Exception {
                if (retention > 0) {
                    Thread.sleep(retention);
                }
                return result;
            }
        };
    }

    private static Callable failureCallable(Exception exception) {
        return failureCallable(exception, DEFAULT_RETENTION);
    }

    private static Callable failureCallable(final Exception exception, final long retention) {
        return new Callable() {
            @Override
            public Object call() throws Exception {
                if (retention > 0) {
                    Thread.sleep(retention);
                }
                throw exception;
            }
        };
    }
}