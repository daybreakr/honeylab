package com.honeycomb.lib.tasks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.after;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class TasksTest {
    private static final long DEFAULT_VERIFICATION_DELAY = 400;

    private static final long FINISHED = -1;
    private static final long DEFAULT_RETENTION = 200;

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
        verifySuccess(task, mResult, DEFAULT_VERIFICATION_DELAY);
    }

    @Test
    public void testForException() {
        // Model side provides a observable task.
        Task task = Tasks.forException(mException);

        // Client side observe the task.
        verifyFailure(task, mException, DEFAULT_VERIFICATION_DELAY);
    }

    @Test
    public void testCall_success() {
        Task task = Tasks.call(successCallable(mResult, DEFAULT_RETENTION));

        verifySuccess(task, mResult, DEFAULT_VERIFICATION_DELAY);
    }

    @Test
    public void testCall_fail() {
        Task task = Tasks.call(failureCallable(mException, DEFAULT_RETENTION));

        verifyFailure(task, mException, DEFAULT_VERIFICATION_DELAY);
    }

    @Test
    public void testCall_withExecutor() {
        Executor executor = spy(Executors.newCachedThreadPool());

        Task task = Tasks.call(executor, successCallable(mResult, DEFAULT_RETENTION));

        verify(executor).execute(any(Runnable.class));
        verifySuccess(task, mResult, DEFAULT_VERIFICATION_DELAY);
    }

    @Test
    public void testAwait_successNoDelay() throws ExecutionException, InterruptedException {
        Task task = successTask(mResult, FINISHED);

        Object result = Tasks.await(task);

        assertEquals(result, mResult);
    }

    @Test
    public void testAwait_successDelayed() throws ExecutionException, InterruptedException {
        Task task = successTask(mResult, DEFAULT_RETENTION);

        Object result = Tasks.await(task);

        assertEquals(result, mResult);
    }

    @Test
    public void testAwait_failNoDelay() throws InterruptedException {
        Task task = failureTask(mException, FINISHED);

        try {
            Tasks.await(task);
            fail();
        } catch (ExecutionException e) {
            assertEquals(e.getCause(), mException);
        }
    }

    @Test
    public void testAwait_failDelayed() throws InterruptedException {
        Task task = failureTask(mException, DEFAULT_RETENTION);

        try {
            Tasks.await(task);
            fail();
        } catch (ExecutionException e) {
            assertEquals(e.getCause(), mException);
        }
    }

    @Test
    public void testAwait_successInTime()
            throws InterruptedException, ExecutionException, TimeoutException {
        Task task = successTask(mResult, DEFAULT_RETENTION);

        Object result = Tasks.await(task, DEFAULT_RETENTION + 500, TimeUnit.MILLISECONDS);

        assertEquals(result, mResult);
    }

    @Test
    public void testAwait_timeout() throws InterruptedException, ExecutionException {
        Task task = successTask(mResult, 500);

        try {
            Tasks.await(task, 200, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            assertThat(e.getMessage(), equalTo("Time out waiting for Task"));
        }
    }

    @Test
    public void testWhenAll_allSuccess() {
        Task task1 = successTask(new Object(), FINISHED);
        Task task2 = successTask(new Object(), 200);
        Task task3 = successTask(new Object(), 100);

        Task batch = Tasks.whenAll(task1, task2, task3);

        verifySuccess(batch, null, 400);
    }

    @Test
    public void testWhenAll_oneOfThreeFailed() {
        final int failureCount = 1;
        final long verificationDelay = 400;

        final List<Task<?>> tasks = new LinkedList<>();
        tasks.add(successTask(new Object(), FINISHED));
        tasks.add(successTask(new Object(), 200));
        tasks.add(failureTask(mException, 100));

        Task batch = Tasks.whenAll(tasks);

        verifyFailure(batch, ExecutionException.class, verificationDelay);

        OnFailureListener listener = mock(OnFailureListener.class);
        batch.addOnFailureListener(listener);
        verify(listener, timeout(verificationDelay)).onFailure(
                argThat(new ArgumentMatcher<Exception>() {
                    @Override
                    public boolean matches(Exception e) {
                        String expectedMsg = failureCount + " out of " + tasks.size()
                                + " underlying tasks failed";
                        return (e instanceof ExecutionException)
                                && expectedMsg.equals(e.getMessage())
                                && mException.equals(e.getCause());
                    }
                }));
    }

    @Test
    public void testWhenAll_twoOfThreeFailed() {
        final int failureCount = 2;
        final long verificationDelay = 400;

        final List<Task<?>> tasks = new LinkedList<>();
        tasks.add(failureTask(mException, FINISHED));
        tasks.add(successTask(new Object(), 200));
        tasks.add(failureTask(mException, 100));

        Task batch = Tasks.whenAll(tasks);

        verifyFailure(batch, ExecutionException.class, verificationDelay);

        OnFailureListener listener = mock(OnFailureListener.class);
        batch.addOnFailureListener(listener);
        verify(listener, timeout(verificationDelay)).onFailure(
                argThat(new ArgumentMatcher<Exception>() {
                    @Override
                    public boolean matches(Exception e) {
                        String expectedMsg = failureCount + " out of " + tasks.size()
                                + " underlying tasks failed";
                        return (e instanceof ExecutionException)
                                && expectedMsg.equals(e.getMessage())
                                && mException.equals(e.getCause());
                    }
                }));
    }

    //==============================================================================================
    // Verifications
    //==============================================================================================

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

    private void verifyFailure(Task task, Class<? extends Exception> expectException,
                               long timeout) {
        bindListeners(task);

        if (timeout > 0) {
            verify(mOnCompleteListener, timeout(timeout)).onComplete(task);
            verify(mOnSuccessListener, after(timeout).never()).onSuccess(any());
            verify(mOnFailureListener, timeout(timeout)).onFailure(any(expectException));
        } else {
            verify(mOnCompleteListener).onComplete(task);
            verify(mOnSuccessListener, never()).onSuccess(any());
            verify(mOnFailureListener).onFailure(any(expectException));
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

    private static Task successTask(Object result, long retention) {
        if (retention < 0) {
            return Tasks.forResult(result);
        } else {
            return Tasks.call(successCallable(result, retention));
        }
    }

    private static Task failureTask(Exception exception, long retention) {
        if (retention < 0) {
            return Tasks.forException(exception);
        } else {
            return Tasks.call(failureCallable(exception, retention));
        }
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