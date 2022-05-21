package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dtoFacades.ProfileDTOFacade;
import dtos.ProfileDTO;
import errorhandling.EntityNotFoundException;
import facades.IFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("profile")
public class ProfileResource {

    private static final IFacade<ProfileDTO> FACADE =  ProfileDTOFacade.getFacade();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(FACADE.getAll())).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") int id) throws EntityNotFoundException {
        ProfileDTO pdto = FACADE.getById(id);
        return Response.ok().entity(GSON.toJson(pdto)).build();
    }
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        ProfileDTO pdto = GSON.fromJson(content, ProfileDTO.class);
        ProfileDTO newPdto = FACADE.create(pdto);
        return Response.ok().entity(GSON.toJson(newPdto)).build();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") int id, String content) throws EntityNotFoundException {
        ProfileDTO pdto = GSON.fromJson(content, ProfileDTO.class);
        pdto.setId(id);
        ProfileDTO updated = FACADE.update(pdto);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") int id) throws EntityNotFoundException {
        ProfileDTO deleted = FACADE.delete(id);
        return Response.ok().entity(GSON.toJson(deleted)).build();
    }

    @PUT
    @Path("/add/{profileid}/{entityid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response addRelation(@PathParam("profileid") int id1, @PathParam("entityid") int id2) throws EntityNotFoundException {
        ProfileDTO updated = FACADE.addRelation(id1, id2);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("/remove/{profileid}/{entityid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response removeRelation(@PathParam("profileid") int id1, @PathParam("entityid") int id2) throws EntityNotFoundException {
        ProfileDTO updated = FACADE.removeRelation(id1, id2);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @GET
    @Path("count")
    @Produces({MediaType.APPLICATION_JSON})
    public String getCount()  {
        long count = FACADE.getCount();
        return "{\"count\":"+count+"}";  //Done manually so no need for a DTO
    }
}