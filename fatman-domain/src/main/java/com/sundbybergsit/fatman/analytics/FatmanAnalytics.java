package com.sundbybergsit.fatman.analytics;

import com.sundbybergsit.entities.FatmanDbUser;

public interface FatmanAnalytics {

    FatmanDbUser getMostPhantomManThisWeek();

    FatmanDbUser getFattestManThisWeek();
}
