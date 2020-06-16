package com.yuechen.whiteboard.Service;

public interface LoginServiceObserver {
    /**
     *
     * @param result 登录成功为 true，失败为 false.
     * @param message 如果登录成功，值为用户的名字；否则为错误的原因。
     */
    public void notifyLoginVerifyResult(Boolean result, String message);
}
