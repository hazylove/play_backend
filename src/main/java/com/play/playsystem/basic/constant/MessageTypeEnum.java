package com.play.playsystem.basic.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageTypeEnum {
    FRIEND_APPLICATION("friend_application", "好友申请");

    @EnumValue
    @JsonValue
    private final String key;

    private final String value;

    public String toString() {
        return value;
    }
}
