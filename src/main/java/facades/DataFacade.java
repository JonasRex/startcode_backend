package facades;

import com.nimbusds.jose.shaded.json.JSONObject;
import entities.Profile;
import utils.Utility;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;

public class DataFacade {
    private static DataFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private DataFacade() {
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static DataFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DataFacade();

        }
        return instance;
    }


    public String getRandomQuote() throws IOException {
        return Utility.fetchData("https://zenquotes.io/api/random");
    }

    public static void main(String[] args) {
        // Use this to test your data fetching.
        try {
            System.out.println(Utility.fetchData("https://zenquotes.io/api/random"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
