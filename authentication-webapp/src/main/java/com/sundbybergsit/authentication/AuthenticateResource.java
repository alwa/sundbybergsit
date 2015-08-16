package com.sundbybergsit.authentication;

import com.sundbybergsit.authentication.entities.DbUser;
import com.sundbybergsit.authentication.services.AuthenticationService;
import com.sundbybergsit.objects.User;
import org.apache.commons.lang.Validate;
import org.apache.cxf.feature.Features;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * @author DrakPappa
 */
@Path("/ws/authenticationservice")
@Features(features = "org.apache.cxf.feature.LoggingFeature")
@Produces({"text/xml", "application/json"})
public class AuthenticateResource {
    @Inject
    private AuthenticationService service;

    @GET
    @Path("/hello")
    public String helloWorld() {
        return "Hello world";
    }

    @POST
    @Path("/users")
    @Consumes({"application/json", MediaType.MULTIPART_FORM_DATA})
    public Response addUser(User user) throws Exception {
        Validate.notNull(user, "User must not be null!");
        DbUser dbUser = new DbUser(user.getUsername(), user.getPassword(), user.getFirstname(),
                user.getLastname(), new java.sql.Date(user.getBirthday().getTime()));
        service.save(dbUser);
        // TODO: Make usable URI.
        return Response.created(URI.create(ConfigurationLoader.getBaseURL() + "/")).entity(dbUser.getId()).build();
    }

    @GET
    @Path("/authenticate/{username}/{password}/{application}")
    public User authenticate(@PathParam("username") String username,
                             @PathParam("password") String password,
                             @PathParam("application") String application) {

        DbUser persistedUser = service.findUser(username, password);
        if (persistedUser != null) {
            if (persistedUser.getPassword().equals(password)) {
                return new User(persistedUser.getUsername(),
                        persistedUser.getPassword(),
                        persistedUser.getFirstname(),
                        persistedUser.getLastname(),
                        persistedUser.getBirthday());
            }
        }
        return null;
    }
}
