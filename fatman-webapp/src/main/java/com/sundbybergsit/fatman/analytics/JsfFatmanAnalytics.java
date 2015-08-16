package com.sundbybergsit.fatman.analytics;

import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.services.UserRepository;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

@RequestScoped
@ManagedBean(name = "fatmanAnalyticsBean")
public class JsfFatmanAnalytics implements FatmanAnalytics {

    @Inject
    private UserRepository userRepository;

    @Override
    public FatmanDbUser getMostPhantomManThisWeek() {
        return userRepository.findMostPhantomThisWeek();
    }

    @Override
    public FatmanDbUser getFattestManThisWeek() {
        return userRepository.findFattestThisWeek();
    }
}
