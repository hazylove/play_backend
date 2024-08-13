package com.play.playsystem.basic.utils.result;

import com.play.playsystem.basic.constant.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResult {
    /**
     * 消息类型
     */
    private MessageTypeEnum type;

    /**
     * 消息体
     */
    private Object data;
}
