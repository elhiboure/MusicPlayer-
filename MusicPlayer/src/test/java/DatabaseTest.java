import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Database;

public class DatabaseTest {
	
	    Connection connection;
	    Database database = new Database();

	    @Before
	    public void before() throws Exception {
	    	database.connect();
	        connection = database.getConnection();
	    }

	    @After
	    public void after() {
	    	database.disconnect();
	    }

	    @Test
	    public void closeStatementShouldCloseStatement() throws SQLException {// pour generer une exception quand une requete sql en cas d'erreur 
	        Statement statement = connection.createStatement();//objet statement que j'insere dans une methode database 
	        database.closeStatement(statement);
	        assertTrue(statement.isClosed());// déconnexion echouée 
	    }

	    @Test
	    public void closeStatementWithNullShouldNotThrow()  {
	    	database.closeStatement(null);//ex quand je declare un statement mais jl'ai pas initialiser ou je lai envoyer un objet null 
	    }

}
