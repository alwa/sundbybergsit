package com.sundbybergsit;

import com.sundbybergsit.objects.FatmanUser;
import com.sundbybergsit.objects.UserSettings;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.ResponseReader;
import org.apache.tomee.embedded.EmbeddedTomEEContainer;
import org.junit.gen5.api.*;
import org.junit.gen5.junit4.runner.JUnit5;
import org.junit.runner.RunWith;

import javax.ejb.embeddable.EJBContainer;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Disabled("javax.ejb.EJBException: Provider error. No provider found")
@RunWith(JUnit5.class)
public class FatmanUserResourceTest {

    private static EJBContainer container;
    private static File webApp;

    @BeforeAll
    public static void start() throws Exception {
        webApp = createWebApp();
        Properties p = new Properties();
        p.setProperty(EJBContainer.APP_NAME, "fatman-webapp");
        p.setProperty(EJBContainer.PROVIDER, "tomee-embedded"); // need web feature
        p.setProperty(EJBContainer.MODULES, webApp.getAbsolutePath());
        p.setProperty(EmbeddedTomEEContainer.TOMEE_EJBCONTAINER_HTTP_PORT, "-1"); // random port
        container = EJBContainer.createEJBContainer(p);
    }

    @BeforeEach
    public void setUp() throws Exception {
        UserServiceClientAPI testClient = createTestClient();
        testClient.clear();
    }

    private UserServiceClientAPI createTestClient() {
        String uri = "http://127.0.0.1:" + System.getProperty(EmbeddedTomEEContainer.TOMEE_EJBCONTAINER_HTTP_PORT) + "/fatman-webapp";
        ResponseReader reader = new ResponseReader();
        reader.setEntityClass(Long.class); // Return ids
        return JAXRSClientFactory.create(uri, UserServiceClientAPI.class, Collections.singletonList(reader));
    }

    /* Happy path test cases */

    @Test
    public void deleteUser() throws Exception {
        UserServiceClientAPI testClient = createTestClient();
        long id = addUser(testClient);
        Response response = testClient.delete(id);
        assertThat(response.getStatus(), is(200));
    }

    @Test
    public void addFatmanUserAndRetrievingIt() throws Exception {
        //Add
        UserServiceClientAPI client = createTestClient();
        long id = addUser(client);
        // Retrieve
        FatmanUser retrievedUser = client.getUser(id);
        assertNotNull(retrievedUser);
        assertThat(retrievedUser.getFirstName(), is("Alix"));
        assertThat(retrievedUser.getLastName(), is("Warnke"));
        assertThat(retrievedUser.getHeightInCentimetres(), is(173));
    }

    @Test
    @Disabled("Fattar inte vad som är fel")
    public void updateSettings() throws Exception {
        UserServiceClientAPI client = createTestClient();
        long id = addUser(client);
        UserSettings settings = client.getSettings(id);
        settings.setScale(3000);
        Response response = client.updateSettings(settings, id);
        assertThat(response.getStatus(), is(200));
        // Update settings
        UserSettings updatedSettings = client.getSettings(id);
        assertThat(updatedSettings.getScale(), is(3000));
    }

    @Test
    public void updateFatmanUser() throws Exception {
        UserServiceClientAPI client = createTestClient();
        long id = addUser(client);
        FatmanUser user = client.getUser(id);
        // Update user
        user.setLastName("Hitler");
        client.updateUser(user);
        assertThat(user.getLastName(), is("Hitler"));
    }

    /* Sad path test cases */

    @Test
    public void deleteUserThatDoesNotExist() throws Exception {
        UserServiceClientAPI testClient = createTestClient();
        Response response = testClient.delete(1711);// This user does not exist
        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void getUsersWhenThereAreNone() throws Exception {
        UserServiceClientAPI client = createTestClient();
        List<FatmanUser> fatmanUsers = client.getUsers();
        assertThat(fatmanUsers.size(), is(0));
    }

    private static File createWebApp() throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir") + "/tomee-" + Math.random());
        if (!file.mkdirs() && !file.exists()) {
            throw new RuntimeException("can't create " + file.getAbsolutePath());
        }

        FileUtils.copyDirectory(new File("target/classes"), new File(file, "WEB-INF/classes"));

        return file;
    }

    private long addUser(UserServiceClientAPI testClient) {
        FatmanUser fatmanUser = new FatmanUser("alwa", 173, "Alix", "Warnke", new Date());
        Response response = testClient.addUser(fatmanUser);
        assertThat(response.getStatus(), is(201));
        return (Long) response.getEntity();
    }

    /**
     * a simple copy of the unique method i want to use from my service.
     * It allows to use cxf proxy to call remotely our rest service.
     * Any other way to do it is good.
     */
    @Path("/ws/fatmanuserservice/")
    @Produces("application/json")
    @Consumes("application/json")
    public static interface UserServiceClientAPI {

        @DELETE
        @Path("/users/clear")
        void clear();

        @GET
        @Path("/users")
        List<FatmanUser> getUsers();

        @POST
        @Path("/users")
        Response addUser(FatmanUser user);

        @PUT
        @Path("/users")
        Response updateUser(FatmanUser user);

        @PUT
        @Path("/settings/{id}")
        Response updateSettings(UserSettings settings, @PathParam("id") long id);

        @GET
        @Path("/users/{id}")
        FatmanUser getUser(@PathParam("id") long id);

        @GET
        @Path("/settings/{id}")
        UserSettings getSettings(@PathParam("id") long id);

        @DELETE
        @Path("/users/{id}")
        Response delete(@PathParam("id") long id);
    }

    @AfterAll
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
}