package com.honeycomb.lib.utilities;

import java.io.IOException;

public class CommandLine {

    public static void execute(String... args) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(args);
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
