package bist;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Veritabani {

	public static String parametreGetir() 
	{
		//SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");		
		
		Connection con = WebScrapingHisseSenedi.getPostgresConnection();
		PreparedStatement p=null;
		ResultSet rs=null;
		
		String sql = "select borsa_tarih from hisse_senedi2_prm";
		String borsaTarih="";
		
		try {
			p = con.prepareStatement(sql);
			rs =p.executeQuery();

			if (rs.next()) 
			{
	            borsaTarih = rs.getString("borsa_tarih");	                
	            //borsaTarihStr = formatter.format( borsaTarih );	                
	            System.out.println("borsa_tarih : " + borsaTarih );
			}
			else 
			{
				System.out.println("Parametre okunamadı");
			}
	        
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

	public static String parametreGuncelle(String zaman) 
	{
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");		
		
		Connection conn = WebScrapingHisseSenedi.getPostgresConnection();
		Statement stmt=null;
		
		
		String sql = "update hisse_senedi2_prm set borsa_tarih = '" + zaman + "', guncelleme_tarihi=" + "TO_CHAR(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS')";
		
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
	
	
	public static void logEkle(String zaman, String log_metin) 
	{						
		Connection conn = WebScrapingHisseSenedi.getPostgresConnection();
		Statement stmt=null;
				
		//insert into hisse_senedi2_log set borsa_tarih = '1.10.2003, aciklama = Bu tarih için veri alınamadı. detSearch alanı null geliyor.', guncelleme_tarihi=TO_CHAR(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS')
		String sql = "insert into hisse_senedi2_log(borsa_tarih, aciklama, guncelleme_tarihi) values('"+ zaman +"', '"+ log_metin +"', " + "TO_CHAR(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS'))";
						
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
		
		//Veritabani.parametreGuncelle("01.01.2002");
		String prm = parametreGetir();
		
		System.out.println(prm);
		

	}

}
