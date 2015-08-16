package com.sundbybergsit;

import com.sundbybergsit.entities.PersonDataDbEntry;
import com.sundbybergsit.fatman.ConfigurationLoader;
import com.sundbybergsit.objects.PersonDataEntry;
import com.sundbybergsit.objects.TimePeriodType;
import com.sundbybergsit.services.FatmanDataService;
import com.sundbybergsit.services.PersonDataDbEntryRepository;
import com.sundbybergsit.transformers.PersonDataDbEntryToPersonDataEntryTransformer;
import org.apache.cxf.feature.Features;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author alwa
 *         <p/>
 *         From example @ http://svn.apache.org/viewvc/cxf/trunk/distribution/src/main/release/samples/jax_rs/basic/src/main/java/demo/jaxrs/server/CustomerService.java?view=markup
 */
@Path("/ws/fatmandataservice")
@Features(features = "org.apache.cxf.feature.LoggingFeature")
@Produces({"text/xml", "application/json"})
public class FatmanDataResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(FatmanDataResource.class);

    @Inject
    private PersonDataDbEntryRepository personDataDbEntryRepository;

    @Inject
    private FatmanDataService fatmanDataService;

    @GET
    public String helloWorld() {
        return "Hello world";
    }

    @GET
    @Path("/{username}/{timeperiodtype}")
    public List<PersonDataEntry> list(@PathParam("username") String username,
                                      @PathParam("timeperiodtype") String timePeriodType) throws Exception {
        LOGGER.info("Retrieving data entries for: {} and for the time period: {}", username, timePeriodType);
        List<PersonDataDbEntry> personDataDbEntries = findEntries(username, TimePeriodType.valueOf(timePeriodType));

        PersonDataEntry[] personDataEntries = new PersonDataEntry[personDataDbEntries.size()];
        for (int i = 0; i < personDataDbEntries.size(); i++) {
            personDataEntries[i] = new PersonDataDbEntryToPersonDataEntryTransformer().transform(personDataDbEntries.get(i));
        }
        return Arrays.asList(personDataEntries);
    }

    private List<PersonDataDbEntry> findEntries(String username, TimePeriodType timePeriodType) {
        switch (timePeriodType) {
            case ALL:
                return personDataDbEntryRepository.findAllEntries(username);
            case THIS_MONTH:
                Calendar lastMonth = Calendar.getInstance();
                lastMonth.set(Calendar.MONTH, -1);
                return personDataDbEntryRepository.findAllEntries(username, lastMonth.getTime(), new Date());
            case THIS_WEEK:
                Calendar lastWeek = Calendar.getInstance();
                lastWeek.add(Calendar.DAY_OF_MONTH, -7);
                return personDataDbEntryRepository.findAllEntries(username, lastWeek.getTime(), new Date());
            default:
                throw new UnsupportedOperationException("Unknown time period type: " + timePeriodType);
        }
    }

    @POST
    public Response add(PersonDataEntry personDataEntry) throws SystemException, NotSupportedException {
        try {
            LOGGER.info("Adding data entry: {}", personDataEntry);
            fatmanDataService.save(personDataEntry);
            return Response.created(URI.create(ConfigurationLoader.getProperty("host.url") + "/SundbybergsITFatman/ws/fatmandataservice/" + personDataEntry.getFatmanUser().getUsername() + "/ALL")).build();
        } catch (Exception e) {
            LOGGER.error("Exception: " + e.getMessage());
            return Response.serverError().build();
        }
    }
}