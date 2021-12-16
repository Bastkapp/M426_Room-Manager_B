package ch.roomManager.service;

import ch.roomManager.data.Dao;
import ch.roomManager.data.ReservationDAO;
import ch.roomManager.models.Reservation;


import javax.validation.Valid;
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
        Integer reservationCount = new ReservationDAO().count();

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
        Dao<Reservation, String> reservationDao = new ReservationDAO();
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
     * @param reservationUUID the reservationUUID in the URL
     * @return Response
     */
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)

    public Response readReservation(
            @QueryParam("uuid") String reservationUUID,
            @CookieParam("token") String token
    ) {
        int httpStatus = 200;
        Dao<Reservation, String> reservationDAO = new ReservationDAO();
        Reservation reservation = reservationDAO.getEntity(reservationUUID);
        if (reservation.getStart().toString() == null)
            httpStatus = 404;

        return Response
                .status(httpStatus)
                .entity(reservation)
                .build();
    }
}