package com.sundbybergsit;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/ws")
public class FatmanApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        // register root resource
        classes.add(FatmanDataResource.class);
        classes.add(FatmanUserResource.class);
        return classes;
    }
}