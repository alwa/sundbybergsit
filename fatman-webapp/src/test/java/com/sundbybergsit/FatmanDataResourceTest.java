package com.sundbybergsit;

import com.sundbybergsit.objects.FatmanUser;
import com.sundbybergsit.objects.PersonDataEntry;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.tomee.embedded.EmbeddedTomEEContainer;
import org.junit.*;

import javax.ejb.embeddable.EJBContainer;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FatmanDataResourceTest {

    private static EJBContainer container;
    private static File webApp;

    @BeforeClass
    public static void start() throws Exception {
        webApp = createWebApp();
        Properties p = new Properties();
        p.setProperty(EJBContainer.APP_NAME, "fatman-webapp");
        p.setProperty(EJBContainer.PROVIDER, "tomee-embedded"); // need web feature
        p.setProperty(EJBContainer.MODULES, webApp.getAbsolutePath());
        p.setProperty(EmbeddedTomEEContainer.TOMEE_EJBCONTAINER_HTTP_PORT, "-1"); // random port
        container = EJBContainer.createEJBContainer(p);
    }

    @Before
    public void setUp() throws Exception {
        String uri = "http://127.0.0.1:" + System.getProperty(EmbeddedTomEEContainer.TOMEE_EJBCONTAINER_HTTP_PORT) + "/fatman-webapp";
        FatmanUserServiceClientAPI userClient = JAXRSClientFactory.create(uri, FatmanUserServiceClientAPI.class);
        userClient.clear();
    }

    @Test
    public void getPersonDataEntries() throws Exception {
        DataServiceClientAPI client = createTestClient();
        List<PersonDataEntry> dataEntries = client.list("alwa", "ALL");
        assertThat(dataEntries.size(), is(0));
    }

    @Test
    public void addPersonDataEntry() throws Exception {
        String uri = "http://127.0.0.1:" + System.getProperty(EmbeddedTomEEContainer.TOMEE_EJBCONTAINER_HTTP_PORT) + "/fatman-webapp";
        FatmanUserServiceClientAPI userClient = JAXRSClientFactory.create(uri, FatmanUserServiceClientAPI.class);
        FatmanUser user = new FatmanUser("fatso", 150, "Fat", "So", new Date(1950, 1, 1));
        Response response = userClient.addUser(user);
        assertThat(response.getStatus(), is(201));

        DataServiceClientAPI client = createTestClient();
        Response dataResponse = client.add(new PersonDataEntry(user, 90, 35, 45, new Date(), 0));
        assertThat(dataResponse.getStatus(), is(201));
        List<PersonDataEntry> dataEntries = client.list("fatso", "ALL");
        assertThat(dataEntries.get(0).getFatmanUser().getUsername(), is("fatso"));
    }

    private DataServiceClientAPI createTestClient() {
        String uri = "http://127.0.0.1:" + System.getProperty(EmbeddedTomEEContainer.TOMEE_EJBCONTAINER_HTTP_PORT) + "/fatman-webapp";
        return JAXRSClientFactory.create(uri, DataServiceClientAPI.class);
    }

    private static File createWebApp() throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir") + "/tomee-" + Math.random());
        if (!file.mkdirs() && !file.exists()) {
            throw new RuntimeException("can't create " + file.getAbsolutePath());
        }

        FileUtils.copyDirectory(new File("target/classes"), new File(file, "WEB-INF/classes"));

        return file;
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

    /**
     * a simple copy of the unique method i want to use from my service.
     * It allows to use cxf proxy to call remotely our rest service.
     * Any other way to do it is good.
     */
    @Path("/ws/fatmandataservice")
    @Produces({"text/xml", "application/json"})
    public static interface DataServiceClientAPI {

        @GET
        String helloWorld();

        @GET
        @Path("/{username}/{timeperiodtype}")
        List<PersonDataEntry> list(@PathParam("username") String username,
                                   @PathParam("timeperiodtype") String timePeriodType) throws Exception;

        @POST
        Response add(PersonDataEntry personDataEntry);
    }

    @Path("/ws/fatmanuserservice/")
    @Produces("application/json")
    @Consumes("application/json")
    public static interface FatmanUserServiceClientAPI {
        @POST
        @Path("/users")
        Response addUser(FatmanUser user);

        @DELETE
        @Path("/users/clear")
        void clear();
    }
}