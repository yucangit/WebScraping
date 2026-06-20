package bist;


import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class WebScrapingHisseSenedi {

	public static void yahooApi() 
	{
		/*
			Oluşan hata : java.io.IOException: Server returned HTTP response code: 401 for URL: https://query1.finance.yahoo.com/v7/finance/quote?symbols=THYAO.IS
		*/
		/*
		try {
			
			// Kütüphanenin bağlantı kurarken kullandığı sistem özelliklerine User-Agent ekleyin
			System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");

            // Ticker for BIST 100									
            Stock bist = YahooFinance.get("THYAO.IS");
            
            System.out.println("Symbol: " + bist.getSymbol());
            System.out.println("Price: " + bist.getQuote().getPrice());
            System.out.println("Currency: " + bist.getCurrency());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
		System.out.println("");
	}
	
	public static void webScrapingWithJSoup() {
        // Örnek hedef URL (kullanmak istediğiniz finans sitesi)
        

        try {
        	
        	
        	//System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
        	//System.setProperty("https.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
        	
        	
        	String url = "https://tr.investing.com/equities";
            // Web sitesine bağlanma ve HTML'i çekme
            Document doc = Jsoup.connect(url)                    
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Connection", "keep-alive")
                    //.header("Referer", "https://google.com")
                    .header("Referer", "https://tr.investing.com/")
                    .timeout(10000)
                    .ignoreHttpErrors(true) 
                    .get();

            // İlgili fiyat verisinin CSS sınıfı veya ID'si (Örn: .valueClass)
            // Tarayıcınızda F12 ile inceleyip doğru elementi seçmeniz gerekir
            Element table = doc.select("table").first(); 
            
            
            
            /*
            if (!fiyatElementi.isEmpty()) {
                String fiyat = fiyatElementi.text();
                System.out.println("Hisse Güncel Fiyatı: " + fiyat);
            } else {
                System.out.println("Fiyat elementi bulunamadı, CSS seçici güncellenmeli.");
            }
            */
            System.out.println(doc.html());
            
            System.out.println(doc.title());

        } catch (Exception e) {
        	e.printStackTrace();
            //System.err.println("Bağlantı hatası: " + e.getMessage());
        }
    }
	
    public static void webScrapingWithSelenium() {
        // 1. Configure Chrome options for scraping
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Run background process without UI
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                
    	// 2. Initialize the WebDriver
        WebDriver driver = new ChromeDriver(options);
        
        try {
            // 3. Navigate to target URL
        	String url = "https://tr.investing.com/equities";
        	

            driver.get(url);
            
            Thread.sleep(5000);
            
            System.out.println( driver.getTitle() );   //"Bir dakika lütfen..." çıktısı oluşuyor.
                        

            // 4. Implement Explicit Wait for dynamic JS content to render
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            List<WebElement> namesElements = driver.findElements(By.cssSelector("tbody>tr>td:nth-child(1)"));
            System.out.println("size of names elements : " + namesElements.size());
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("datatable-v2_body__8TXQk")));

            // 5. Extract multiple elements matching a DOM locator
            List<WebElement> products = driver.findElements(By.className("product-card"));

            System.out.println("--- Scraped Data Results ---");
            for (WebElement product : products) {
                // Find sub-elements within the parent container
                String title = product.findElement(By.className("product-title")).getText();
                String price = product.findElement(By.className("product-price")).getText();
                String link = product.findElement(By.tagName("a")).getAttribute("href");

                // Print the extracted structured data
                System.out.printf("Item: %s | Price: %s | Link: %s%n", title, price, link);
            }

        } 
        catch (Exception e) 
        {
            //System.err.println("An error occurred during extraction: " + e.getMessage());
            e.printStackTrace();
        } 
        finally 
        {
            // 6. Ensure browser session terminates to free memory
            driver.quit();
        }
        
        System.out.println("asdf");
    }
	
	
    public static void webScrapingWithSelenium2() {
    	
    	//Calisiyor(15.06.2026)
    	
        // 1. Configure Chrome options for scraping
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Run background process without UI
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                
    	// 2. Initialize the WebDriver
        WebDriver driver = new ChromeDriver(options);
        
        try {
            // 3. Navigate to target URL
        	String url = "https://datatables.net/examples/basic_init/zero_configuration.html";
        	

            driver.get(url);
            
            Thread.sleep(5000);
            
            System.out.println( driver.getTitle() );   //"Bir dakika lütfen..." çıktısı oluşuyor.
                        

            // 4. Implement Explicit Wait for dynamic JS content to render
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            List<WebElement> namesElements = driver.findElements(By.cssSelector("tbody>tr>td:nth-child(1)"));
            List<WebElement> namesElements2 = driver.findElements(By.cssSelector("tbody>tr>td:nth-child(1)"));
            System.out.println("size of names elements : " + namesElements.size());
                        
            List<String> names = new ArrayList<String>();
            
            //Adding column1 elements to the list
            for (WebElement nameEle : namesElements) {
            	names.add(nameEle.getText());
            }
            //Displaying the list elements on console
            for (WebElement s : namesElements) {
            	System.out.println(s.getText());
            }
            

        } catch (Exception e) {
            System.err.println("An error occurred during extraction: " + e.getMessage());
        } finally {
            // 6. Ensure browser session terminates to free memory
            driver.quit();
        }
        
        System.out.println("asdf");
    }
    
	
    public static void webScrapingWithSelenium3() {
    	//Durum : Calisiyor
    	//Tarih : 19.06.2026
    	//Diger : Bazen peş peşe birkaç defa calistirildiginda exception olusuyor. 
        //        Olusan hata : ("HTTP/1.1 header parser received no bytes")
    	
        // 1. Configure Chrome options for scraping
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new"); // Run background process without UI
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                
    	// 2. Initialize the WebDriver
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        
        
        //Database parameters
        String jdbcUrl = "jdbc:postgresql://localhost:5439/postgres";
        String user = "postgres";
        String pass = "123456";


        
        try {
        	
        	Connection connection = DriverManager.getConnection(jdbcUrl, user, pass);
            System.out.println("Java JDBC bağlantısı başarıyla gerçekleşti.");
        	
        	
            // 3. Navigate to target URL
        	String url = "https://tr.investing.com/equities";
        	Statement statement = connection.createStatement();
        	

            driver.get(url);
            
            Thread.sleep(2000);
            
            System.out.println( driver.getTitle() );   //"Bir dakika lütfen..." çıktısı oluşuyor.
                        

            // 4. Implement Explicit Wait for dynamic JS content to render
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            wait.until(ExpectedConditions.not(ExpectedConditions.titleIs("Bir dakika lütfen...")));
            wait.until(ExpectedConditions.not(ExpectedConditions.titleContains("Please")));
            wait.until(ExpectedConditions.not(ExpectedConditions.titleContains("please")));
            
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("datatable-v2_body__8TXQk")));
            
            //List<WebElement> tableRows = driver.findElements(By.cssSelector("tbody>tr>td:nth-child(1)"));
            WebElement tableBody = driver.findElement(By.className("datatable-v2_body__8TXQk"));
            //List<WebElement> namesElements2 = driver.findElements(By.cssSelector("datatable-v2_row__hkEus dynamic-table-v2_row__ILVMx"));
            List<WebElement> rows = tableBody.findElements(By.tagName("tr"));
            
            //List<WebElement> namesElements = driver.findElements(By.cssSelector("tbody>tr>td:nth-child(1)"));
            System.out.println("size of names elements : " + rows.size());
                       
            
            List<String> names = new ArrayList<String>();
            
            
        	System.out.printf("%4s    %-50s    %8s    %8s   %8s  %8s   %8s \n",   "", "Firma Adi", "Son", "Yüksek", "Düşük", "Fark", "Fark%");  //"Hac.", "Zaman"
        	
            //Adding column1 elements to the list
        	int i=0;
            for (WebElement row : rows) {
            	names.add(row.getText());
            	List<WebElement> list = row.findElements(By.tagName("td"));
            	String [] arr = 
            	{
    				list.get(1).getText(),   //Firma Adı 
    				list.get(2).getText(),   //Son
    				list.get(3).getText(),   //Yüksek
    				list.get(4).getText(),   //Düşük
    				list.get(5).getText(),   //Fark
    				list.get(6).getText(),   //Fark %
    				list.get(7).getText(),   //Hac.
    				list.get(8).getText()    //Zaman
            	};
            	
            	System.out.printf("%4d.  %-50s     %8s   %8s   %8s   %8s   %8s  \n", ++i, arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]);
            	
            	String sqlSorgusu = "INSERT INTO hisse_senedi(Firma_Adi, Son, Yuksek, Dusuk, Fark, Fark_yuzde, Hac, Zaman)" +           
                        " VALUES('" + arr[0] + "','" +  arr[1] + "','" + arr[2] + "','" + arr[3] + "','" + arr[4] + "','" + arr[5] + "','" + arr[6] + "','" + arr[7] +"')";
            	
            	System.out.println(sqlSorgusu);
            	
                int kayitSayisi = statement.executeUpdate(sqlSorgusu);
                System.out.printf("%d kayıt eklendi.%n", kayitSayisi);
            	
            }
           
            

        } 
        catch (Exception e) 
        {
            //System.err.println("An error occurred during extraction: " + e.getMessage());
            e.printStackTrace();
        } 
        finally 
        {
            // 6. Ensure browser session terminates to free memory
        	//Closes every associated window/tab, terminates the active WebDriver background session, 
        	//and kills the driver executable process (e.g., chromedriver) running on your operating system. 
        	//Always use this to prevent memory leaks.
            driver.quit();  
        }
        
        
    }
    
	public static void main(String[] args) {
		webScrapingWithSelenium3();

	}

}








