<?php
 	session_start();
	if(!isset($_SESSION['session_username'])){
		header("location: index.php");
		exit();
	}

	function mysqlError($result,$mysql_con){
		if (!$result)
			die('Invalid query: ' . $mysql_con->error."\n");
		else
			print_r("Updated records: ".$mysql_con->affected_rows."\n");
	}

	$db_server["host"] = "localhost"; //database server
	$db_server["username"] = "root"; // DB username
	$db_server["password"] = "bitch1please"; // DB password
	$db_server["database"] = "WEB";// database name

	$mysql_con = mysqli_connect($db_server["host"], $db_server["username"], $db_server["password"], $db_server["database"]);

	$mysql_con->query ('SET CHARACTER SET utf8');
	$mysql_con->query ('SET COLLATION_CONNECTION=utf8_general_ci');

	$delete = "DELETE FROM `BLOCK`;";
	$result = $mysql_con->query($delete);
	mysqlError($result,$mysql_con);
	$delete = "DELETE FROM `CENTROID`;";
	$result = $mysql_con->query($delete);
	mysqlError($result,$mysql_con);
	$delete = "DELETE FROM `POINT`;";
	$result = $mysql_con->query($delete);
	mysqlError($result,$mysql_con);
	
	$mysql_con->close();
	header('refresh:1;url=login.php');
	exit;
?>