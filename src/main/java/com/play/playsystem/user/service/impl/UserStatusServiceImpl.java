package com.play.playsystem.user.service.impl;

import com.play.playsystem.user.service.IUserStatusService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class UserStatusServiceImpl implements IUserStatusService {
    public final ConcurrentMap<Long, Boolean> onlineUsers = new ConcurrentHashMap<>();

    @Override
    public void userOnline(Long userId) {
        onlineUsers.put(userId, true);
    }

    @Override
    public void userOffline(Long userId) {
        onlineUsers.remove(userId);
    }

    @Override
    public boolean isUserOnline(Long userId) {
        return onlineUsers.containsKey(userId);
    }
}
