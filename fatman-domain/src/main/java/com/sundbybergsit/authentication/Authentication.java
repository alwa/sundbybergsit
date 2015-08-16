package com.sundbybergsit.authentication;

import java.io.IOException;

public interface Authentication {
    void login();

    boolean isLoggedIn();

    void logout() throws IOException;
}
