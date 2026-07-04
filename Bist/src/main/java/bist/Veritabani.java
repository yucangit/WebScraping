package bist;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Veritabani {

	public static void main(String[] args) 
	{
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");		
		
		Connection con = WebScrapingHisseSenedi.getPostgresConnection();
		PreparedStatement p=null;
		ResultSet rs=null;
		
		String sql = "select borsa_tarih from hisse_senedi2_prm";
		
		try {
			p = con.prepareStatement(sql);
			rs =p.executeQuery();

			if (rs.next()) 
			{
	            Date borsaTarih = rs.getDate("borsa_tarih");	                
	            String borsaTarihStr = formatter.format( borsaTarih );	                
	            System.out.println("borsa_tarih : " + borsaTarihStr );
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
		

	}

}
