package com.flowcamp.tab;

import android.graphics.Bitmap;

public class Phone {
    String name;
    String num;
    Bitmap profile;
    int phoneId;

    public Phone(int phoneId, String name, String num, Bitmap profile) {
        this.name = name;
        this.num = num;
        this.profile = profile;
        this.phoneId = phoneId;
    }

    public String getName() {
        return name;
    }

    public String getNum() {
        return num;
    }

    public Bitmap getProfile() { return profile; }
}
