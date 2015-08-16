package com.sundbybergsit.services;

public interface ImageRepository {
    void updatePhantomImage(String userId, byte[] phantomImage);

    void updateFatImage(String userId, byte[] fatImageContents);
}
