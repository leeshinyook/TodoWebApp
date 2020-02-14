package com.todo.domain.enums;

public enum  BoardCategory {
    STUDY("공부"),
    FREE("자유"),
    HW("과제"),
    MEETING("미팅");

    private String value;

    BoardCategory(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
