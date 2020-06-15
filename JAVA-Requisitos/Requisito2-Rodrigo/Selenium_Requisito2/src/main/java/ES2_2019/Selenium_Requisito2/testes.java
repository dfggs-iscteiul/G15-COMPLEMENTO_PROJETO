package ES2_2019.Selenium_Requisito2;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import com.sun.mail.smtp.SMTPTransport;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class testes {

	// WebDriver instance
	private static WebDriver driver;
	private WebElement element;
	
	
	//email
	private static final String SMTP_SERVER = "smtp.gmail.com";
	private static final String USERNAME = "rmmsses2@gmail.com";
	private static final String PASSWORD = "password123.";

	private static final String EMAIL_FROM = "rmmsses2@gmail.com";
	private static final String EMAIL_TO = "covid19grupo15iscte@gmail.com";
	private static final String EMAIL_TO_CC = "";

	private static final String EMAIL_SUBJECT = "Indisponibilidade Website WordPress";
	private static Properties prop = System.getProperties();
	private static Session session = Session.getInstance(prop, null);
	private static Message msg = new MimeMessage(session);
	
	//Vetores com os resultados dos testes e com os nomes dos testes
	private static Boolean[] testesFalhados;
	private static String[] nomesTestes;
	
	
	/**
	 * Método getter do atributo testesFalhados
	 * @return Vetor de String com todos os testes falhados
	 */
	public static Boolean[] getTestesFalhados() {
		return testesFalhados;
	}
	
	
	/**
	 * Método setter do atributo testesFalhado
	 * @param newVector Vetor que o atributo vai tomar
	 */
	public static void setTestesFalhados(Boolean[] newVector) {
		testesFalhados= newVector;
	}
	
	
	/**
	 * Método getter do atributo nomesTestes
	 * @return Vetor de String com todos os testes falhados
	 */
	public static String[] getNomesTestes() {
		return nomesTestes;
	}
	
	
	/**
	 * Método setter do atributo nomesTestes
	 * @param newVector Vetor que o atributo vai tomar
	 */
	public static void setNomesTestes(String[] newVector) {
		nomesTestes = newVector;
	}
	
	
	/**
	 * Metódo que corre antes dos testes
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// location of drivers
		System.setProperty("webdriver.chrome.driver", "..\\chromedriver.exe");
		// create a new instance of the driver
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("localhost:8000");
		//email
        prop.put("mail.smtp.host", SMTP_SERVER); //optional, defined in SMTPTransport
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "465"); // default port 25
        prop.put("mail.smtp.ssl.enable", "true");
        testesFalhados = new Boolean[19]; //tamanho do vetor é o número de testes definidos na classe
        for (int i=0; i<19; i++) {
        	testesFalhados[i]=false;
        }
        populateNomesTestes();
        
	}
	
	
	public static void populateNomesTestes() {
		nomesTestes = new String[19];
		nomesTestes[0] = "TestOpenWebsite";
		nomesTestes[1] = "TestSiteTitle";
		nomesTestes[2] = "TestHome";
		nomesTestes[3] = "TestCovSciDis";
		nomesTestes[4] = "TestCovSpr";
		nomesTestes[5] = "TestCovQue";
		nomesTestes[6] = "TestCovEvo";
		nomesTestes[7] = "TestCovWiki";
		nomesTestes[8] = "TestCovFAQ";
		nomesTestes[9] = "TestCovFAQSearch";
		nomesTestes[10] = "TestContact";
		nomesTestes[11] = "TestContactForm";
		nomesTestes[12] = "TestLogin";
		nomesTestes[13] = "TestSubmitLogin";
		nomesTestes[14] = "TestJoin";
		nomesTestes[15] = "TestJoinForm";
		nomesTestes[16] = "TestAbout";
		nomesTestes[17] = "TestRepository";
		nomesTestes[18] = "TestAnalytics";
		
	}
	

	/**
	 * Metódo que corre depois dos testes
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		driver.close();   // close the tab it has opened
		driver.quit();    // close the browser
	}

	
	/**
	 * Metódo que é chamado para se enviar email em caso de falha de um teste
	 * @param text Conteúdo do email
	 */
	public static void sendEmail(String text) {
		try {
            msg.setFrom(new InternetAddress(EMAIL_FROM));
            msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(EMAIL_TO, false));
            msg.setRecipients(Message.RecipientType.CC,InternetAddress.parse(EMAIL_TO_CC, false));
            msg.setSubject(EMAIL_SUBJECT);
            msg.setText(text);
            msg.setSentDate(new Date());

			// Get SMTPTransport
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");

			// connect
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);

			// send
            t.sendMessage(msg, msg.getAllRecipients());

            System.out.println("Response: " + t.getLastServerResponse());

            t.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }

	}
	
	
	/**
	 * Teste para ver se existe a homepage do site
	 */
	@Test
	public void testOpenWebsite() {
		driver.get("localhost:8000"); //connect to localhost:8000 again because of order that tests run, it's not guaranteed that this test runs on the website's homepage
		if (!"Covid-19 by ISCTE-IUL – ESII – G15 – 2019/2020".equals(driver.getTitle())) {
			sendEmail("Homepage do website indisponivel");
			testesFalhados[0]=true;
		}
	}
	
	
	/**
	 * Teste para ver se existe o botão "COVID-19 BY ISCTE-IUL"
	 */
	@Test
	public void testSiteTitle() {
		try {
		element = driver.findElement(By.cssSelector("#masthead > div.custom-header > div.site-branding > div > div > p.site-title")); //Site title link at top of page
		element.click();
		} catch(NoSuchElementException e) {
			sendEmail("Botão Titulo do Website não encontrado");
			testesFalhados[1]=true;
		}
	}


	/**
	 * Teste para ver se existe o botão "Home"
	 */
	@Test
	public void testHome() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-73")); //Home button 
		element.click();
		} catch (NoSuchElementException e) {
			sendEmail("Botão Home do Website não encontrado");
			testesFalhados[2]=true;
		};
	}

	
	/**
	 * Teste para ver se existe o botão "Covid Scientific Discoveries"
	 */
	@Test
	public void testCovSciDis() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-66")); //Covid Scientific Discoveries button 
		element.click();
		} catch(NoSuchElementException e) {
			sendEmail("Botão Covid Scientific Discoveries não encontrado");
			testesFalhados[3]=true;
		}
	}
	
	
	/**
	 * Teste para ver se existe o botão "Covid Spread"
	 */
	@Test
	public void testCovSpr() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-65")); //Covid Spread button 
		element.click();
		} catch(NoSuchElementException e) {
			sendEmail("Botão Covid Spread não encontrado");
			testesFalhados[4]=true;
		}
	}
	
	
	/**
	 * Teste para ver se existe o botão "Covid Queries"
	 */
	@Test
	public void testCovQue() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-72")); //Covid Queries button 
		element.click();
		} catch (NoSuchElementException e) {
			sendEmail("Botão Covid Queries não encontrado");
			testesFalhados[5]=true;
		}
	}
	
	
	/**
	 * Teste para ver se existe o botão "Covid Evolution"
	 */
	@Test
	public void testCovEvo() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-64")); //Covid Evolution button 
		element.click();
		} catch (NoSuchElementException e) {
			sendEmail("Botão Covid Evolution não encontrado");
			testesFalhados[6]=true;
		}
	}
	
	
	/**
	 * Teste para ver se existe o botão "Covid Wiki"
	 */
	@Test
	public void testCovWiki() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-179")); //Covid Wiki button 
		element.click();
		} catch(NoSuchElementException e) {
			sendEmail("Botão Wiki não encontrado");
			testesFalhados[7]=true;
		}
	}

	/**
	 * Teste para ver se existe o botão "FAQ"
	 */
	@Test
	public void testCovFAQ() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-100")); //Covid Faq button 
		element.click();
		} catch(NoSuchElementException e) {
			sendEmail("Botão FAQ não encontrado");
			testesFalhados[8]=true;
		}
	}
	
	
	/**
	 * Teste para ver se a função de Search da página FAQ está a funcionar
	 */
	@Test
	public void testCovFAQSearch(){
		try {
		element = driver.findElement(By.cssSelector("#menu-item-100")); //Covid Faq button 
		element.click();
		element = driver.findElement(By.cssSelector("#post-90 > div > section > form > input"));
		element.sendKeys("all");
		} catch(NoSuchElementException e) {
			sendEmail("Função Search não disponivel na página FAQ");
			testesFalhados[9]=true;
		}
	}
	
	
	/**
	 * Teste para ver se existe o botão "Contact Us"
	 */
	@Test
	public void testContact() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-61")); //Covid Contact button 
		element.click();
		} catch(NoSuchElementException e) {
			sendEmail("Botão Contact Us não encontrado");
			testesFalhados[10]=true;
		}
	}
	
	
	/**
	 * Teste para ver se o formulário de contacto tem os campos corretos
	 */
	@Test
	public void testContactForm() {
		try {
		driver.get("localhost:8000");
		element = driver.findElement(By.cssSelector("#menu-item-61")); //Covid Contact button 
		element.click();
		element = driver.findElement(By.cssSelector("#nf-field-1"));
		element.sendKeys("Manel");
		element = driver.findElement(By.cssSelector("#nf-field-2"));
		element.sendKeys("Manel@Manel.pt");
		element = driver.findElement(By.cssSelector("#nf-field-5"));
		element.sendKeys("Covid 19");
		element = driver.findElement(By.cssSelector("#nf-field-3"));
		element.sendKeys("Mensagem de teste");
		element.submit();
		} catch(NoSuchElementException e) {
			sendEmail("Erro ao preencher o formulário para entrar em contacto");
			testesFalhados[11]=true;
		}
	}
	
	
	/**
	 * Teste para ver se existe o botão "Login"
	 */
	@Test
	public void testLogin() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-143")); //login button 
		element.click();
		} catch (NoSuchElementException e) {
			sendEmail("Botão Log In não encontrado");
			testesFalhados[12]=true;
		}
		
	}
	
	
	/**
	 * Teste para ver se o formulário de log in tem os campos corretos
	 */
	@Test
	public void testSubmitLogin() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-143")); //login button 
		element.click();
		
		element = driver.findElement(By.cssSelector("#username-6")); //username field
		element.sendKeys("Administrator");
		
		element = driver.findElement(By.cssSelector("#user_password-6")); //password field
		element.sendKeys("Administrator");
		
		element.submit();
		} catch (NoSuchElementException e) {
			sendEmail("Erro ao preencher o formulário de log in");
			testesFalhados[13]=true;
		}
	}
	
	
	/**
	 * Teste para ver se existe o botão "Join Us"
	 */
	@Test
	public void testJoin() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-67")); //Covid Join button 
		element.click();
		} catch(NoSuchElementException e) {
			sendEmail("Botão Join us não encontrado");
			testesFalhados[14]=true;
		}
	}
	
	
	/**
	 * Teste para ver se o formulário de registo tem os campos corretos
	 */
	@Test
	public void testJoinForm() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-67")); //Covid Join button 
		element.click();
		element = driver.findElement(By.cssSelector("#first_name-16"));
		element.sendKeys("Manel");
		element = driver.findElement(By.cssSelector("#last_name-16"));
		element.sendKeys("Jose");
		element = driver.findElement(By.cssSelector("#affiliation-16"));
		element.sendKeys("Ladroes");
		element = driver.findElement(By.cssSelector("#countrysignup-16"));
		element.sendKeys("Nigeria");
		element = driver.findElement(By.cssSelector("#keywords"));
		element.sendKeys("; Roubar");
		element = driver.findElement(By.cssSelector("#username-16"));
		element.sendKeys("ManelRoubaTodos");
		element = driver.findElement(By.cssSelector("#user_password-16"));
		element.sendKeys("Ladraozinho123.");
		element = driver.findElement(By.cssSelector("#confirm_user_password-16"));
		element.sendKeys("Ladraozinho123.");
		element = driver.findElement(By.cssSelector("#um-admin-form-shortcode > div > div > div > div > form > div.um-field.um-field-type_terms_conditions > div:nth-child(2) > label > span.um-field-checkbox-state"));
		element.click();
		element.submit();
		} catch (NoSuchElementException e) {
			sendEmail("Erro ao preencher o formulário de registo");
			testesFalhados[15]=true;
		}
		
	}
	
	
	/**
	 * Teste para ver se existe o botão "About Us"
	 */
	@Test
	public void testAbout() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-60")); //Covid About button 
		element.click();
		assertEquals("About Us – Covid-19 by ISCTE-IUL",driver.getTitle());
		} catch(NoSuchElementException e) {
			sendEmail("Botão About Us não encontrado");
			testesFalhados[16]=true;
		} 
	}
	
	
	/**
	 * Teste para ver se existe o botão "Covid Scientific Discoveries Repository"
	 */
	@Test
	public void ztestRepository() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-59")); //Covid Repository button 
		element.click();;
		} catch (NoSuchElementException e) {
			sendEmail("Botão Covid Scientific Discoveries Repository não encontrado");
			testesFalhados[17]=true;
		}
	}
	
	
	/**
	 * Teste para ver se existe o botão "Web Site Analytics"
	 */
	@Test
	public void ztestAnalytics() {
		try {
		element = driver.findElement(By.cssSelector("#menu-item-58")); //Covid Analytics button 
		element.click();
		} catch(NoSuchElementException e) {
			sendEmail("Botão Web Site Analytics não encontrado");
			testesFalhados[18]=true;
		} 
	}
	
	
}
