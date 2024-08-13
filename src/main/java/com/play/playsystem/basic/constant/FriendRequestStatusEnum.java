package com.play.playsystem.basic.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FriendRequestStatusEnum {

    PENDING("pending", "待处理"), // 申请待处理
    ACCEPTED("accepted", "已通过"), // 申请已通过
    REJECTED("rejected", "已拒绝"), // 申请已拒绝
    WITHDRAWN("withdrawn", "已撤回"), // 申请已撤回
    EXPIRED("expired", "已过期"), // 申请已过期
    CANCELLED("cancelled", "已取消"); // 申请已取消

    @EnumValue
    private final String key;

    private final String value;

    public String toString() {
        return value;
    }


    //public static FriendRequestStatusEnum fromKey(String key) {
    //    for (FriendRequestStatusEnum status : values()) {
    //        if (status.getKey().equals(key)) {
    //            return status;
    //        }
    //    }
    //    throw new IllegalArgumentException("Unknown status: " + key);
    //}

    //public static SexStatusConstant of(Integer key) {
    //    Objects.requireNonNull(key);
    //    return Stream.of(values()).filter(bean -> bean.getKey().equals(key))
    //            .findFirst()
    //            .orElseThrow(() -> new RuntimeException("no search bean by key=" + key));
    //}

}

