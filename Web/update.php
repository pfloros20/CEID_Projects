<?php
	echo 'ID: '.$_POST['blockID'].'<br>';
	echo 'Spots: '.$_POST['spots'].'<br>';
	echo 'Dist: '.$_POST['distribution'].'<br>';

	$distribution = "";
	if($_POST['distribution'] == 'center')
		$distribution = 0;
	else if($_POST['distribution'] == "residential")
		$distribution = 1;
	else
		$distribution = 2;

	$db_server["host"] = "localhost";
	$db_server["username"] = "web";
	$db_server["password"] = "web";
	$db_server["database"] = "WEB";

	$mysql_con = mysqli_connect($db_server["host"], $db_server["username"], $db_server["password"], $db_server["database"]);

	$mysql_con->query ('SET CHARACTER SET utf8');
	$mysql_con->query ('SET COLLATION_CONNECTION=utf8_general_ci');

	$update = "UPDATE `BLOCK` SET capacity=".$_POST['spots'].", distribution=".$distribution." WHERE ID=".$_POST['blockID'].";";
	$result = $mysql_con->query($update);
	$mysql_con->close();

	
	header('location: index.php');

?>