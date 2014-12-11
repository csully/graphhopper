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

package com.graphhopper;
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


public class BlockWeighting implements Weighting 
{
    private final FlagEncoder encoder;
    private Set<Integer> trafficEdges = new Set<Integer>();
    private Map<Integer, Double> speeds = new Map<Integer, Double>();

    public BlockingWeighting( FlagEncoder encoder)
    {
        this.encoder = encoder;

    }

    public void setSpeed(int edge, Double speed){
	this.speeds.put(edge,speeds);
	this.trafficEdges.put(edge);
    }

    @Override
    public double getMinWeight( double distance )
    {
        return distance / maxSpeed;
    }

    @Override
    public double calcWeight( EdgeIteratorState edgeState, boolean reverse, int prevOrNextEdgeId )
    {
	if(trafficEdges.contains(edgeState.getEdge())){
	    double speed = speeds.get(edgeState.getEdge());
	    return edgeState.getDistance() / speed;
	}
	return edgeState.getDistance() / this.encoder.getSpeed(edgeState.getFlags());
    }

    @Override
    public String toString()
    {
        return "BLOCKING";
    }
}
