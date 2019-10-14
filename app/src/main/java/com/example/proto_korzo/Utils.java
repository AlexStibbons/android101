package com.example.proto_korzo;

public class Utils {

    public static final String IF_FAVE_CHANGED = "com.example.proto_korzo.FAVE_CHANGE";

    public static boolean hasLoginData(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            return true;
        }
        return false;
    }




}
