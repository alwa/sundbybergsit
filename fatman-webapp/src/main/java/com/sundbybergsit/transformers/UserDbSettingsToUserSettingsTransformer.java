package com.sundbybergsit.transformers;

import com.sundbybergsit.entities.UserDbSettings;
import com.sundbybergsit.objects.FatmanUser;
import com.sundbybergsit.objects.UserSettings;

public class UserDbSettingsToUserSettingsTransformer implements Transformer<UserDbSettings, UserSettings> {

    public UserSettings transform(UserDbSettings userDbSettings) {
        FatmanUser user = new FatmanDbUserToFatmanUserTransformer().transform(userDbSettings.getFatmanUser());
        return new UserSettings(user);
    }
}
