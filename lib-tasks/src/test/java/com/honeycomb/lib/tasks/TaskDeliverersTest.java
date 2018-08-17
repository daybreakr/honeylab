package com.honeycomb.lib.tasks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class TaskDeliverersTest {
    @Mock
    private OnCompleteDeliverer mOnCompleteDeliverer;
    @Mock
    private OnSuccessDeliverer mOnSuccessDeliverer;
    @Mock
    private OnFailureDeliverer mOnFailureDeliverer;

    @Test
    public void testAddDeliverer() {
        TaskDeliverers deliverers = spy(TaskDeliverers.class);
        deliverers.addDeliverer(mOnCompleteDeliverer);
        deliverers.addDeliverer(mOnSuccessDeliverer);

        InOrder inOrder = inOrder(deliverers);

        inOrder.verify(deliverers).addDeliverer(mOnCompleteDeliverer);
        inOrder.verify(deliverers).addDeliverer(mOnSuccessDeliverer);

        verify(deliverers, never()).addDeliverer(mOnFailureDeliverer);
    }

    @Test
    public void testDeliverComplete_allDelivered() {
        TaskDeliverers deliverers = spy(TaskDeliverers.class);
        deliverers.addDeliverer(mOnCompleteDeliverer);
        deliverers.addDeliverer(mOnSuccessDeliverer);
        deliverers.addDeliverer(mOnFailureDeliverer);

        Task task = mock(Task.class);
        deliverers.deliverComplete(task);

        verify(mOnCompleteDeliverer).onComplete(task);
        verify(mOnSuccessDeliverer).onComplete(task);
        verify(mOnFailureDeliverer).onComplete(task);
    }

    @Test
    public void testDeliverComplete_deliverTwice() {
        TaskDeliverers deliverers = spy(TaskDeliverers.class);

        // First delivery.
        Task taskOne = mock(Task.class);
        deliverers.addDeliverer(mOnCompleteDeliverer);
        deliverers.deliverComplete(taskOne);
        verify(mOnCompleteDeliverer).onComplete(taskOne);

        // Second delivery.
        Task taskTwo = mock(Task.class);
        deliverers.addDeliverer(mOnSuccessDeliverer);
        deliverers.deliverComplete(taskTwo);
        verify(mOnSuccessDeliverer).onComplete(taskTwo);

        // Expect removed from deliverers if delivered.
        verifyNoMoreInteractions(mOnCompleteDeliverer);
    }

    @Test
    public void testDeliverComplete_duplicateDelivery() {
        TaskDeliverers deliverers = spy(TaskDeliverers.class);
        deliverers.addDeliverer(mOnCompleteDeliverer);

        Task task = mock(Task.class);
        deliverers.deliverComplete(task);
        deliverers.deliverComplete(task);

        verify(mOnCompleteDeliverer, times(1)).onComplete(task);
    }
}