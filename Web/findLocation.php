<?php
	/**
	 * Calculates the great-circle distance between two points, with
	 * the Haversine formula.
	 * @param float $latitudeFrom Latitude of start point in [deg decimal]
	 * @param float $longitudeFrom Longitude of start point in [deg decimal]
	 * @param float $latitudeTo Latitude of target point in [deg decimal]
	 * @param float $longitudeTo Longitude of target point in [deg decimal]
	 * @param float $earthRadius Mean earth radius in [m]
	 * @return float Distance between points in [m] (same as earthRadius)
	 */
	function haversineGreatCircleDistance(
	  $latitudeFrom, $longitudeFrom, $latitudeTo, $longitudeTo, $earthRadius = 6371000)
	{
	  $latFrom = deg2rad($latitudeFrom);
	  $lonFrom = deg2rad($longitudeFrom);
	  $latTo = deg2rad($latitudeTo);
	  $lonTo = deg2rad($longitudeTo);

	  $latDelta = $latTo - $latFrom;
	  $lonDelta = $lonTo - $lonFrom;

	  $angle = 2 * asin(sqrt(pow(sin($latDelta / 2), 2) +
	    cos($latFrom) * cos($latTo) * pow(sin($lonDelta / 2), 2)));
	  return $angle * $earthRadius;
	}

	function random_float ($min,$max) {
    	return ($min + lcg_value()*(abs($max - $min)));
	}

	ob_start();
	require("simulation.php");
	ob_get_clean();
	$x=$_POST['x'];
	$y=$_POST['y'];
	$radius=$_POST['radius'];

	$nearByArea= array();
	for($i=0;$i<sizeof($blocks);$i++){
		if(haversineGreatCircleDistance($x, $y, $blocks[$i]->centroid[0], $blocks[$i]->centroid[1])<$radius){
			array_push($nearByArea, $i);
		}
	}

	$point= array();
	$points= array();
	for($i=0;$i<sizeof($nearByArea);$i++){
		$block = $blocks[$nearByArea[$i]];
		for($j=0;$j<$block->capacity*(1-$block->taken);$j++){
			do{
				$radius = 50;
				$lng_min = $block->centroid[0] - $radius / abs(cos(deg2rad($block->centroid[1])) * 111044.74);
				$lng_max = $block->centroid[0] + $radius / abs(cos(deg2rad($block->centroid[1])) * 111044.74);
				$lat_min = $block->centroid[1] - ($radius / 111044.74);
				$lat_max = $block->centroid[1] + ($radius / 111044.74);

				$point=array(random_float($lng_min, $lng_max), random_float($lat_min, $lat_max));
			}while(haversineGreatCircleDistance($point[0], $point[1], $block->centroid[0], $block->centroid[1])>50);
			array_push($points,$point);
		}
	}


	include_once('dbscan.php');
	unset($nearByArea);
	$point_ids = array();
	for($i=0;$i<sizeof($points);$i++){
		array_push($point_ids, $i);
	}

	$distance_matrix = array();
	for($i=0;$i<sizeof($point_ids);$i++){
		for($j=0;$j<sizeof($point_ids);$j++){
			$distance_matrix[$i][$j] = haversineGreatCircleDistance($points[$i][0], $points[$i][1], $points[$j][0], $points[$j][1]);
		}
		
	}


	$dbscan = new DBSCAN($distance_matrix, $point_ids);
	$clusters = $dbscan->dbscan(30, 3);

	$max_cluster_size = -1;
	for($i=0;$i<sizeof($clusters);$i++){
		if($max_cluster_size <sizeof($clusters[$i])){
			$max_cluster_size = sizeof($clusters[$i]);
		}
	}

	$chosen_clusters = array();
	for($i=0;$i<sizeof($clusters);$i++){
		if(sizeof($clusters[$i]) == $max_cluster_size){
			array_push($chosen_clusters, $clusters[$i]);
		}
	}

	include_once('centroid.php');

	$centroids = array();
	$polygons = new stdClass();
	$polygons->rings = array();
	for($i=0;$i<sizeof($chosen_clusters);$i++){
		$set = array();
		for($j=0;$j<sizeof($chosen_clusters[$i]);$j++){
			array_push($set, $points[$chosen_clusters[$i][$j]]);
		}
		$polygons->rings = array($set);
		$centroids[$i] = getCentroidOfPolygon($polygons);
		$centroids[$i][0]=abs($centroids[$i][0]);
		$centroids[$i][1]=abs($centroids[$i][1]);
	}

	echo json_encode($centroids);

?>