package com.example.proto_korzo;

public class Utils {
    // extras and intent actions
    public static final String IF_FAVE_CHANGED = "com.example.proto_korzo.FAVE_CHANGE";

    // api related
    public static final String API_KEY = "KEY";
    public static final String BASE_MOVIEDB_URL = "https://api.themoviedb.org/3/";
    public static final String BASE_IMG_URL = "https://image.tmdb.org/t/p/w500";
    public static final String POPULARITY_DESC = "popularity.desc";

    public static boolean hasLoginData(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            return true;
        }
        return false;
    }


}
