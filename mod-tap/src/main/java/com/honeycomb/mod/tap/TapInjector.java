package com.honeycomb.mod.tap;

import com.honeycomb.lib.utilities.CommandLine;

public class TapInjector {

    void injectTap(int x, int y) {
        CommandLine.execute("input", "tap", String.valueOf(x), String.valueOf(y));
    }
}
