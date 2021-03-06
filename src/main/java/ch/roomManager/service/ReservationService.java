package ch.roomManager.service;

import ch.roomManager.dao.DynamicDao;
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
 * @author Bastian Kappeler
 */
@Path("reservation")
public class ReservationService extends Service {

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

        Integer reservationCount = reservationDao.count();

        return Response
            .status(getHttpStatus())
            .entity(Integer.toString(reservationCount))
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

        List<Reservation> reservationList = reservationDao.getAll();

        return Response
            .status(getHttpStatus())
            .entity(reservationList)
            .build();
    }

    @POST
    @Path("save")
    @Produces(MediaType.TEXT_PLAIN)
    public Response saveReservation(
        @QueryParam("id") int reservationId,
        @Valid @BeanParam Reservation reservation,
        @CookieParam("token") String token
    ) {
        reservation.setId(reservationId);
        reservationDao.save(reservation);

        return Response
            .status(getHttpStatus())
            .build();
    }

    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteReservation (
        @QueryParam("id") int reservationId
    ) {
        reservationDao.delete(reservationId);

        return Response
            .status(getHttpStatus())
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
        @QueryParam("reservationId") int reservationId,
        @CookieParam("token") String token
    ) {
        Reservation reservation = reservationDao.getEntity(reservationId);

        return Response
            .status(getHttpStatus())
            .entity(reservation)
            .build();
    }
}
