package com.play.playsystem.chat.service.impl;

import com.play.playsystem.basic.handler.MyWebSocketHandler;
import com.play.playsystem.chat.service.INoticeService;
import com.play.playsystem.user.service.IUserStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NoticeServiceImpl implements INoticeService {
    @Autowired
    private MyWebSocketHandler webSocketHandler;

    @Autowired
    private IUserStatusService userStatusService;

    public void sendFriendRequest(Long toUserId) throws IOException {
        if (userStatusService.isUserOnline(toUserId)) {
            // 立即通知在线用户
            webSocketHandler.sendMessageToUser(toUserId, "你有新的好友申请！");
        } else {
            // Save the request for later notification
        }
        // 保存好友请求
    }

    //public void acceptFriendRequest(Long fromUserId, Long toUserId) throws IOException {
    //    // Add both users to each other's friend list in the database
    //
    //    if (userStatusService.isUserOnline(fromUserId)) {
    //        // Notify the original requester
    //        webSocketHandler.sendMessageToUser(fromUserId, toUserId + " accepted your friend request");
    //    }
    //}
}
