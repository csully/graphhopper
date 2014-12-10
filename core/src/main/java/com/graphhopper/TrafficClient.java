/*
 *  Licensed to GraphHopper and Peter Karich under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for 
 *  additional information regarding copyright ownership.
 * 
 *  GraphHopper licenses this file to you under the Apache License, 
 *  Version 2.0 (the "License"); you may not use this file except in 
 *  compliance with the License. You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*
 * A client for using the GraphHopper package in many instances for time 
 * paramterized queries.
 * 
 * Chris Sullivan csully@bu.edu
 * Shiran Sukumar shiran@bu.edu
 *
 * Sources:
 * https://github.com/graphhopper/graphhopper/blob/master/docs/core/low-level-api.md
 */


import com.graphhopper.GraphHopper;
import com.graphhopper.reader.DataReader;
import com.graphhopper.reader.OSMReader;
import com.graphhopper.reader.dem.CGIARProvider;
import com.graphhopper.reader.dem.ElevationProvider;
import com.graphhopper.reader.dem.SRTMProvider;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.RoutingAlgorithm;
import com.graphhopper.routing.ch.PrepareContractionHierarchies;
import com.graphhopper.routing.util.*;
import com.graphhopper.storage.*;
import com.graphhopper.storage.index.*;
import com.graphhopper.util.*;
import com.graphhopper.util.shapes.GHPoint;
import com.graphhopper.util.FlagEncoder;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TrafficClient{

    public static void answerQuery(GraphHopper g, String outName){
	Scanner input = new Scanner(System.in);
	System.out.print("Enter the longtitude and latitude of your " + 
			 "source separated by commas (long,lat): ");
	String src = input.nextLine();
	System.out.print("Enter the longtitude and latitude of your" + 
			 "destination separated by commas (long,lat): ");
	String dest = input.nextline();
	input.close();

	String[] tups = src.split(",");
	String[] desttups = dest.split(",");
	
	Double srclong = Double.parseDouble(tups[0].trim());
	Double srclat =  Double.parseDouble(tups[1].trim());

	Double destlong = Double.parseDouble(tups[0].trim());
	Double destlat =  Double.parseDouble(tups[1].trim());



	LocationIndex i = g.getLocationIndex();
	GraphStorage graph = g.getGraph();
	FlagEncoder encoder = new CarFlagEncoder();

	QueryResult fromQR = i.findClosest(srclat,srclong, EdgeFilter.All_EDGES);
	QueryResult toQR = i.findID(destlat,destlong, EdgeFilter.All_EDGES);

	Path path = new Dijkstra(graph,encoder).calcPath(fromQR,toQR);
	
	FileOutputStream f = new FileOutputStream(new File(outName));
	String out = path.toDetailString();
	f.write(out.getBytes());
	f.close();
	    
    }

    public static String getTime(){
	Scanner input = new Scanner(System.in);
	System.out.print("What time will you be traveling? " + 
			 "Enter a valid hour on 24 hr clock: ");
	
	return input.nextLine().trim();
    }
    
    public static String getHash(String filename){
	String ret = filename.substring(11,13);
	return ret;
    }
    
    public static void addWeights(MyGraphHopper g, String filename){
	File f = new File(filename);
	String headers = f.readLine();
	BlockWeighting weights = g.createWeighting(new CarFlagEncoder());
	while(f.hasNextLine()){

	    String line = f.readLine();
	    String[] args = line.split("\t");
	    Double speed = Double.parseDouble(args[1].replace("\"",""));
	    String[] links = args[6].replace("\"","").split();
	    Double[] longs = new Double[links.length()];
	    Double[] lats = new Double[links.length()];

	    for (int i = 0; i < links.length(); i++){
		String[] coords = links[i].split(",");
		lats[i] = Double.parseDouble(coords[0].trim());
		longs[i] = Double.parseDouble(coords[1].trim());
	    }
	    LocationIndex i = g.getLocationIndex();
	    GraphStorage graph = g.getGraph();
	    
	    QueryResult fromQR = i.findClosest(lats[0],longs[0], EdgeFilter.All_EDGES);
	    QueryResult toQR = i.findID(lats[lats.length - 2],longs[longs.length - 2], EdgeFilter.All_EDGES);
	    
	    EdgeIteratorState src = fromQR.getClosestEdge();
	    EdgeIteratorState dest = toQR.getClosestEdge();
	    
	    weights.setSpeed(src.getEdge(),speed);
	    weights.setSpeed(dest.getEdge(),speed);
					     
	}
    }


    public static void main(String[] args){
	
	Map<String, GraphHopper> db = new HashMap<String, GraphHopper>();
	
	File dir = new File("./traffic/data/");
	String[] trafficFiles = dir.list();

	for(String time : trafficFiles){
	    String graphHopperName = getHash(time);
	    GraphHopper g = new MyGraphHopper();
	    CmdArgs a = new CmdArgs();
	    g.init(a.read(args));
	    addWeights(g,time);
	    //code to add weights from file parsing goes here
	    
	    db.put(graphHopperName,g);
	}
	
	while(true){
	    String time = getTime();
	    GraphHopper g = db.get(time);
	    answerQuery(db)
	}
}
