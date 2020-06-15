package com.org.covid_sci_discoveries;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AppTeste {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getPDFNamesTest() {
		assertEquals(App.getPDFNames("documents").toString(),
				"[D:\\Projetos\\Eclipse Workspace\\covid-sci-discoveries\\documents\\teste.pdf]");
	}

	@Test
	public void getMetadataTest() {
		assertTrue(App.getMetadata("documents").contains("href=\"http://localhost:8000/wp-content/uploads/simple-file-list/D:\\Projetos\\Eclipse Workspace\\covid-sci-discoveries\\documents\\teste.pdf\""));
	}

}
