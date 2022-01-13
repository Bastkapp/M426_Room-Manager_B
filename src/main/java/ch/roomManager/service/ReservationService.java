package ch.roomManager.service;

import ch.roomManager.dao.DynamicDao;
import ch.roomManager.models.Reservation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * service controller for reservations
 * <p>
 * Room Manager
 *
 * @author Dominic Lumsden
 */
@Path("reservation")
public class ReservationService {

    private final DynamicDao<Reservation> reservationDao;

    public ReservationService() {
        this.reservationDao = new DynamicDao<>(Reservation.class);
    }

    /**
     * produces the number of reservations
     *
     * @param token encrypted authorization token
     * @return Response
     */
    @GET
    @Path("count")
    @Produces(MediaType.APPLICATION_JSON)

    public Response countReservations(
        @CookieParam("token") String token
    ) {

        int httpStatus = 200;
        Integer reservationCount = reservationDao.count();

        return Response
            .status(httpStatus)
            .entity("{\"reservationCount\":" + reservationCount + "}")
            .build();
    }

    /**
     * produces a list of all reservations
     *
     * @param token encrypted authorization token
     * @return Response
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)

    public Response listReservations(
        @CookieParam("token") String token
    ) {

        int httpStatus = 200;
        List<Reservation> reservationList = reservationDao.getAll();
        if (reservationList.isEmpty())
            httpStatus = 404;

        return Response
            .status(httpStatus)
            .entity(reservationList)
            .build();
    }

    /**
     * reads a single reservation identified by the reservationId
     *
     * @param reservationId the reservationId in the URL
     * @return Response
     */
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)

    public Response readReservation(
        @QueryParam("id") int reservationId,
        @CookieParam("token") String token
    ) {
        int httpStatus = 200;
        Reservation reservation = reservationDao.getEntity(reservationId);
        if (reservation.getStart() == null)
            httpStatus = 404;

        return Response
            .status(httpStatus)
            .entity(reservation)
            .build();
    }
}