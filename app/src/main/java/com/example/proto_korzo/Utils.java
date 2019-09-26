package com.example.proto_korzo;

public class Utils {

    public static boolean hasLoginData(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            return true;
        }
        return false;
    }

    public static long findUserIdByEmail(String email){

        // better to return id only, not a whole User object
       /* User foundUser = db.getByEmail(email);
        if (foundUser != null) {
            return foundUser.getId();
        }*/

        return -1;
    }

}
