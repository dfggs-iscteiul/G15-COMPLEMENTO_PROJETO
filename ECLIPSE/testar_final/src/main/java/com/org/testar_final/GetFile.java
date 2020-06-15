package com.org.testar_final;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.w3c.dom.Document;

public class GetFile {

    private  Git git;

    public void cloneAndCopyFiles() throws org.xml.sax.SAXException {

        try {
        	File folder = new File("ESII1920");
        	deleteFolder(folder);
           git = Git.cloneRepository().setURI("https://github.com/vbasto-iscte/ESII1920").call();
        } catch (GitAPIException e) {

            e.printStackTrace();

        }



        File ficheiro = new File("ESII1920/covid19spreading.rdf");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;

        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(ficheiro);
            doc.getDocumentElement().normalize();

        } catch (ParserConfigurationException | IOException e1) {
            e1.printStackTrace();
        }
    }
    
    public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { 
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}

    public static void main(String[] args) {

        try {
			new GetFile().cloneAndCopyFiles();
		} catch (org.xml.sax.SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}