<?php
	ob_start();
	require("selectPolygons.php");
	ob_get_clean();
	$hour = $_POST['hour'];
	if($hour==24)
		$hour=0;
	//print_r($blocks);

	$center = [0.75,0.55,0.46,0.19,0.2,0.2,0.39,0.55,0.67,0.8,0.95,0.9,0.95,0.9,0.88,0.83,0.7,0.62,0.74,0.8,0.8,0.95,0.92,0.76];
	$resident = [0.69,0.71,0.73,0.68,0.69,0.7,0.67,0.55,0.49,0.43,0.34,0.45,0.48,0.53,0.5,0.56,0.73,0.41,0.42,0.48,0.54,0.6,0.72,0.66];
	$constant = [0.18,0.17,0.21,0.25,0.22,0.17,0.16,0.39,0.54,0.77,0.78,0.83,0.78,0.78,0.8,0.76,0.78,0.79,0.84,0.57,0.38,0.24,0.19,0.23];
	// for($i=0;$i<24;$i++){
	// 	array_push($distribution, rand(0,100)/100);
	// }
	//print_r($distribution);
	for($i=0;$i<sizeof($blocks);$i++){
		if($blocks[$i]->distribution == 0)
			$distribution = $center;
		else if($blocks[$i]->distribution == 1)
			$distribution = $residential;
		else
			$distribution = $constant;

		if($blocks[$i]->capacity == 0)
			$blocks[$i]->taken=1;
		else
			$blocks[$i]->taken=0.2*$blocks[$i]->population/$blocks[$i]->capacity + $distribution[$hour];

		if($blocks[$i]->taken>1)
			$blocks[$i]->taken=1;
	}
	echo json_encode($blocks);
?>
