package com.example.springbook.user.domain;

public enum Level {
    // BASIC, SILVER, GOLD 순이 아닌 이유는 상위 레벨이 먼저 와야 하위 레벨이 다음 레벨을 참조할 수 있어서
    GOLD(3, null), SILVER(2, GOLD), BASIC(1, SILVER);

    private final int value;
    private final Level next;

    Level(int value, Level next){
        this.value = value;
        this.next = next;
    }

    public int intValue(){
        return value;
    }

    public Level nextLevel(){
        return this.next;
    }

    public static Level valueOf(int value){
        switch (value) {
            case 1: return BASIC;
            case 2: return SILVER;
            case 3: return GOLD;
            default: throw new AssertionError("Unknown value: " + value);
        }
    }
}
