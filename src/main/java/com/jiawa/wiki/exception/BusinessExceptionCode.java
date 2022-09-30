package com.jiawa.wiki.exception;

public enum BusinessExceptionCode {

    USER_LOGIN_NAME_EXIST("亲，登录名已存在啦！"),
    LOGIN_USER_ERROR("亲，当前用户名不存在或密码错误哦！"),
    VOTE_REPEAT("亲，您已点赞过啦！"),
    ;

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
