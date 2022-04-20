package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtoFacades.ProfileDTOFacade;
import dtos.ProfileDTO;
import entities.Profile;
import errorhandling.EntityNotFoundException;
import facades.IFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
public class UserResources {

    private static final UserFacade FACADE = UserFacade.getUserFacade(EMF_Creator.createEntityManagerFactory());
    private static final IFacade<ProfileDTO> profileDTOIFacade =  ProfileDTOFacade.getFacade();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

/*
    @GET
    @Path("/role/{username}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUserRole(@PathParam("username") String username) throws EntityNotFoundException {
        String role = FACADE.getById(id);
        return Response.ok().entity(GSON.toJson(pdto)).build();
    }


 */

    //TODO: NOT DONE YET
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        ProfileDTO pdto = GSON.fromJson(content, ProfileDTO.class);
        ProfileDTO newPdto = profileDTOIFacade.create(pdto);
        return Response.ok().entity(GSON.toJson(newPdto)).build();
    }


}
