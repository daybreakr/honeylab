package com.honeycomb.lib.utilities.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LegalizedStateMachine extends StateMachine {

    private final Map<Class<? extends IState>, Set<Class<? extends IState>>> mLegalTransitions =
            new HashMap<>();
    private final Set<Class<? extends IState>> mWildcardStates = new HashSet<>();

    public void setLegalTransition(Class<? extends IState> from, Class<? extends IState> to) {
        Set<Class<? extends IState>> legals = mLegalTransitions.get(from);
        if (legals == null) {
            legals = new HashSet<>();
            mLegalTransitions.put(from, legals);
        }
        legals.add(to);
    }

    public void setLegalTransition(Class<? extends IState> wildcardState) {
        mWildcardStates.add(wildcardState);
    }

    @Override
    public boolean setState(IState state) {
        return isLegalTransition(state) && super.setState(state);
    }

    private boolean isLegalTransition(IState toState) {
        // Cannot transition to a null state.
        if (toState == null) {
            return false;
        }
        // Any transition away from null is legal.  It's the first transition.
        if (getCurrentState() == null) {
            return true;
        }

        Class<? extends IState> from = getCurrentState().getClass();
        Class<? extends IState> to = toState.getClass();
        if (mWildcardStates.contains(from) || mWildcardStates.contains(to)) {
            return true;
        }

        Set<Class<? extends IState>> legals = mLegalTransitions.get(from);
        return legals != null && legals.contains(to);
    }
}
