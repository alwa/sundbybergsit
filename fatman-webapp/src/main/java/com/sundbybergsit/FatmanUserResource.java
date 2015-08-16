package com.sundbybergsit;

import com.sundbybergsit.entities.FatmanDbUser;
import com.sundbybergsit.entities.UserDbSettings;
import com.sundbybergsit.fatman.ConfigurationLoader;
import com.sundbybergsit.objects.FatmanUser;
import com.sundbybergsit.objects.UserSettings;
import com.sundbybergsit.services.UserRepository;
import com.sundbybergsit.services.UserSettingsRepository;
import com.sundbybergsit.transformers.FatmanDbUserToFatmanUserTransformer;
import com.sundbybergsit.transformers.UserDbSettingsToUserSettingsTransformer;
import org.apache.cxf.feature.Features;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author alwa
 *         <p/>
 *         From example @ http://svn.apache.org/viewvc/cxf/trunk/distribution/src/main/release/samples/jax_rs/basic/src/main/java/demo/jaxrs/server/CustomerService.java?view=markup
 */
@Path("/ws/fatmanuserservice")
@Features(features = "org.apache.cxf.feature.LoggingFeature")
@Produces("application/json")
@Consumes("application/json")
public class FatmanUserResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(FatmanUserResource.class);

    @Inject
    private UserSettingsRepository userSettingsRepository;

    @Inject
    private UserRepository userRepository;

    @GET
    @Path("/users")
    public List<FatmanUser> getUsers() {
        LOGGER.info("Retrieving users");
        List<FatmanUser> userDTOs = new ArrayList<>();
        for (FatmanDbUser persistedUser : userRepository.findUsers()) {
            userDTOs.add(new FatmanDbUserToFatmanUserTransformer().transform(persistedUser));
        }
        return userDTOs;
    }

    @GET
    @Path("/users/{id}")
    public FatmanUser getUser(@PathParam("id") long id) {
        LOGGER.info("Retrieving user: {}", id);
        FatmanDbUser user = userRepository.findUser(id);
        if (user != null) {
            return new FatmanDbUserToFatmanUserTransformer().transform(user);
        } else {
            return null;
        }
    }

    @GET
    @Path("/settings/{id}")
    public UserSettings getSettings(@PathParam("id") long id) {
        LOGGER.info("Retrieving user settings for: {}", id);
        UserDbSettings userSettings = userSettingsRepository.findSettingsForUser(id);
        if (userSettings != null) {
            return new UserDbSettingsToUserSettingsTransformer().transform(userSettings);
        } else {
            return null;
        }
    }

    @POST
    @Path("/users")
    public Response addUser(FatmanUser user) {
        LOGGER.info("Creating user: {}", user.getUsername());
        if (!userRepository.existsUser(user.getUsername())) {

            FatmanDbUser fatmanDbUser = new FatmanDbUser(user.getUsername(),
                    user.getHeightInCentimetres(),
                    new java.sql.Date(user.getBirthday().getTime()),
                    user.getFirstName(),
                    user.getLastName());

            userRepository.saveNew(fatmanDbUser);
            return Response.created(URI.create(ConfigurationLoader.getProperty("host.url") + "/SundbybergsITFatman/fatmanuserservice/users/" + fatmanDbUser.getId()))
                    .entity(fatmanDbUser.getId()).build();
        }
        return Response.notModified().build();
    }

    @DELETE
    @Path("/users/clear")
    @TransactionAttribute
    public void clear() {
        List<FatmanDbUser> users = userRepository.findUsers();
        for (FatmanDbUser user : users) {
            userRepository.delete(user.getId());
        }
    }

    @PUT
    @Path("/users/{id}")
    @TransactionAttribute
    public Response updateUser(FatmanUser user, @PathParam("id") long id) throws Exception {
        LOGGER.info("Updating user: {}", id);

        FatmanDbUser existingUser = userRepository.findUser(id);
        if (existingUser != null) {
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setHeightInCentimetres(user.getHeightInCentimetres());
            userRepository.update(existingUser);
            return Response.ok().build();
        } else {
            throw new IllegalArgumentException("THIS USER ALREADY EXISTS!");
//            return addFatmanUser(user);
        }
    }

    @PUT
    @Path("/settings/{id}")
    @TransactionAttribute
    public Response updateSettings(UserSettings settings, @PathParam("id") long id) throws Exception {
        LOGGER.info("Updating settings for user: {}", id);

//        Optional<UserDbSettings> optional = Optional.of(userSettingsRepository.findSettingsForUser(id));
//        if (optional.isPresent()) {
//            optional.get().setScale(settings.getScale());
//            optional.get().setTargetFatPercentage(settings.getTargetFatPercentage());
//            optional.get().setTargetWaterPercentage(settings.getTargetWaterPercentage());
//            optional.get().setTargetWeight(settings.getTargetWeight());
//            userRepository.update(optional.get());
//            return Response.ok().build();
//        }
        UserDbSettings dbSettings = userSettingsRepository.findSettingsForUser(id);
        if (dbSettings != null) {
            dbSettings.setScale(settings.getScale());
            dbSettings.setTargetFatPercentage(settings.getTargetFatPercentage());
            dbSettings.setTargetWaterPercentage(settings.getTargetWaterPercentage());
            dbSettings.setTargetWeight(settings.getTargetWeight());
            userSettingsRepository.update(dbSettings);
            return Response.ok().build();
        } else {
            throw new IllegalArgumentException("No settings found!");
        }
    }

    @DELETE
    @Path("/users/{id}")
    public Response delete(@PathParam("id") long id) {
        LOGGER.info("Deleting user: {}", id);
        if (userRepository.delete(id)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}