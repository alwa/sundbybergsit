package com.sundbybergsit.transformers;

import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.objects.FatmanUser;

public class FatmanDbUserToFatmanUserTransformer implements Transformer<FatmanDbUser, FatmanUser> {

    public FatmanUser transform(FatmanDbUser fatmanDbUser) {
        return new FatmanUser(fatmanDbUser.getUsername(),
                fatmanDbUser.getHeightInCentimetres(),
                fatmanDbUser.getFirstName(),
                fatmanDbUser.getLastName(),
                fatmanDbUser.getBirthday());
    }
}
