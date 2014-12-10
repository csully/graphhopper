/*
 * Simple script for periodically scraping traffic data from a public dataset
 * in NYC.
 *
 * Written by Chris Sullivan
 * csully@bu.edu
 *
 */
import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.text.*;

public class TrafficScraper{

    public static void scrape(String urlName, String destname){
	Writer w = null;
	URL url;
	try{

	    w = new BufferedWriter(new OutputStreamWriter(
		       new FileOutputStream(destname), "utf-8"));
	    url = new URL(urlName);

	    BufferedReader in = new BufferedReader(
				new InputStreamReader(url.openStream()));



	    String inputLine;

	    while ((inputLine = in.readLine()) != null)
		w.write(inputLine + "\n");
	    in.close();
	    

	} catch (IOException e){
	    System.out.println("ERROR OCCURED");
	    return;
	    
	}finally {
	    try{
		w.close();
	    } catch (IOException e) {}
	}
    }


    public static void main(String[] args) 
	throws InterruptedException{
	
	String urlName = "http://207.251.86.229/nyc-links-cams/LinkSpeedQuery.txt";
	String fileName = "";

	int interval = 900000; //15 minute intervals
	while(true){
	    Date date = new Date();
	    SimpleDateFormat df = 
            new SimpleDateFormat ("MM.dd.yyyy.hh-mm-ss");
	    fileName = "./data/" + df.format(date) + ".txt";

	    //System.out.println(df.format(date));
	    scrape(urlName,fileName);
	    Thread.sleep(interval);
	}
	

    }

}
