package com.honeycomb.lib.tasks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Executor;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class OnSuccessDelivererTest {
    private static final long DEFAULT_VERIFICATION_DELAY = 400;

    @Mock
    private OnSuccessListener mOnSuccessListener;
    @Mock
    private Task mTask;
    @Mock
    private Object mResult;

    private Executor mExecutor = TaskExecutors.DEFAULT;

    @Test
    public void testOnComplete_whenTaskSuccess() {
        when(mTask.isSuccessful()).thenReturn(true);
        when(mTask.getResult()).thenReturn(mResult);

        OnSuccessDeliverer deliverer = new OnSuccessDeliverer(mExecutor, mOnSuccessListener);
        deliverer.onComplete(mTask);

        verify(mOnSuccessListener, timeout(DEFAULT_VERIFICATION_DELAY)).onSuccess(mResult);
    }

    @Test
    public void testOnComplete_whenTaskFailure() {
        when(mTask.isSuccessful()).thenReturn(false);

        OnSuccessDeliverer deliverer = new OnSuccessDeliverer(mExecutor, mOnSuccessListener);
        deliverer.onComplete(mTask);

        verifyZeroInteractions(mOnSuccessListener);
    }

    @Test
    public void testOnComplete_nullListener() {
        when(mTask.isSuccessful()).thenReturn(true);

        //noinspection ConstantConditions
        OnSuccessDeliverer deliverer = new OnSuccessDeliverer(mExecutor, null);
        deliverer.onComplete(mTask);

        // Expect no thrown.
    }

    @Test
    public void testCancel() {
        OnSuccessDeliverer deliverer = new OnSuccessDeliverer(mExecutor, mOnSuccessListener);
        deliverer.cancel();
        deliverer.onComplete(mTask);

        verifyZeroInteractions(mOnSuccessListener);
    }
}