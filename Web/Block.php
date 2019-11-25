<?php
	class Block{
		var $id = -1;
		var $population = -1;
		var $centroid = array();
		var $points = array();
		var $capacity =-1;
		var $distribution =-1;
		var $taken = 0.0;

		function insert($a, $b){
			array_push($this->points, array($a, $b));
		}
	}
?>