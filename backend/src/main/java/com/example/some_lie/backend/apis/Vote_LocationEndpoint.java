package com.example.some_lie.backend.apis;

import com.example.some_lie.backend.models.Vote_Location;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "d",
        version = "v1",
        resource = "vote_Location",
        namespace = @ApiNamespace(
                ownerDomain = "models.backend.some_lie.example.com",
                ownerName = "models.backend.some_lie.example.com",
                packagePath = ""
        )
)
public class Vote_LocationEndpoint {

    private static final Logger logger = Logger.getLogger(Vote_LocationEndpoint.class.getName());

    /**
     * This method gets the <code>Vote_Location</code> object associated with the specified <code>id</code>.
     *
     * @param id The id of the object to be returned.
     * @return The <code>Vote_Location</code> associated with <code>id</code>.
     */
    @ApiMethod(name = "getVote_Location")
    public Vote_Location getVote_Location(@Named("id") Long id) {
        // TODO: Implement this function
        logger.info("Calling getVote_Location method");
        return null;
    }

    /**
     * This inserts a new <code>Vote_Location</code> object.
     *
     * @param vote_Location The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertVote_Location")
    public Vote_Location insertVote_Location(Vote_Location vote_Location) {
        // TODO: Implement this function
        logger.info("Calling insertVote_Location method");
        return vote_Location;
    }
}