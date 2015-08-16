package com.sundbybergsit.services;

import com.sundbybergsit.entities.UserDbSettings;

public interface UserSettingsRepository {
    void showMyValuesToEveryone(String userId, boolean hide);

    void update(UserDbSettings settings);

    UserDbSettings findSettingsForUser(String userId);

    UserDbSettings findSettingsForUser(long userId);
}
