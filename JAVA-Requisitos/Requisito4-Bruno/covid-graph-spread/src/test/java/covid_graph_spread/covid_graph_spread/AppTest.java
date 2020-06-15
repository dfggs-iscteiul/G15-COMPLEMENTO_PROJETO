package covid_graph_spread.covid_graph_spread;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Repository;
import org.junit.Before;
import org.junit.Test;

public class AppTest {

	App app;
	
	@Before
	public void setUp() throws Exception {
		
		app = new App();
		app.createTable();
	}
	
	
	//Ao contrário da app, o apptest está a gerar uma pasta no local onde as pastas existentes deviam ser apagadas e tal está
	//a impossibilitar a realização dos testes


	@Test
	public void testDeleteFolder() throws FileNotFoundException {
		
		File file = new File("../files");
		app.deleteFolder(file);
		assertEquals(null, file);
	}

	@Test
	public void testGetSize() {
		assertEquals(3, app.getSize());
	}

	@Test
	public void testGetTable() {
		
		assertEquals("2020-05-26 12:52:32", app.getTable()[0][0]);
		assertEquals("TestesGeracaoIII", app.getTable()[2][2]);
	}

	@Test
	public void testGetRep() throws InvalidRemoteException, TransportException, GitAPIException {

		Repository r = app.getGit().getRepository();
		
		Git git = Git.cloneRepository()
				.setURI("https://github.com/vbasto-iscte/ESII1920")
				.setDirectory(new File("../files")) 
				.call();

		Repository repository = git.getRepository();
		
		assertTrue(r.equals(repository));
	}


}
