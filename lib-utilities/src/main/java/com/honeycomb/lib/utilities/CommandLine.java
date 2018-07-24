package com.honeycomb.lib.utilities;

import com.honeycomb.util.IoUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class CommandLine {

    public static String execute(String... args) {
        String result = null;
        Process process = null;
        InputStream input = null;
        ByteArrayOutputStream output = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(args);
            process = processBuilder.start();

            if (process.exitValue() == 0) { // read std stream
                input = process.getInputStream();
            } else { // read err stream
                input = process.getErrorStream();
            }
            output = new ByteArrayOutputStream();
            IoUtils.dump(input, output);
            result = new String(output.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtils.closeQuietly(input);
            IoUtils.closeQuietly(output);
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }
}
