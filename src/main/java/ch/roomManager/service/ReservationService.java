package ch.roomManager.service;

import ch.roomManager.dao.DynamicDao;
import ch.roomManager.dao.Result;
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

    @POST
    @Path("save")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveReservation(
            @QueryParam("id") int reservationId,
            @CookieParam("token") String token
    ) {
        int httpStatus;
        Reservation reservation = reservationDao.getEntity(reservationId);
        String message;
        Result result = reservationDao.save(reservation);
        if (result == Result.SUCCESS) {
            message = "Reservation erfasst";
            httpStatus = 200;
        } else if (result == Result.DUPLICATE) {
            message = "Diese Reservation existiert bereits.";
            httpStatus = 422;
        }
        else {
            message = "Fehler beim Speichern der Reservation";
            httpStatus = 500;
        }
        return Response
                .status(httpStatus)
                .entity(message)
                .build();
    }


    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteReservation(
            @QueryParam("id") int reservationId,
            @CookieParam("token") String token
    ) {
        int httpStatus = 200;
        Reservation reservation = reservationDao.getEntity(reservationId);
        Result result = reservationDao.delete(reservationId);
        if (result != Result.SUCCESS) httpStatus = 500;
        return Response
                .status(httpStatus)
                .entity("")
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
        if (reservation == null)
            httpStatus = 404;

        return Response
            .status(httpStatus)
            .entity(reservation)
            .build();
    }
}