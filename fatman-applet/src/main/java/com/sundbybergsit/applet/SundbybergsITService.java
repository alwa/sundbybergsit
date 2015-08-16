package com.sundbybergsit.applet;
import com.sundbybergsit.objects.FatmanUser;
import com.sundbybergsit.objects.PersonDataEntry;
import com.sundbybergsit.objects.TimePeriodType;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Z
 * Date: 2013-03-25
 * Time: 20:55
 * To change this template use File | Settings | File Templates.
 */
public class SundbybergsITService {

    public List<PersonDataEntry> getPersonDataEntries(String fatmanUser, TimePeriodType timeScale) {
        FatmanDataResource fatmanDataResource = JAXRSClientFactory.create("http://www.sundbybergsit.com/SundbybergsITFatman/ws/fatmandataservice/", FatmanDataResource.class);
        String response = fatmanDataResource.helloWorld();
        if (!response.startsWith("Hello")) {
            throw new RuntimeException("FatmanUserResource is not up and running");
        }
        return fatmanDataResource.list(fatmanUser, timeScale.name());
    }

    @Path("/ws/fatmandataservice")
    @Produces({"text/xml", "application/json"})
    private interface FatmanDataResource {
        @GET
        public String helloWorld();

        @GET
        @Path("/{username}/{timeperiodtype}")
        List<PersonDataEntry> list(@PathParam("username") String username,
                                   @PathParam("timeperiodtype") String timePeriodType);

        @POST
        public Response add(PersonDataEntry personDataEntry);
    }
}
