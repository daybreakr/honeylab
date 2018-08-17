package com.honeycomb.lib.tasks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Executor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class OnFailureDelivererTest {
    @Mock
    private OnFailureListener mOnFailureListener;
    @Mock
    private Task mTask;
    @Mock
    private Exception mException;

    private Executor mExecutor = TaskExecutors.DEFAULT;

    @Test
    public void testOnComplete_whenTaskFailure() {
        when(mTask.isSuccessful()).thenReturn(false);
        when(mTask.getException()).thenReturn(mException);

        OnFailureDeliverer deliverer = new OnFailureDeliverer(mExecutor, mOnFailureListener);
        deliverer.onComplete(mTask);

        verify(mOnFailureListener).onFailure(mException);
    }

    @Test
    public void testOnComplete_whenTaskSuccess() {
        when(mTask.isSuccessful()).thenReturn(true);

        OnFailureDeliverer deliverer = new OnFailureDeliverer(mExecutor, mOnFailureListener);
        deliverer.onComplete(mTask);

        verifyNoMoreInteractions(mOnFailureListener);
    }

    @Test
    public void testOnComplete_nullListener() {
        when(mTask.isSuccessful()).thenReturn(false);

        //noinspection ConstantConditions
        OnFailureDeliverer deliverer = new OnFailureDeliverer(mExecutor, null);
        deliverer.onComplete(mTask);

        // Expect no thrown.
    }

    @Test
    public void testCancel() {
        OnFailureDeliverer deliverer = new OnFailureDeliverer(mExecutor, mOnFailureListener);
        deliverer.cancel();
        deliverer.onComplete(mTask);

        verifyNoMoreInteractions(mOnFailureListener);
    }
}