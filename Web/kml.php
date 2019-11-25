<?php
	include "Block.php";
	include "centroid.php";

	function mysqlError($result,$mysql_con){
		if (!$result)
			die('Invalid query: ' . $mysql_con->error."\n");
		else
			print_r("Updated records: ".$mysql_con->affected_rows."\n");
	}


	//Upload file from site
	// echo "Upload: " . $_FILES["file"]["name"] . "<br>";
	// echo "Type: " . $_FILES["file"]["type"] . "<br>";
	// echo "Size: " . ($_FILES["file"]["size"] / 1024) . " kB<br>";
	// echo "Temp file: " . $_FILES["file"]["tmp_name"] . "<br>";
	if(move_uploaded_file($_FILES["file"]["tmp_name"],"uploads/".$_FILES["file"]["name"])){
  		//echo "Stored in: uploads/" . $_FILES["file"]["name"];
  		echo "File Uploaded Successfully!\n";

  		//Open file
		$xml=simplexml_load_file("uploads/".$_FILES["file"]["name"]) or die("Error: Cannot create object");
		echo $xml;
		//Array keeping block objects.
		$blocks = array();

		$placemarks = $xml->Document->Folder->Placemark;
		$coordinates = array();
		for($i=0;$i<sizeof($placemarks);$i++){
			if(is_object($placemarks[$i]->MultiGeometry->Polygon->outerBoundaryIs))
				$coordinates = $placemarks[$i]->MultiGeometry->Polygon->outerBoundaryIs->LinearRing->coordinates;

			//For each placemark add a block object and initiate its id.
	        array_push($blocks, new Block);
	        $blocks[$i]->id=$i;
	        //For each coordinates keep sets of x,y as strings in matches.
			preg_match_all('/[0-9]*\.[0-9]*,[0-9]*\.[0-9]*/',$coordinates,$matches);
			//Split set of x,y with "," as the delimiter and insert it to object.
			foreach ($matches[0] as $match) {
	         		$temp = explode(",", $match);
	         		$blocks[$i]->insert($temp[1],$temp[0]);
			}

			$description = $placemarks[$i]->description;
			//If population information exists, add it to object.
			if(preg_match('/.*Population.*/',$description)){
				preg_match_all('/[0-9]+/',$description,$pop);
				$blocks[$i]->population = $pop[0][4];

			}else
				$blocks[$i]->population = 0;
		}

		$polygons = new stdClass();
		$polygons->rings = array();
		for($i=0;$i<sizeof($blocks);$i++){
			$polygons->rings = array($blocks[$i]->points);
			$blocks[$i]->centroid = getCentroidOfPolygon($polygons);
			$blocks[$i]->centroid[0]=abs($blocks[$i]->centroid[0]);
			$blocks[$i]->centroid[1]=abs($blocks[$i]->centroid[1]);
		}

		//Print object array to check results.
		//print_r($blocks);

		$db_server["host"] = "localhost"; //database server
		$db_server["username"] = "web"; // DB username
		$db_server["password"] = "web"; // DB password
		$db_server["database"] = "WEB";// database name

		$mysql_con = mysqli_connect($db_server["host"], $db_server["username"], $db_server["password"], $db_server["database"]);

		$mysql_con->query ('SET CHARACTER SET utf8');
		$mysql_con->query ('SET COLLATION_CONNECTION=utf8_general_ci');

		$delete = "DELETE FROM `BLOCK`;";
		$result = $mysql_con->query($delete);
		$delete = "DELETE FROM `CENTROID`;";
		$result = $mysql_con->query($delete);
		$delete = "DELETE FROM `POINT`;";
		$result = $mysql_con->query($delete);

		$insert_blocks = "INSERT  INTO `BLOCK` VALUES ";
		$insert_centroids = "INSERT  INTO `CENTROID` VALUES ";
		$insert_points = "INSERT  INTO `POINT` VALUES ";

		for($i=0;$i<sizeof($blocks);$i++){
			$block_id = $i +1;
			$capacity = rand(0, $blocks[$i]->population/2);
			$distribution = rand(0,2);
			$insert_blocks = $insert_blocks."(".$block_id.",".$blocks[$i]->population.",".$capacity.",".$distribution."),";

			$insert_centroids = $insert_centroids."(NULL,".$block_id.",".$blocks[$i]->centroid[0].",".$blocks[$i]->centroid[1]."),";

			for($j=0;$j<sizeof($blocks[$i]->points);$j++){
				$insert_points = $insert_points."(NULL,".$block_id.",".$blocks[$i]->points[$j][0].",".$blocks[$i]->points[$j][1]."),";
			}
		}

		$insert_blocks = substr_replace($insert_blocks, "", -1);
		$insert_blocks = $insert_blocks.";";
		$result = $mysql_con->query($insert_blocks);
		mysqlError($result,$mysql_con);


		$insert_centroids = substr_replace($insert_centroids, "", -1);
		$insert_centroids = $insert_centroids.";";
		$result = $mysql_con->query($insert_centroids);
		mysqlError($result,$mysql_con);


		$insert_points = substr_replace($insert_points, "", -1);
		$insert_points = $insert_points.";";
		$result = $mysql_con->query($insert_points);
		mysqlError($result,$mysql_con);

		$mysql_con->close();

	}else
  		echo "File Could Not Be Uploaded!\n";

  	header('refresh:2;url=login.php');
	exit;

?>