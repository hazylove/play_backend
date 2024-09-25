package com.play.playsystem.chat.domain.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatTypeEnum {
    TEXT("text", "文本");

    @EnumValue
    @JsonValue
    private final String key;

    private final String value;

    public String toString() {
        return value;
    }

    public static ChatTypeEnum fromKey(String key) {
        for (ChatTypeEnum type : values()) {
            if (type.key.equalsIgnoreCase(key)) {
                return type;
            }
        }
        throw new IllegalArgumentException("获取类型失败: " + key);
    }
}
