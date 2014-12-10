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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TrafficClient{

    public static void answerQuery(GraphHopper g, String[] args){
	Scanner input = new Scanner(System.in);
	System.out.print("Enter the longtitude and latitude of your " + 
			 "source separated by commas (long,lat): ");
	String src = input.nextLine();

	System.out.print("Enter the longtitude and latitude of your" + 
			 "destination separated by commas (long,lat): ");
	
	String dest = input.nextline();
	
	
	    
    }
    
    
    public static void main(String[] args){
	
	Map<String, GraphHopper> db = new HashMap<String, GraphHopper>();
	
	File dir = new File("./traffic/data/");
	String[] trafficFiles = dir.list();
	for(String time : trafficFiles){
	    String graphHopperName = time.replace(".txt");
	    GraphHopper g = new GraphHopper();
	    CmdArgs a = new CmdArgs();
	    g.init(a.read(args));
	    //code to add weights from file parsing goes here
	    
	    db.put(g);
	}
	
	while(true){
	    answerQuery(db)
	}
}
