package QATest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SiteTest {
	private WebDriver driver;
	
	@Before
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}
	
	@Test
	public void testLinkEmpleo() {
		driver.get("https://www.choucairtesting.com/");
		
		driver.findElement(By.id("menu-item-550"))
												 .findElement(By.cssSelector("a"))
												 .click();
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		String url = driver.getCurrentUrl();
		
		assertTrue(url.contains("empleos-2"));
	}	
	
	@Test
	public void testEmpleosDisponibles() {
		driver.get("https://www.choucairtesting.com/empleos-2/");
		
		WebElement empleo = driver.findElement(By.name("search_keywords"));
		
		empleo.clear();
		empleo.sendKeys("Programacion", Keys.ENTER);

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		assertEquals("Búsqueda completada. Encontrados 4 registros.", 
			     driver.findElement(By.cssSelector(".showing_jobs.wp-job-manager-showing-all"))
			     	   .findElement(By.cssSelector("span")).getText());
	}
	
	@Test
	public void testEmpleoEnPanama() {
		driver.get("https://www.choucairtesting.com/empleos-2/");
		
		WebElement empleo = driver.findElement(By.name("search_keywords"));
		String textoUbicacion = "";
		
		empleo.clear();
		empleo.sendKeys("Programacion", Keys.ENTER);
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		WebElement contenedor = driver.findElement(By.cssSelector("ul.job_listings"));
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		List<WebElement> lista = contenedor.findElements(By.cssSelector("li:nth-child(n)"));
	    for (WebElement we : lista){
	    	List<WebElement> tagsA;
	    	driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	    	try
	    	{
		    	tagsA = we.findElements(By.cssSelector("a:nth-child(n)"));
		    	if (tagsA.size() > 0) {
		    		WebElement tagDiv = tagsA.get(0).findElement(By.cssSelector(".location"));
		    		textoUbicacion = tagDiv.getText();
		    		if (textoUbicacion.contains("Panamá"))
		    			break;
		    	}
	    	}
	    	catch(StaleElementReferenceException ex)
	    	{
	    		tagsA = we.findElements(By.cssSelector("a:nth-child(n)"));
		    	if (tagsA.size() > 0) {
		    		WebElement tagDiv = tagsA.get(0).findElement(By.cssSelector(".location"));
		    		textoUbicacion = tagDiv.getText();
		    		if (textoUbicacion.contains("Panamá"))
		    			break;		    		
		    	}
	    	}
	      }
	    assertTrue(textoUbicacion.contains("Panamá"));
	}
	
	@Test
	public void testLinkPrincipiosdeprogramacion() {
		driver.get("https://www.choucairtesting.com/empleos-2/");
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		WebElement tagSection = driver.findElement(By.cssSelector("section.has_eae_slider.elementor-element.elementor-element-7069c5e.elementor-section-boxed.elementor-section-height-default.elementor-section-height-default.elementor-section.elementor-top-section"));
		WebElement tagDiv1 = tagSection.findElement(By.cssSelector("div.elementor-container.elementor-column-gap-default div.elementor-row"));
		WebElement tagDiv2 = tagDiv1.findElement(By.cssSelector("div.has_eae_slider.elementor-element.elementor-element-ef9e957.elementor-column.elementor-col-50.elementor-top-column"));		
		WebElement tagDiv3 = tagDiv2.findElement(By.cssSelector("div div div div div ul.blue-dot-list"));		
		WebElement tagDiv4 = tagDiv3.findElement(By.cssSelector("li:nth-child(3)"));
		WebElement tagDiv5 = tagDiv4.findElement(By.cssSelector("a"));
		try
		{
			List<WebElement> tagLinkCookies = driver.findElements(By.cssSelector("#cookie-law-info-bar span a#cookie_action_close_header"));
			if (tagLinkCookies.get(0).isDisplayed()) {
				tagLinkCookies.get(0).click();
			}
	
			WebDriverWait wait = new WebDriverWait(driver, 3);
			boolean invisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("#cookie-law-info-bar span a#cookie_action_close_header")));
			if (invisible) {
				tagDiv5.click();
			}
				
			Thread.sleep(5000);
			Set<String> prevWindowHandles = driver.getWindowHandles();
			String titulo = "";
		    for(String prevHandle : prevWindowHandles){
		    	driver.switchTo().window(prevHandle);
		    	titulo = driver.getTitle();
		    	if (titulo.contains("Curso de programación desde cero")) {
		    		break;
		    	}
		    }
			assertEquals("Curso de programación desde cero | Principio básico de programación #1 - YouTube", titulo);
		}
		catch (InterruptedException ex)
		{
			//No hay necesidad de capturar el error
		}
	}
	
	@After
	public void tearDown() {
		driver.quit();
	}
}
