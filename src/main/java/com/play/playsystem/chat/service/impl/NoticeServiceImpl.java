package com.play.playsystem.chat.service.impl;

import com.play.playsystem.basic.handler.NotificationWebSocketHandlerMy;
import com.play.playsystem.chat.service.INoticeService;
import com.play.playsystem.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl implements INoticeService {
    @Autowired
    private NotificationWebSocketHandlerMy webSocketHandler;

    @Autowired
    private IUserService userService;

}
