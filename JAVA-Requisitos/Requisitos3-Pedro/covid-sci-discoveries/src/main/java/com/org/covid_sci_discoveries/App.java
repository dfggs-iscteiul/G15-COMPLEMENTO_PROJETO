package com.org.covid_sci_discoveries;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cgi.lib.cgi_lib;
import pl.edu.icm.cermine.ContentExtractor;
import pl.edu.icm.cermine.exception.AnalysisException;

public class App {

	public static void main(String[] args) {
		System.out.println(cgi_lib.Header());
		String style = "<style>\r\n" + "table {\r\n" + "  font-family: arial, sans-serif;\r\n"
				+ "  border-collapse: collapse;\r\n" + "  width: 100%;\r\n" + "}\r\n" + "td, th {	\r\n"
				+ "  border: 1px solid #dddddd;\r\n" + "  text-align: center;\r\n" + "  padding: 8px;\r\n" + "\r\n"
				+ "}\r\n" + "th {\r\n" + "	min-width: 100px; \r\n color: white;\r\n" + "	background-color: #424040;\r\n" + "}"
				+ "</style>\r\n";
		System.out.println(style);
		System.out.println("<title>Articles</title>\r\n" + "</head>\r\n" + "<body>\r\n" + "<table>\r\n" + "  <tr>\r\n"
				+ "    <th>Article Title</th>\r\n" + "    <th>Journal Name</th>\r\n" + "    <th>Pub Data</th>\r\n"
				+ "    <th>Authors</th>\r\n" + "  </tr>\r\n");
		System.out.println(getMetadata("/var/www/html/wp-content/uploads/simple-file-list"));
		System.out.println("</table>\r\n" + "</body>\r\n" + "</html>");
		
	}

	/***
	 * Search all the names of the pdf files in a folder
	 * @param folderPath folder location to look up pdf file names
	 * @return pdf files names
	 */
	public static List<String> getPDFNames(String path) {
		List<String> pdfNames = new ArrayList<String>();
		File[] files = new File(path).listFiles();
		for (File file : files) {
			if (file.isFile() && file.getName().endsWith(".pdf")) {
				pdfNames.add(file.getAbsolutePath());
			}
		}
		return pdfNames;
	}

	/***
	 * Open all the pdf files and extract the metaData, and transform this into a table
	 * @return A string that is Table
	 */
	public static String getMetadata(String pdfPath) {
		String resultS = "<tr>\r\n";
		for (String pdfName : getPDFNames(pdfPath)) {
			try {
				ContentExtractor extractor = new ContentExtractor();
				InputStream inputStream = new FileInputStream(pdfName);
				extractor.setPDF(inputStream);
				Element result = extractor.getMetadataAsNLM();

				XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
				String xmlString = outputter.outputString(result);
//				System.out.println(xmlString);
				String[] aux = pdfName.split("/");
				DocumentBuilder newDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document doc = newDocumentBuilder.parse(new ByteArrayInputStream(xmlString.getBytes("UTF8")));
				doc.getDocumentElement().normalize();
				XPathFactory xpathFactory = XPathFactory.newInstance();
				XPath xpath = xpathFactory.newXPath();

				// article title
				String query = "//article-title";
				XPathExpression expr = xpath.compile(query);
				resultS = resultS + "<td>\r\n" + "<a href=\"http://localhost:8000/wp-content/uploads/simple-file-list/"
						+ aux[aux.length - 1] + "\"" + ">" + expr.evaluate(doc, XPathConstants.STRING).toString().trim()
						+ "</a>" + "\r\n" + "</td>\r\n";

				// journal name
				query = "//journal-title";
				expr = xpath.compile(query);
				resultS = resultS + "<td>\r\n" + expr.evaluate(doc, XPathConstants.STRING).toString().trim() + "\r\n"
						+ "</td>\r\n";

				// publication data
				query = "//pub-date";
				expr = xpath.compile(query);
				resultS = resultS + "<td>\r\n" + expr.evaluate(doc, XPathConstants.STRING).toString().trim() + "\r\n"
						+ "</td>\r\n";

				// authors
				query = "//contrib/string-name";
				expr = xpath.compile(query);
				NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
				resultS = resultS + "<td>\r\n";
				for (int i = 0; i < nl.getLength(); i++) {
					Node node = nl.item(i);
					resultS = resultS + "<p>" + node.getTextContent() + "</p>\r\n";
				}
				resultS = resultS + "</td>\r\n" + "</tr>\r\n";
			} catch (AnalysisException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		return resultS;
	}
}
