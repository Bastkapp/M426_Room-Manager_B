package ch.roomManager.service;

import ch.roomManager.data.Dao;
import ch.roomManager.data.EventDAO;
import ch.roomManager.models.Event;

import javax.validation.Valid;
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
        Integer eventCount = new EventDAO().count();

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
        Dao<Event, String> eventDao = new EventDAO();
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
     * @param eventUUID the eventUUID in the URL
     * @return Response
     */
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)

    public Response readEvent(
            @QueryParam("uuid") String eventUUID,
            @CookieParam("token") String token
    ) {
        int httpStatus = 200;
        Dao<Event, String> eventDAO = new EventDAO();
        Event event = eventDAO.getEntity(eventUUID);
        if (event.getTitle() == null)
            httpStatus = 404;

        return Response
                .status(httpStatus)
                .entity(event)
                .build();
    }
}
