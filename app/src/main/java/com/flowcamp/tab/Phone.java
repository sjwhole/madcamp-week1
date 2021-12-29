package com.flowcamp.tab;

public class Phone {
    String name;
    String num;
    int phoneId;

    public Phone(int phoneId, String name, String num) {
        this.name = name;
        this.num = num;
        this.phoneId = phoneId;
    }

    public String getName() {
        return name;
    }

    public String getNum() {
        return num;
    }
}
