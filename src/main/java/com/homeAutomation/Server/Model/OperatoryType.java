package com.homeAutomation.Server.Model;

import java.util.HashMap;
import java.util.Map;

public enum OperatoryType {
    AND(0),
    OR(1),
    GRT(2),
    GRT_EQ(3),
    LESS(4),
    LESS_EQ(5);

    private int value;
    private static Map map = new HashMap();

    private OperatoryType(int value) {
        this.value = value;
    }

    static {
        for (OperatoryType pageType : OperatoryType.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static OperatoryType valueOf(int pageType) {
        return (OperatoryType) map.get(pageType);
    }

    public int getValue() {
        return value;
    }
}
