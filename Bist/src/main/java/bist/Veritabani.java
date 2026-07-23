package bist;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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

	public static String parametreGetir(Connection conn) 
	{
		//SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");		
				
		PreparedStatement p=null;
		ResultSet rs=null;
		
		String sql = "select borsa_tarih from bist_verileri.hisse_senedi2_prm";
		String borsaTarih="";
		
		try 
		{
			p = conn.prepareStatement(sql);
			rs =p.executeQuery();

			if (rs.next()) 
			{
	            borsaTarih = rs.getString("borsa_tarih");	                
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
	
	public static int[] splitDate(String tarih)
	{
		//dd.MM.yyyy şeklindeki string tarih verisini int[]{yil, ay, gun} formatına dönüştürür.
		String []prmTarihParts = tarih.split("\\."); 
		int []prmParts = new int[3];
         
         for(int i=0; i<3; i++)
         	prmParts[i] = Integer.parseInt(prmTarihParts[i]);
		
         return prmParts;
	}

	public static String parametreGuncelle(Connection conn, String zaman) 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");		
		
		//Connection conn = WebScrapingHisseSenedi.getPostgresConnection();
		Statement stmt=null;
		
		
		String sql = "update bist_verileri.hisse_senedi2_prm set borsa_tarih = '" + zaman + "', guncelleme_tarihi=" + "TO_CHAR(CURRENT_TIMESTAMP, 'DD.MM.YYYY HH24:MI:SS')";
		
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
		
	public static void logEkle(Connection conn, String zaman, String log_metin) 
	{						
		//Connection conn = WebScrapingHisseSenedi.getPostgresConnection();
		Statement stmt=null;
				
		//insert into hisse_senedi2_log set borsa_tarih = '1.10.2003, aciklama = Bu tarih için veri alınamadı. detSearch alanı null geliyor.', guncelleme_tarihi=TO_CHAR(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS')
		String sql = "insert into bist_verileri.hisse_senedi2_log(borsa_tarih, aciklama, guncelleme_tarihi) values('"+ zaman +"', '"+ log_metin +"', " + "TO_CHAR(CURRENT_TIMESTAMP, 'DD.MM.YYYY HH24:MI:SS'))";
						
		try {
			stmt = conn.createStatement();	                                
            
            int rowsAffected = stmt.executeUpdate(sql);
            System.out.println("Log ekleme başarılı." + rowsAffected);
            
		} 
		catch (SQLException e) 
		{		
			e.printStackTrace();
		}				
	}
		
	public static void main(String[] args) 
	{
		
		getPostgresConnection();
		
		//Veritabani.parametreGuncelle("01.01.2002");
		//String prm = parametreGetir();
		
		//System.out.println(prm);
		
	}

}
