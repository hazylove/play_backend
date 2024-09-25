package com.play.playsystem.chat.domain.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatStatusEnum {
    UNREAD("unread", "未读"),
    READ("read", "已读"),
    FAILED("failed", "发送失败");

    @EnumValue
    @JsonValue
    private final String key;

    private final String value;

    public String toString() {
        return value;
    }
}
