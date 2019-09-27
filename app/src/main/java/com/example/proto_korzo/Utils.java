package com.example.proto_korzo;

public class Utils {

    public static boolean hasLoginData(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            return true;
        }
        return false;
    }




}
