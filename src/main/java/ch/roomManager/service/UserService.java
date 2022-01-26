package ch.roomManager.service;

import ch.roomManager.dao.UserDao;
import ch.roomManager.models.User;
import ch.roomManager.util.TokenHandler;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * service controller for authentication
 * <p>
 * M151: BookDB
 *
 * @author Marcel Suter
 */
@Path("user")
public class UserService {

    /**
     * authenticate the user with username/password
     *
     * @param username the username
     * @param password the password
     * @return empty String
     */
    @POST
    @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(
            @FormParam("username") String username,
            @FormParam("password") String password
    ) {

        UserDao userDao = new UserDao();
        User user = userDao.getEntity(username, password);

        Map<String,String> claimMap = new HashMap<>();

        claimMap.put("userName", user.getUsername());

        NewCookie tokenCookie = new TokenHandler().buildCookie(claimMap);
        return Response
                .ok()
                .cookie(tokenCookie)
                .build();
    }

    /**
     * logoff the user and destroy the session
     *
     * @return Response
     */
    @GET
    @Path("logoff")
    @Produces(MediaType.TEXT_PLAIN)
    public Response logoff(
    ) {
        int httpStatus = 200;
        NewCookie tokenCookie = new TokenHandler().buildCookie(null);
        return Response
                .status(httpStatus)
                .entity("")
                .cookie(tokenCookie)
                .build();
    }

    /**
     * returns the username and role of the authenticated user
     *
     * @param token cookie with the jwtoken
     * @return map as json
     */
    @GET
    @Path("user")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authUser(
            @CookieParam("token") String token
    ) {
        int httpStatus = 403;

        Map<String, String> userData = new HashMap<>();

        if (token != null) {
            Map<String, String> claimMap = new TokenHandler().readClaims(token);
            if (claimMap != null && !claimMap.isEmpty()) {
                userData.put("userName", claimMap.get("userName"));
                userData.put("userRole", claimMap.get("userRole"));
            }
        }

        return Response
                .status(httpStatus)
                .entity(userData)
                .build();
    }

}
