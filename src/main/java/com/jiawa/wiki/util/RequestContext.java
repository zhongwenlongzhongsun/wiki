package com.jiawa.wiki.util;

//过滤
import java.io.Serializable;

public class RequestContext implements Serializable {

    //线程本地变量： 在一个线程中在不同地方赋值取值
    public static ThreadLocal<String> remoteArr = new ThreadLocal<>();

    //取值
    public static String getRemoteAddr(){
        return remoteArr.get();
    }

    //赋值
    public static void setRemoteArr(String remoteArr){
        RequestContext.remoteArr.set(remoteArr);
    }
}
