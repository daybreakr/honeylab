package com.honeycomb.lib.utilities.state;

public interface IState {

    void onEnter();

    void onExit(IState next);
}
