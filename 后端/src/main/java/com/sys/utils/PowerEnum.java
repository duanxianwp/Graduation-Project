package com.sys.utils;

public enum PowerEnum {
    ADMIN(9),
    REVIEW(5),
    TEACHER(2),
    STUDENT(1);

    private int level;

    PowerEnum(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
