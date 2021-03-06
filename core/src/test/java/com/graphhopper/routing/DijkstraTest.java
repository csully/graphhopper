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
package com.graphhopper.routing;

import com.graphhopper.routing.util.*;
import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.graphhopper.storage.Graph;

/**
 *
 * @author Peter Karich
 */
@RunWith(Parameterized.class)
public class DijkstraTest extends AbstractRoutingAlgorithmTester
{
    /**
     * Runs the same test with each of the supported traversal modes
     */
    @Parameters
    public static Collection<Object[]> configs()
    {
        return Arrays.asList(new Object[][]
        {
            { TraversalMode.NODE_BASED },
            { TraversalMode.EDGE_BASED_1DIR },
            { TraversalMode.EDGE_BASED_2DIR },
            { TraversalMode.EDGE_BASED_2DIR_UTURN }
        });
    }

    private TraversalMode traversalMode;

    public DijkstraTest( TraversalMode tMode )
    {
        this.traversalMode = tMode;
    }

    @Override
    public AlgorithmPreparation prepareGraph( Graph defaultGraph, final FlagEncoder encoder, final Weighting weighting )
    {
        return new NoOpAlgorithmPreparation()
        {
            @Override
            public RoutingAlgorithm createAlgo()
            {
                return new Dijkstra(_graph, encoder, weighting, traversalMode);
            }
        }.setGraph(defaultGraph);
    }
}
