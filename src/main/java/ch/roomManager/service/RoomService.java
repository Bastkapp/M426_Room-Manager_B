package ch.roomManager.service;

import ch.roomManager.dao.DynamicDao;
import ch.roomManager.models.Room;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * service controller for rooms
 * <p>
 * Room Manager
 *
 * @author Bastian Kappeler
 */
@Path("room")
public class RoomService extends Service {

    private final DynamicDao<Room> roomDao;

    public RoomService() {
        this.roomDao = new DynamicDao<>(Room.class);
    }

    /**
     * produces the number of rooms
     *
     * @param token encrypted authorization token
     * @return Response
     */
    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)

    public Response countRooms(
        @CookieParam("token") String token
    ) {

        Integer roomCount = roomDao.count();

        return Response
            .status(getHttpStatus())
            .entity(Integer.toString(roomCount))
            .build();
    }

    /**
     * produces a list of all rooms
     *
     * @param token encrypted authorization token
     * @return Response
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)

    public Response listRooms(
        @CookieParam("token") String token
    ) {

        List<Room> roomList = roomDao.getAll();

        return Response
            .status(getHttpStatus())
            .entity(roomList)
            .build();
    }

    @POST
    @Path("save")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveRoom(
        @QueryParam("id") int roomId,
        @Valid @BeanParam Room room,
        @CookieParam("token") String token
    ) {
        room.setId(roomId);
        roomDao.save(room);

        return Response
            .status(getHttpStatus())
            .build();
    }

    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteRoom (
        @QueryParam("id") int roomId
    ) {
        roomDao.delete(roomId);

        return Response
            .status(getHttpStatus())
            .build();
    }

    /**
     * reads a single room identified by the roomId
     *
     * @param roomId the roomId in the URL
     * @return Response
     */
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)

    public Response readRoom(
        @QueryParam("roomId") int roomId,
        @CookieParam("token") String token
    ) {
        Room room = roomDao.getEntity(roomId);

        return Response
            .status(getHttpStatus())
            .entity(room)
            .build();
    }
}
