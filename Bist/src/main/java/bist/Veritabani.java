package bist;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class Veritabani 
{
	public static Connection getPostgresConnection() 
	{
	    //Database parameters
	    String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
	    String user = "postgres_kul1";
	    String pass = "123456";
	    Connection conn = null;
		    
	    try 
	    {	    
	    	conn = DriverManager.getConnection(jdbcUrl, user, pass);
	        System.out.println("Java JDBC baglantisi basariyla gerceklesti.");
	    	//Statement statement = connection.createStatement();
	    }
	    catch (Exception e) 
	    {
			e.printStackTrace();
		}
	    
	    return conn;
    }

	public static LocalDate parametreGetir(Connection conn) 
	{
		//SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");		
				
		PreparedStatement p=null;
		ResultSet rs=null;
		
		String sql = "select borsa_tarih from bist_verileri.hisse_senedi3_prm";
		LocalDate borsaTarih=null;
		
		try 
		{
			p = conn.prepareStatement(sql);
			rs =p.executeQuery();

			if (rs.next()) 
			{
				// = rs.getObject("borsa_tarih");
	            borsaTarih = rs.getObject("borsa_tarih", LocalDate.class);	                
	            //borsaTarihStr = formatter.format( borsaTarih );	                
	            System.out.println("borsa_tarih : " + borsaTarih );
			}
			else 			
				System.out.println("Parametre okunamadı");
			
	        
		} 
		catch (SQLException e) 
		{		
			e.printStackTrace();
		}
		
		return borsaTarih ;				
	}

	public static void veriEkle(Connection conn, LocalDate veriZamani, String [] dataCols, String url) throws SQLException 
	{
		Statement statement = null;
		
		try 
		{
			statement = conn.createStatement();
		
			String sql = "insert into bist_verileri.Hisse_Senedi3(firma_adi, son, dun, yuzde, yuksek, dusuk, ag_ort, hacim_lot, hacim_bin_tl, veri_zaman, aktarim_zamani, url) values(";
	        
	    	//System.out.println("");
	    	//aktarimZamani = formatterTarihZaman.format( Calendar.getInstance().getTime() );  
	    		
			sql += "'"+ dataCols[0] +"', ";
			System.out.printf("%15s", dataCols[0] + "  ");
			
	    	for(int j=1; j<dataCols.length; j++)   //8 kolon var
	    	{
	    		sql += dataCols[j] +", ";
	    		System.out.printf("%15s", dataCols[j] + "  ");
	    	}
	    	
	    	//arr[9]  = "to_date('" + veriZamani + "','dd.MM.yyyy')";
	    	//arr[10] = "CURRENT_TIMESTAMP";  
	    	//arr[11] = url;
	    	
	    	sql += "'"+veriZamani +"'" + ", CURRENT_TIMESTAMP" + ", '" + url + "')";
	    	
	    	System.out.printf("%15s\n", veriZamani);
	    	
	    	//System.out.println("sql = " + sql);            	
	    	//int kayitSayisi = statement.executeUpdate(sql);
	    	statement.executeUpdate(sql);
		}
		catch (Exception e) 
		{
			throw e;			
		}
	}
	
	public static int[] splitDate(String tarih)
	{
		//dd.MM.yyyy şeklindeki string tarih verisini int[]{yil, ay, gun} formatına dönüştürür.
		String []prmTarihParts = tarih.split("\\."); 
		int []prmParts = new int[3];
         
         for(int i=0; i<3; i++)
         	prmParts[i] = Integer.parseInt(prmTarihParts[i]);
		
         return prmParts;
	}

	public static String parametreGuncelle(Connection conn, LocalDate zaman) 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");		
		
		//Connection conn = WebScrapingHisseSenedi.getPostgresConnection();
		Statement stmt=null;
		
		
		String sql = "update bist_verileri.hisse_senedi3_prm set borsa_tarih = '" + zaman + "', guncelleme_tarihi=" + "CURRENT_TIMESTAMP";
		
		String borsaTarihStr="";
		
		try {
			stmt = conn.createStatement();
	            
            // Bind parameters to the placeholders (?)
             

            // Execute the data modification statement
            int rowsAffected = stmt.executeUpdate(sql);
            System.out.println("Parametre Tablosu güncellendi: " + rowsAffected);
	        
		} 
		catch (SQLException e) 
		{		
			e.printStackTrace();
		}
		
		return borsaTarihStr ;
	}
		
	public static void logEkle(Connection conn, LocalDate zaman, String log_metin) throws Exception 
	{						
		//Connection conn = WebScrapingHisseSenedi.getPostgresConnection();
		Statement stmt=null;
				
		//insert into hisse_senedi2_log set borsa_tarih = '1.10.2003, aciklama = Bu tarih için veri alınamadı. detSearch alanı null geliyor.', guncelleme_tarihi=TO_CHAR(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS')
		//String sql = "insert into bist_verileri.hisse_senedi3_log(borsa_tarih, aciklama, guncelleme_tarihi) values('"+ zaman +"', '"+ log_metin +"', " + "TO_CHAR(CURRENT_TIMESTAMP, 'DD.MM.YYYY HH24:MI:SS'))";
		String sql = "insert into bist_verileri.hisse_senedi3_log(borsa_tarih, aciklama, guncelleme_tarihi) values('"+ zaman +"', '"+ log_metin +"', " + " CURRENT_TIMESTAMP)";
						
		try 
		{
			stmt = conn.createStatement();	                                
            
            int rowsAffected = stmt.executeUpdate(sql);
            System.out.println("Log ekleme başarılı." + rowsAffected);
            
		} 
		catch (Exception e) 
		{		
			e.printStackTrace();
			throw e;			
		}				
	}
		
	public static void main(String[] args) 
	{
		
		Connection conn = getPostgresConnection();
		
		//Veritabani.parametreGuncelle("01.01.2002");
		LocalDate prm = parametreGetir(conn);
		
		//System.out.println(prm);
		
	}

}
