package com.honeycomb.util.state;

public interface IState {

    void onEnter();

    void onExit(IState next);
}
