package facades;

import entities.Profile;
import entities.RenameMe;
import errorhandling.EntityNotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;



import static org.junit.jupiter.api.Assertions.*;

@Disabled
class ProfileFacadeTest {
    private static EntityManagerFactory emf;
    private static IFacade<Profile> facade;
    Profile p1,p2;
    RenameMe r1;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = ProfileFacade.getFacade(emf);


    }

    // Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    @AfterAll
    public static void tearDownClass() {
        emf.close();
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Profile.deleteAllRows").executeUpdate();
            em.createNamedQuery("RenameMe.deleteAllRows").executeUpdate();
            p1 = new Profile("Anna", "Andersen", "aa@mail.com");
            p2 = new Profile("Bo", "Berthelsen", "bb@mail.com");
            r1 = new RenameMe("First", "First");
            p2.addRenameMe(r1);

            em.persist(p1);
            em.persist(p2);
            em.persist(r1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }


    @Test
    void create() {
        Profile expected = new Profile("Henning", "Olsen", "ho@mail.com");
        Profile actual   = facade.create(expected);
        assertEquals(expected, actual);
    }

    @Test
    void getById() throws EntityNotFoundException {
        Profile expected = p1;
        Profile actual = facade.getById(p1.getId());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void getAll() {
        int actual = facade.getAll().size();
        assertEquals(2, actual);
    }

    @Test
    void update() throws EntityNotFoundException {
        p1.setFirstName("Lone");
        Profile expected = p2;
        Profile actual = facade.update(p2);
        assertEquals(expected.getFirstName(),actual.getFirstName());
    }

    @Test
    void delete() throws EntityNotFoundException {
        Profile p = facade.delete(p1.getId());
        int expected = 1;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
        assertEquals(p.getId(),p1.getId());
    }

    @Test
    void addRelation() throws EntityNotFoundException {
        p1.addRenameMe(r1);
        Profile p = facade.addRelation(p1.getId(), r1.getId());
        assertEquals(1, p1.getRenameMesList().size());
        assertEquals(p.getId(), p1.getId());
    }

    @Test
    void removeRelation() throws EntityNotFoundException {
        p2.removeRenameMe(r1);
        Profile p = facade.removeRelation(p2.getId(), r1.getId());
        assertEquals(0, p2.getRenameMesList().size());
        assertEquals(p.getId(), p2.getId());
    }

    @Test
    void getCount() {
    }
}