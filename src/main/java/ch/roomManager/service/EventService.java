package ch.roomManager.service;

import ch.roomManager.dao.DynamicDao;
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
 * @author Bastian Kappeler
 */
@Path("event")
public class EventService extends Service {

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

        Integer eventCount = eventDao.count();

        return Response
            .status(getHttpStatus())
            .entity(Integer.toString(eventCount))
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

        List<Event> eventList = eventDao.getAll();

        return Response
            .status(getHttpStatus())
            .entity(eventList)
            .build();
    }

    @POST
    @Path("save")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveEvent(
        @QueryParam("id") int eventId,
        @Valid @BeanParam Event event,
        @CookieParam("token") String token
    ) {
        event.setId(eventId);
        eventDao.save(event);

        return Response
            .status(getHttpStatus())
            .build();
    }

    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteEvent (
        @QueryParam("id") int eventId
    ) {
        eventDao.delete(eventId);

        return Response
            .status(getHttpStatus())
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
        Event event = eventDao.getEntity(eventId);

        return Response
            .status(getHttpStatus())
            .entity(event)
            .build();
    }
}
