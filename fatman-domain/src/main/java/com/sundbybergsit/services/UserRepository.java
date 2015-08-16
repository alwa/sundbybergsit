package com.sundbybergsit.services;

import com.sundbybergsit.entities.FatmanDbUser;

import java.util.List;

public interface UserRepository {

    boolean delete(long id);

    List<FatmanDbUser> findUsersWithPublicData();

    FatmanDbUser findMostPhantomThisWeek();

    FatmanDbUser findFattestThisWeek();

    List<FatmanDbUser> findUsers();

    void saveNew(FatmanDbUser fatmanDbUser);

    void update(FatmanDbUser existingUser);

    FatmanDbUser findUser(long id);

    FatmanDbUser findUserByUserName(String username);

    boolean existsUser(String userName);

}
