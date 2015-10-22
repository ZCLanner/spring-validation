package me.lanner.spring.validation.handler;

/**
 * Created by zhaochen.zc on 15/10/22.
 */
public class ViolationMessageHolder {

    private static final ThreadLocal<String> threadLocalViolationMessage = new ThreadLocal<>();

    public static void setViolationMessage(String violationMessage) {
        threadLocalViolationMessage.set(violationMessage);
    }

    public static String getViolationMessage() {
        return threadLocalViolationMessage.get();
    }
}
