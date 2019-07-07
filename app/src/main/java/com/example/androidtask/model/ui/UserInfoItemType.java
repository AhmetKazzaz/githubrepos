package com.example.androidtask.model.ui;

import java.util.HashMap;
import java.util.Map;

public enum UserInfoItemType {
    details(100),
    repos(101);

    private final int type;
    private static Map map = new HashMap<>();

    static {
        for (UserInfoItemType type : UserInfoItemType.values()) {
            map.put(type.rawValue(), type);
        }
    }

    UserInfoItemType(int type) {
        this.type = type;
    }

    public int rawValue() {
        return type;
    }

    public static UserInfoItemType valueOf(int type) {
        return (UserInfoItemType) map.get(type);
    }
}
