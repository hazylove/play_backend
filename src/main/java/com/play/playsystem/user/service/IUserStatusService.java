package com.play.playsystem.user.service;

public interface IUserStatusService {

    void userOnline(Long userId);

    void userOffline(Long userId);

    boolean isUserOnline(Long userId);
}
