package ch.roomManager.service;

import ch.roomManager.dao.DynamicDao;
import ch.roomManager.models.Event;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * service controller for events
 * <p>
 * Room Manager
 *
 * @author Dominic Lumsden
 */
@Path("event")
public class EventService {

    private final DynamicDao<Event> eventDao;

    public EventService() {
        this.eventDao = new DynamicDao<>(Event.class);
    }

    /**
     * produces the number of events
     *
     * @param token encrypted authorization token
     * @return Response
     */
    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)

    public Response countEvents(
        @CookieParam("token") String token
    ) {

        int httpStatus = 200;
        Integer eventCount = eventDao.count();

        return Response
            .status(httpStatus)
            .entity("{\"eventCount\":" + eventCount + "}")
            .build();
    }

    /**
     * produces a list of all events
     *
     * @param token encrypted authorization token
     * @return Response
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)

    public Response listEvents(
        @CookieParam("token") String token
    ) {

        int httpStatus = 200;
        List<Event> eventList = eventDao.getAll();
        if (eventList.isEmpty())
            httpStatus = 404;

        return Response
            .status(httpStatus)
            .entity(eventList)
            .build();
    }

    /**
     * reads a single event identified by the eventId
     *
     * @param eventId the eventId in the URL
     * @return Response
     */
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)

    public Response readEvent(
        @QueryParam("eventId") int eventId,
        @CookieParam("token") String token
    ) {
        int httpStatus = 200;
        Event event = eventDao.getEntity(eventId);
        if (event.getClass() == null)
            httpStatus = 404;

        return Response
            .status(httpStatus)
            .entity(event)
            .build();
    }
}
