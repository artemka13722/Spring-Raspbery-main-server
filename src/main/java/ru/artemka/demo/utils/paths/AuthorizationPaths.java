package ru.artemka.demo.utils.paths;

import static ru.artemka.demo.utils.paths.CommonPaths.USER_PREFIX;

public class AuthorizationPaths {
    public static final String SIGN_IN = USER_PREFIX + "/sign-in";

    public static final String SIGN_UP = USER_PREFIX + "/sign-up";

    public static final String LOGOUT = "/logout";

    public static final String REFRESH_TOKENS = "/token-refresh";

    public static final String EMAIL_CONFIRM = "/confirm";

    public static final String PASSWORD_RESTORE = USER_PREFIX + "/restore";
}
