package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtoFacades.ProfileDTOFacade;
import dtoFacades.RenameMeDTOFacade;
import dtos.ProfileDTO;
import dtos.RenameMeDTO;
import errorhandling.EntityNotFoundException;
import facades.IFacade;
import utils.EMF_Creator;
import facades.FacadeExample;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

//Todo Remove or change relevant parts before ACTUAL use
@Path("xxx")
public class RenameMeResource {

    private static final IFacade<RenameMeDTO> FACADE =  RenameMeDTOFacade.getFacade();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Path("demo")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }


    @Path("data")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getExampleData() throws IOException {
        return RenameMeDTOFacade.getExampleData();
    }


    //TODO: Change these

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(FACADE.getAll())).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") int id) throws EntityNotFoundException {
        RenameMeDTO rdto = FACADE.getById(id);
        return Response.ok().entity(GSON.toJson(rdto)).build();
    }
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        RenameMeDTO rdto = GSON.fromJson(content, RenameMeDTO.class);
        RenameMeDTO newRdto = FACADE.create(rdto);
        return Response.ok().entity(GSON.toJson(newRdto)).build();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") int id, String content) throws EntityNotFoundException {
        RenameMeDTO rdto = GSON.fromJson(content, RenameMeDTO.class);
        rdto.setId(id);
        RenameMeDTO updated = FACADE.update(rdto);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") int id) throws EntityNotFoundException {
        RenameMeDTO deleted = FACADE.delete(id);
        return Response.ok().entity(GSON.toJson(deleted)).build();
    }

}