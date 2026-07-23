package bist;


import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.SocketTimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


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
	
	public static Document getWebData(Connection conn, String url, LocalDate veriZamani) throws Exception 
	{
    	//System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
    	//System.setProperty("https.agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36");
		Document doc = null;
				
		do 
		{
			try 
			{
				doc = Jsoup.connect(url)                    
		                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
		                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
		                .header("Accept-Language", "en-US,en;q=0.9")
		                .header("Accept-Encoding", "gzip, deflate, br")
		                .header("Connection", "keep-alive")
		                //.header("Referer", "https://google.com")
		                .header("Referer", url)
		                .timeout(5000)   //5.000 ms( 5 sn )
		                .ignoreHttpErrors(true) 
		                .get();
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				
				String msg = e.getMessage();
				
				Veritabani.logEkle(conn, veriZamani, "Exception oluştu : " + e.getMessage() );
				
				if( msg.equals("Read timed out") )
				{			
		            System.out.println("It is SocketTimeoutException");
		            Thread.sleep(10000);   //10 sn
		        }
				else
				{
		             throw e;
		        }			
			}
		}while(doc==null);

		return doc;
        // İlgili fiyat verisinin CSS sınıfı veya ID'si (Örn: .valueClass)
        // Tarayıcınızda F12 ile inceleyip doğru elementi seçmeniz gerekir
	}	
	
	public static void webScrapingTarih(Connection conn, LocalDate veriZamani) throws Exception
	{
		boolean hasNextPage = true;
		int pageNumber = 0;
		//String aktarimZamani = null;
		String url="";
		Document doc;
		//SimpleDateFormat formatterTarihZaman = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		
		//Statement statement = conn.createStatement();
		
		int gun, ay, yil;
		
		/*String []arrZaman = veriZamani.split("\\.");
		gun = arrZaman[0];
		ay  = arrZaman[1];
		yil = arrZaman[2];
		*/
		gun = veriZamani.getDayOfMonth();
		ay  = veriZamani.getMonthValue();
		yil = veriZamani.getYear();
		
		while(hasNextPage)
    	{            		
    		pageNumber++;
    	
        	url = "https://uzmanpara.milliyet.com.tr/borsa/gecmis-kapanislar/?Pagenum=" + pageNumber + "&tip=Hisse&gun="+ gun + "&ay=" + ay + "&yil=" + yil;
        
        	System.out.println("URL : " + url);
        	
            doc = getWebData(conn, url, veriZamani );

            // İlgili fiyat verisinin CSS sınıfı veya ID'si (Örn: .valueClass)
            // Tarayıcınızda F12 ile inceleyip doğru elementi seçmeniz gerekir
            
            System.out.println(doc.title());
            
            Element detSearch = doc.select(".detSearch").first();            //verinin ait olduğu gün bilgisi bulunuyor
            Element table = doc.select("table").first();                     //verinin kendisi
            Element pager = doc.select(".pager").first();                    //		            		             
            
            if(detSearch==null) 
            {			             		            		           
            	//Veritabani.logEkle(conn, veriZamani, "Bu tarih için veri alınamadı. detSearch alanı null geliyor.");            		                        	
            	Veritabani.logEkle(conn, veriZamani, "Bu tarih için veri alınamadı. detSearch alanı null geliyor.");
	            hasNextPage = false;
            	break;
            }
            
            int scrapingDay = Integer.parseInt(detSearch.select("select[name=gun] option[selected]").text());
            int scrapingAy  = Integer.parseInt(detSearch.select("select[name=ay] option[selected]").val() );
            int scrapingYil = Integer.parseInt(detSearch.select("select[name=yil] option[selected]").text() );
            
            //String scrapingDate = scrapingDay + "." + scrapingAy + "." +  scrapingYil;
            LocalDate scrapingDate = LocalDate.of( scrapingYil, scrapingAy , scrapingDay);
            
            if(!veriZamani.equals(scrapingDate)) 		            
            {
            	System.out.println(veriZamani + " tarihi için borsa verisi bulunmuyor");
            	Veritabani.logEkle(conn, veriZamani, "Bu tarih için veri alınamadı. veriZamani= " + veriZamani + " ile scrapingDate = " + scrapingDate + " aynı değiller.");
            	
            	hasNextPage = false;
            	break;
            }		            		            
            
            Elements rows = table.getElementsByTag("tr");
            	            	                        
            //Table header kısmı
            if(pageNumber==1) 
            {
	            for(Element col: rows.get(0).getElementsByTag("th")) 
	            {
	            	System.out.printf("%15s", col.text());
	            }
	            System.out.printf("%15s\n", "Tarih");
            }

            //Table data kısmı
            for(int i=1; i<rows.size(); i++) 
            {
            	Element row = rows.get(i);
            	Elements cols = row.getElementsByTag("td");
            	
            	//"." karakteri binlik ayracı olarak kullanılmıs. Once bu kaldırılır. 
            	//"," karakteri ondalık karakteri olarak kullanılmış. Veritabanına aktarırken ondalık ayracı olarak "." karakteri kullanılmalı.
            	String [] arr = //new String[10];            	
            	{
            		cols.get(0).text(),   //Firma Adı 
            		cols.get(1).text().replace(".", "").replace(",", "."),   //Son
            		cols.get(2).text().replace(".", "").replace(",", "."),   //dun
            		cols.get(3).text().replace(".", "").replace(",", "."),   //yuzde
            		cols.get(4).text().replace(".", "").replace(",", "."),   //yuksek
            		cols.get(5).text().replace(".", "").replace(",", "."),   //dusuk,
            		cols.get(6).text().replace(".", "").replace(",", "."),   //Ağ. Ort.,
            		cols.get(7).text().replace(".", "").replace(",", "."),   //Hacim lot
            		cols.get(8).text().replace(".", "").replace(",", ".")    //Hacim Bin TL
            	      				                		
            	};
            	
            	/*
            	String sql = "insert into bist_verileri.Hisse_Senedi3(firma_adi, son, dun, yuzde, yuksek, dusuk, ag_ort, hacim_lot, hacim_bin_tl, veri_zaman, aktarim_zamani, url) values(";
            
            	//System.out.println("");
            	aktarimZamani = formatterTarihZaman.format( Calendar.getInstance().getTime() );  
            	
            	for(int j=0; j<cols.size(); j++)   //8 kolon var
            	{
            		sql += "'"+ arr[j] +"', ";
            		System.out.printf("%15s", arr[j] + "  ");
            	}
            	
            	arr[9]  = "to_date('" + veriZamani + "','dd.MM.yyyy')";
            	arr[10] = "to_timestamp('" + aktarimZamani + "','dd.MM.yyyy HH24:MI:SS')";  
            	arr[11] = url;
            	
            	sql += arr[9] + ", " + arr[10] +", '" + url + "')";
            	
            	System.out.printf("%15s\n", veriZamani);
            	*/
            	//System.out.println("sql = " + sql);            	
            	//int kayitSayisi = statement.executeUpdate(sql);
            	//statement.executeUpdate(sql);
            	Veritabani.veriEkle(conn, veriZamani, arr, url);
            	System.out.print("");
            }            
            if(rows.size()<2) 
            	hasNextPage = false;                         
    	}
	}
    
	public static void webScrapingWithJSoupUzmanPara() throws Exception 
	{
        // Örnek hedef URL (kullanılmak istenilen finans sitesi)
        
		// Temel URL  : https://uzmanpara.milliyet.com.tr/borsa/gecmis-kapanislar/?Pagenum=2&tip=Hisse&gun=3&ay=7&yil=2000&Harf=-1
		// ilk Tarih : 3.1.2000

		Connection conn = null;
		LocalDate veriZamani = null;		
        
		try 
        {        	               
        	conn = Veritabani.getPostgresConnection();        	
        	
            // Web sitesine bağlanma ve HTML'i çekme
                    
            //String prmEnsonTarih = Veritabani.parametreGetir(conn);
        	LocalDate prmEnsonTarih = Veritabani.parametreGetir(conn);
            
            //int []prmParts = Veritabani.splitDate(prmEnsonTarih);                        
            
            //LocalDate date1 = LocalDate.of(2002, 1, 1);
            //LocalDate date1 = LocalDate.of(prmParts[2], prmParts[1], prmParts[0]);
            LocalDate date1 = prmEnsonTarih;
            
            //LocalDate date2 = LocalDate.of(2000, 1, 10);
            LocalDate date2 = LocalDate.now();
            
            //int gun, ay, yil;
            
            veriZamani = prmEnsonTarih;            
            
         // 2. Define your desired date pattern            
            //DateTimeFormatter formatterTarih = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                                    
            while(date1.isBefore(date2))
            {            	
            	
            	//prmParts = Veritabani.splitDate(prmEnsonTarih);
            	//date1 = LocalDate.of(prmParts[2], prmParts[1], prmParts[0]);
            	
            	DayOfWeek day =  date1.getDayOfWeek();
            	
            	/*
            	gun = date1.getDayOfMonth();
            	ay  = date1.getMonthValue();
            	yil = date1.getYear();
            	*/
            	//veriZamani = gun + "." + ay + "." + yil;
            	              	
            	/*
            	if(yil==2026) 
            	{
            		System.out.println("yil = " + yil + " olduğundan döngü sonlandırıldı.");
            		break;
            	}*/
            	
            	if( day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) 
            	{
            		System.out.println("Bu tarih hafta sonu olduğu için veri alınamadı. Tarih : " + veriZamani);
            		Veritabani.logEkle(conn, veriZamani, "Bu tarih için hafta sonu olduğu için veri alınamadı. ");
            		date1 = date1.plusDays(1);
            		day =  date1.getDayOfWeek();
            		continue;            	
            	}            	            	                      	            	            	
            	
            	webScrapingTarih(conn, veriZamani);
	            	            
	            date1 = date1.plusDays(1);
	            
	            //Veritabani.parametreGuncelle(conn, formatterTarih.format( date1 ));
	            Veritabani.parametreGuncelle(conn, date1 );
	            //System.out.println(detSearch.html());
	            //System.out.println(table.html());
	            //System.out.println(pager.html());	                       
	           // System.out.println(doc.html());            
	            //System.out.println(doc.title());
	            Thread.sleep(1000);   //1000ms = 1 sec
	            
	           // break; 	            
	        } 
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
        	
        	Veritabani.logEkle(conn, veriZamani, "Exception oluştu : " + e.getMessage() );
        	
            //System.err.println("Bağlantı hatası: " + e.getMessage());
        }
					
    }
	
    public static void webScrapingWithSelenium() 
    {
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
		
    public static void webScrapingWithSelenium2() 
    {
    	
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
    	
    public static void webScrapingWithSelenium3() 
    {
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
            
            
        	System.out.printf("%4s    %-50s    %8s    %8s   %8s  %8s   %8s   %8s   %8s \n",   "", "Firma Adi", "Son", "Yüksek", "Düşük", "Fark", "Fark", "Hac.", "Zaman");
        	
            //Adding column1 elements to the list
        	int i=0;        	
            for (WebElement row : rows) 
            {
            	names.add(row.getText());
            	List<WebElement> list = row.findElements(By.tagName("td"));
            	String [] arr = 
            	{
    				list.get(1).getText(),   //Firma Adı 
    				list.get(2).getText(),   //Son
    				list.get(3).getText(),   //Yüksek
    				list.get(4).getText(),   //Düşük
    				list.get(5).getText(),   //Fark
    				list.get(6).getText().replace("%", ""),    //Fark.  '%' karakteri olmadan veritabanına aktarılsın.
    				list.get(7).getText(),   //Hac.
    				list.get(8).getText().replace('/', '.')+"." + LocalDate.now().getYear()    //Yil bilgisi gelmediği için biz ekliyoruz. Zaman
            	};
            	
            	System.out.printf("%4d.  %-50s     %8s   %8s   %8s   %8s   %8s   %8s   %8s  \n", ++i, arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7]);
            	
            	String sqlSorgusu = "INSERT INTO hisse_senedi(Firma_Adi, Son, Yuksek, Dusuk, Fark, Fark_yuzde, Hac, Zaman)" +           
                        " VALUES('" + arr[0] + "','" +  arr[1] + "','" + arr[2] + "','" + arr[3] + "','" + arr[4] + "','" + arr[5] + "','" + arr[6] + "','" + arr[7] +"')";
            	
            	//System.out.println(sqlSorgusu);
            	
                int kayitSayisi = statement.executeUpdate(sqlSorgusu);
                //System.out.printf("%d kayıt eklendi.%n", kayitSayisi);
            	
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
    
	public static void main(String[] args) 
	{
		//webScrapingWithSelenium3();				
		
		try 
		{
			Connection conn = Veritabani.getPostgresConnection();
			//webScrapingTarih(conn,"3.7.2026");
			webScrapingWithJSoupUzmanPara();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		System.out.println("\n------------------Program sonlandı.---------------------");	
	}

}








