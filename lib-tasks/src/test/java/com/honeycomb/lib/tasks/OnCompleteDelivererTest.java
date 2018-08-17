package com.honeycomb.lib.tasks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.Executor;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class OnCompleteDelivererTest {
    @Mock
    private OnCompleteListener mOnCompleteListener;
    @Mock
    private Task mTask;

    private Executor mExecutor = TaskExecutors.DEFAULT;

    @Test
    public void testOnComplete_delivered() {
        OnCompleteDeliverer deliverer = new OnCompleteDeliverer(mExecutor, mOnCompleteListener);
        deliverer.onComplete(mTask);

        verify(mOnCompleteListener).onComplete(mTask);
    }

    @Test
    public void testOnComplete_nullListener() {
        //noinspection ConstantConditions
        OnCompleteDeliverer deliverer = new OnCompleteDeliverer(mExecutor, null);
        deliverer.onComplete(mTask);

        // Expect no thrown.
    }

    @Test
    public void testCancel() {
        OnCompleteDeliverer deliverer = new OnCompleteDeliverer(mExecutor, mOnCompleteListener);
        deliverer.cancel();
        deliverer.onComplete(mTask);

        verifyNoMoreInteractions(mOnCompleteListener);
    }
}