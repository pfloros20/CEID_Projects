<?php
	function getAreaOfPolygon($geometry) {
	    $area = 0;
	    for ($ri=0, $rl=sizeof($geometry->rings); $ri<$rl; $ri++) {
	        $ring = $geometry->rings[$ri];

	        for ($vi=0, $vl=sizeof($ring); $vi<$vl; $vi++) {
	            $thisx = $ring[ $vi ][0];
	            $thisy = $ring[ $vi ][1];
	            $nextx = $ring[ ($vi+1) % $vl ][0];
	            $nexty = $ring[ ($vi+1) % $vl ][1];
	            $area += ($thisx * $nexty) - ($thisy * $nextx);
	        }
	    }

	    // done with the rings: "sign" the area and return it
	    $area = abs(($area / 2));
	    return $area;
	}

	function getCentroidOfPolygon($geometry) {
	    $cx = 0;
	    $cy = 0;

	    for ($ri=0, $rl=sizeof($geometry->rings); $ri<$rl; $ri++) {
	        $ring = $geometry->rings[$ri];

	        for ($vi=0, $vl=sizeof($ring); $vi<$vl; $vi++) {
	            $thisx = $ring[ $vi ][0];
	            $thisy = $ring[ $vi ][1];
	            $nextx = $ring[ ($vi+1) % $vl ][0];
	            $nexty = $ring[ ($vi+1) % $vl ][1];

	            $p = ($thisx * $nexty) - ($thisy * $nextx);
	            $cx += ($thisx + $nextx) * $p;
	            $cy += ($thisy + $nexty) * $p;
	        }
	    }

	    // last step of centroid: divide by 6*A
	    $area = getAreaOfPolygon($geometry);
	    $cx = -$cx / ( 6 * $area);
	    $cy = -$cy / ( 6 * $area);

	    // done!
	    return array($cx,$cy);
	}
?>