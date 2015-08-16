package com.sundbybergsit.authentication;

import com.sundbybergsit.objects.User;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.impl.ResponseBuilderImpl;
import org.apache.tomee.embedded.EmbeddedTomEEContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.annotation.Resource;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;


public class AuthenticateResourceTest {

    protected static final String WEB_APPLICATION_NAME = "SundbybergsITAuthentication";
    private static EJBContainer container;
    private static File webApp;

    @BeforeClass
    public static void start() throws IOException {
        webApp = createWebApp();
        Properties p = new Properties();
        p.setProperty(EJBContainer.APP_NAME, WEB_APPLICATION_NAME);
        p.setProperty(EJBContainer.PROVIDER, "tomee-embedded"); // need web feature
        p.setProperty(EJBContainer.MODULES, webApp.getAbsolutePath());
        p.setProperty(EmbeddedTomEEContainer.TOMEE_EJBCONTAINER_HTTP_PORT, "-1"); // random port
        container = EJBContainer.createEJBContainer(p);
    }

    @AfterClass
    public static void stop() {
        if (container != null) {
            container.close();
        }
        if (webApp != null) {
            try {
                FileUtils.forceDelete(webApp);
            } catch (IOException e) {
                FileUtils.deleteQuietly(webApp);
            }
        }
    }

    @Test
    public void queryForUserAndThenAddUser() throws Exception {
        String uri = "http://127.0.0.1:" + System.getProperty(EmbeddedTomEEContainer.TOMEE_EJBCONTAINER_HTTP_PORT) + "/" + WEB_APPLICATION_NAME;

        UserServiceClientAPI client = JAXRSClientFactory.create(uri, UserServiceClientAPI.class);
        User user = client.authenticate("alwa", "password", "fatman");
        assertThat(user, nullValue());
        Calendar calendar = Calendar.getInstance();
        calendar.set(1983, Calendar.APRIL, 20);
        User newUser = createUser();
        Response response = client.addUser(newUser);
        assertThat(response.getStatus(), is(201));
        user = client.authenticate("alwa", "password", "fatman");
        assertThat(user.getFirstname(), is(newUser.getFirstname()));
    }

    private User createUser() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1983, Calendar.APRIL, 20);
        return new User("alwa", "password", "Alix", "Warnke", new Date(calendar.getTimeInMillis()));
    }

    private static File createWebApp() throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir") + "/tomee-" + Math.random());
        if (!file.mkdirs() && !file.exists()) {
            throw new RuntimeException("can't create " + file.getAbsolutePath());
        }

        FileUtils.copyDirectory(new File("target/classes"), new File(file, "WEB-INF/classes"));

        return file;
    }

    /**
     * a simple copy of the unique method i want to use from my service.
     * It allows to use cxf proxy to call remotely our rest service.
     * Any other way to do it is good.
     */
    @Path("/ws/authenticationservice")
    @Produces({"text/xml", "application/json"})
    public static interface UserServiceClientAPI {

        @Path("/authenticate/{username}/{password}/{application}")
        @GET
        User authenticate(@PathParam("username") String username,
                          @PathParam("password") String password,
                          @PathParam("application") String application);

        @POST
        @Path("/users")
        @Consumes({"application/json", MediaType.MULTIPART_FORM_DATA})
        Response addUser(User user) throws Exception;
    }
}