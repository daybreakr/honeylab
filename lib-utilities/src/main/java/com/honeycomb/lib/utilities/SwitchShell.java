package com.honeycomb.lib.utilities;

public class SwitchShell {
    private final Switch mSwitch;

    public SwitchShell() {
        mSwitch = new Switch()
                .onStart(new Action() {
                    @Override
                    public void onAction() {
                        onStart();
                    }
                }).onStop(new Action() {
                    @Override
                    public void onAction() {
                        onStop();
                    }
                }).onDestroy(new Action() {
                    @Override
                    public void onAction() {
                        onDestroy();
                    }
                });
    }

    public boolean start() {
        return mSwitch.start();
    }

    public boolean stop() {
        return mSwitch.stop();
    }

    public boolean destroy() {
        return mSwitch.destroy();
    }

    protected void onStart() {
    }

    protected void onStop() {
    }

    protected void onDestroy() {
    }
}
