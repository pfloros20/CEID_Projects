<?php
	include "Block.php";
	$db_server["host"] = "localhost"; //database server
	$db_server["username"] = "web"; // DB username
	$db_server["password"] = "web"; // DB password
	$db_server["database"] = "WEB";// database name

	$mysql_con = mysqli_connect($db_server["host"], $db_server["username"], $db_server["password"], $db_server["database"]);
	$mysql_con->query ('SET CHARACTER SET utf8');
	$mysql_con->query ('SET COLLATION_CONNECTION=utf8_general_ci');

	$query = "SELECT BLOCK.ID,population,capacity,distribution,POINT.x AS px,POINT.y AS py,CENTROID.x AS cx,CENTROID.y AS cy FROM BLOCK INNER JOIN POINT ON BLOCK.ID=POINT.BLOCK_ID INNER JOIN CENTROID ON BLOCK.ID=CENTROID.BLOCK_ID";

	$result = $mysql_con->query($query);
	$blocks = array();
	$previous_block_id=-1;
	while($row = $result->fetch_array())
	{
		$i=$row['ID'];
		if($previous_block_id!=$i){
			array_push($blocks, new Block());
			$previous_block_id=$i;
		}
		$blocks[$i-1]->insert($row['px'], $row['py']);
		$blocks[$i-1]->population=$row['population'];
		$blocks[$i-1]->distribution=$row['distribution'];
		$blocks[$i-1]->capacity=$row['capacity'];
		$blocks[$i-1]->id=$row['ID'];
		$blocks[$i-1]->centroid=array($row['cx'],$row['cy']);
	}
	//echo $blocks;
	echo json_encode($blocks);
	$mysql_con->close();
?>
