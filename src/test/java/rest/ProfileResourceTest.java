package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ProfileDTO;
import entities.Profile;
import entities.RenameMe;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class ProfileResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Profile p1, p2, p3;
    private static RenameMe r1;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Profile("Anna", "Andersen", "aa@mail.com");
        p2 = new Profile("Bo", "Berthelsen", "bb@mail.com");
        r1 = new RenameMe("First", "First");
        p2.addRenameMe(r1);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Profile.deleteAllRows").executeUpdate();
            em.createNamedQuery("RenameMe.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(r1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/profile").then().statusCode(200);
    }

    @Test
    void getAll() {
        List<ProfileDTO> profileDTOS;

        profileDTOS = given()
                .contentType("application/json")
                .when()
                .get("/profile")
                .then()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .extract().body().jsonPath().getList("", ProfileDTO.class);


        assertEquals(profileDTOS.size(), 2);
    }

    @Test
    void getById() {
        given()
                .contentType("application/json")
                .get("/profile/{id}", p1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo("Anna"))
                .body("lastName", equalTo("Andersen"))
                .body("email", equalTo("aa@mail.com"));
    }

    @Test
    public void testFailByID() {
        System.out.println("================================================================");
        System.out.println("OBS: Test failing on purpose, when finding entity by invalid ID: ");
        System.out.println("================================================================");

        given()
                .contentType("application/json")
                .get("/profile/99999")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("message", equalTo("The Profile entity with ID: 99999 Was not found"));
    }


    @Test
    void create() {
        p3 = new Profile("Charlie", "Cameron", "cc@mail.com");
        String requestBody = GSON.toJson(new ProfileDTO(p3));

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/profile")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo("Charlie"))
                .body("lastName", equalTo("Cameron"))
                .body("email", equalTo("cc@mail.com"));
    }

    @Test
    void update() {
        ProfileDTO profileDTO = new ProfileDTO(p1);
        profileDTO.setFirstName("Lone");
        String requestBody = GSON.toJson(profileDTO);

        given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .put("/profile/"+p1.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(p1.getId()))
                .body("firstName", equalTo("Lone"));
    }

    @Test
    void delete() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", p1.getId())
                .delete("/profile/{id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(p1.getId()));
    }

    @Test
    void addRelation() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", p1.getId())
                .put("/profile/addrenameme/{id}/1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(p1.getId()))
                .body("renameMeDTOS", hasItems(hasEntry("id",1)));

    }

    @Test
    void removeRelation() {
        given()
                .header("Content-type", ContentType.JSON)
                .pathParam("id", p2.getId()).pathParam("item_id", r1.getId())
                .delete("/profile/removerenameme/{id}/{item_id}")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(p2.getId()))
                .body("renameMeDTOS", empty());
    }

    @Test
    public void getCount() throws Exception {
        given()
                .contentType("application/json")
                .get("/profile/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(2));
    }
}