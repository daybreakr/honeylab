package com.honeycomb.util.state;

import java.util.concurrent.atomic.AtomicBoolean;

public class StateMachine {
    private IState mCurrentState;

    private final AtomicBoolean mIsChangingState = new AtomicBoolean(false);

    public IState getCurrentState() {
        return mCurrentState;
    }

    public boolean setState(IState state) {
        if (mIsChangingState.compareAndSet(false, true)) {
            try {
                if (mCurrentState != null) {
                    mCurrentState.onExit(state);
                }

                mCurrentState = state;
                mCurrentState.onEnter();
                return true;
            } finally {
                mIsChangingState.set(false);
            }
        }
        return false;
    }
}
