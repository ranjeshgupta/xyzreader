package com.example.xyzreader.remote;

import java.io.IOException;
import java.net.URL;

public class Config {
    public static final URL BASE_URL;

    static {
        URL url = null;
        try {
            url = new URL("https://dl.dropboxusercontent.com/u/231329/xyzreader_data/data.json" );
        } catch (IOException e) {
            throw new RuntimeException("Invalid url: " + e);
        }

        BASE_URL = url;
    }
}
