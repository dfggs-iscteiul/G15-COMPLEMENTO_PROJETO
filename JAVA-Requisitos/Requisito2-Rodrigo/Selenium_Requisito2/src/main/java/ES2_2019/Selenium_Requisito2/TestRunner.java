package ES2_2019.Selenium_Requisito2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.runner.JUnitCore;

public class TestRunner {

	public static void main(String[] args) {
		while (true) {
			JUnitCore.runClasses(testes.class);
			Boolean[] testResults = testes.getTestesFalhados();
			String[] testNames = testes.getNomesTestes();
			String pagHTML = array2HTML(testNames, testResults);
			createHTMLFile(pagHTML);
			try {
				TimeUnit.HOURS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static String array2HTML(String[] testNames, Boolean[] resultados){
		StringBuilder html = new StringBuilder("<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"<head>\r\n" + 
				"<meta charset=\"ISO-8859-1\">\r\n" + 
				"<title>Resultados dos testes Selenium</title>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n"  +
				"<table>\r\n");

		html.append("<th>" + "Teste" + "</th>\r\n");
		html.append("<th>" + "Resultado" + "</th>\r\n");
		String resultadoTeste;
		for(int i = 0; i < testNames.length; i++){
			if (!resultados[i]) {
				resultadoTeste = "Sucesso";
			} else {
				resultadoTeste = "Falhou"; 
			}
			html.append("<tr>");
			html.append("<td>" + testNames[i] + "</td>\r\n");
			html.append("<td>" + resultadoTeste + "</td>\r\n");
			html.append("</tr>");
		}
		html.append("</table>\r\n" + 
				"</body>\r\n" + 
				"</html>");
		return html.toString();

	}


	public static void createHTMLFile(String pagHTML) {
		String Path = "../../../wp-content/html/EstatisticasSelenium.html";
		try {
			File myObj = new File(Path);
			if (myObj.createNewFile()) {
				 writeToHTMLFile(pagHTML, Path);
			} else {
				myObj.delete();
				writeToHTMLFile(pagHTML, Path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void writeToHTMLFile(String pagHTML, String Path) {
		try {
		      FileWriter myWriter = new FileWriter(Path);
		      myWriter.write(pagHTML);
		      myWriter.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
	}
	
}
