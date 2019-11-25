<!DOCTYPE html>
<html lang="en">
	<title>Web Project</title>
	<head>	
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<link rel="stylesheet" href="https://unpkg.com/leaflet@1.4.0/dist/leaflet.css"
		   integrity="sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA=="
		   crossorigin=""/>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
		<style>
			#mapid { height: 800px; width: 600px; }

			.topright {
				color: #555555;
				position: absolute;
				top: 8px;
				right: 16px;
				font-size: 18px;
			}
			ul {
				list-style-type: none;
				margin: 0;
				padding: 0;
				overflow: hidden;
				background-color: #333;
			}
			li {
				float: left;
			}
			li a {
				display: block;
				color: white;
				text-align: center;
				padding: 14px 16px;
				text-decoration: none;
			}
			li a:hover {
				background-color: #111;
			}

			input { display: table-cell; }
		</style>
	</head>
	<body>
		<!-- Title -->
		<h1 style="margin-top: 0;margin-bottom: 0;">Web Project</h1>
		<h2 style="margin-top: 0;margin-bottom: 0;margin-left: 5px;font-weight: normal;color: #777777;">Find parking space<br> in real time.</h2>

		<!-- Log In -->
		<?php
			date_default_timezone_set('Europe/Athens');
		 	session_start();
			if(!isset($_SESSION['session_username'])){
				echo '<form class="topright" name="login_form" method="POST" action="login.php">
						<h5 style="margin-top: 0;margin-bottom: 0;font-weight: normal;">Login:</h5>
						<input style="width: 100%;" type="text" name="username" placeholder="Username"><br>
						<input style="width: 100%;" type="password" name="password" placeholder="Password"><br>
						<input style="width: 100%;background-color: #555555;color: #FFFFFF;" type="submit" value="Login">
					</form>';
			}else{
				echo '<div class="topright">
						<h3 style="margin-top: 0;margin-bottom: 0px;font-weight: normal;">Welcome, <b>'.$_SESSION['session_username'].'</b>!</h3>
					</div>
					<a style="top: 35px;" class="topright" href=\'logoff.php\'>Log off</a>';
			}
		?>

		<!-- Navbar -->
		<ul>
			<li><a href="index.php" >Home</a></li>
			<li><a href="index.php" >News</a></li>
			<li><a href="index.php" >Contact</a></li>
			<li><a href="index.php" >About Us</a></li>
		</ul>

		<!-- Hour -->
		<br>
		<?php
		 	session_start();
		 	if(!isset($_SESSION['session_username'])){
		echo '<form method="POST" action="simulation.php" onsubmit="return simulate(getElementById(\'hour\').value, getElementById(\'radius\').value);">';
			}else{
		echo '<form method="POST" action="simulation.php" onsubmit="return simulate(getElementById(\'hour\').value, 500);">';
			}
		
			echo 'Hour: <input type="text" name="hour" id="hour" value="'.date('H').'">';
		?>
			<?php
			 	session_start();
				if(!isset($_SESSION['session_username'])){
					echo 'Radius: <input type="text" name="radius" id="radius" value="500">';
				}
			?>
			<input type="submit" style="background-color: #555555;color: #FFFFFF;" value="Run">
		</form>

		<?php
		 	session_start();
			if(isset($_SESSION['session_username'])){
				echo 	'<br><form action="kml.php" method="post" enctype="multipart/form-data">
							Filename: <input type="file" name="file" id="file"><br />
							<input type="submit" name="submit" style="background-color: #555555;color: #FFFFFF;" value="Submit">
						</form>
						<br><form action="delete.php" method="post" enctype="multipart/form-data">
							<input type="submit" name="Delete Data!" style="background-color: #555555;color: #FFFFFF;" value="Delete Data!">
						</form>';
			}
		?>

		<div id="mapid"></div>
		<script src="https://unpkg.com/leaflet@1.4.0/dist/leaflet.js"
		   integrity="sha512-QVftwZFqvtRNi0ZyCtsznlKSWOStnDORoefr1enyq5mVL4tmKB3S/EnC3rRJcxCPavG10IcrVGSmPh6Qw5lwrg=="
		   crossorigin=""></script>
		<script>
			var data;
			var map;
			var marker;
			var nearby = [];
			var x,y;

			//Make popups
			function bindPopups(data, polygons){
				for(var i=0; i<polygons.length;i++){
					var distribution = "";
					if(data[i].distribution == 0)
						distribution = "Center";
					else if(data[i].distribution == 1)
						distribution = "Residential";
					else
						distribution = "Constant";
					var popup = L.popup().setContent('<form id="form" action="update.php" method="post">\
						 								<input type="hidden" id="blockID" name="blockID" value="'+ data[i].id +'">\
				      									Parking Spots: ' + data[i].capacity + 
				      									'<br><input type="text" name="spots" value="'+ data[i].capacity +'"><br>\
				      										Type: ' + distribution + '<br>'+
				      										'<select style="background-color: #555555;color: #FFFFFF;" id="select" name="distribution"> \
				      											<option value="center">Center</option>\
				      											<option value="residential">Residential</option>\
				      											<option value="constant">Constant\
				      											</option>\
				      										 </select>' + 
				      										'<button style="background-color: #555555;color: #FFFFFF;" type="submit">\
				      											Submit\
				      										</button> ' + 
				      								'</form>');
				    polygons[i].bindPopup(popup);
				}
			}
			//Color polygons according to parking space
			function color(data, polygons){
				for(var i=0; i<polygons.length;i++){
			    	if(data[i].taken<=0.59){
			    		polygons[i].setStyle({fillColor: '#00FF00',weight:0});
			    	}
			    	else if(data[i].taken<=0.84){
			    		polygons[i].setStyle({fillColor: '#FFFF00',weight:0});
			    	}
			    	else if(data[i].taken<=1){
			    		polygons[i].setStyle({fillColor: '#FF0000',weight:0});
			    	}
			    	if(data[i].taken==0.0){
			    		polygons[i].setStyle({fillColor: '#0000FF',weight:0});
			    	}
			    }
			}
			//Fetch and draw polygons from database
			function init() {		
				$.ajax({
					type : "GET",
					url  : "selectPolygons.php",				
    			    async: false,
					success: function(res){  
						data=JSON.parse(res);
					}
				});

				map = L.map('mapid').setView(data[0].points[0], 13);
				L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
				        attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
				    }).addTo(map);

				var polygons = []
				for(var i=0; i<data.length;i++){
					polygons[i] = L.polygon(data[i].points).addTo(map);
					polygons[i].setStyle({fillColor: '#000000',weight:0});

				}
				bindPopups(data, polygons);      	
			}

			<?php
			 	session_start();
				if(isset($_SESSION['session_username'])){
					echo 'init();';
				}else{
					echo'simulate('.date('H').', 500);';
				}
			?>

		    function simulate(hour, radius) {
				$.ajax({
					type : "POST",
					url  : "simulation.php",				
    			    async: false,
					data : {hour : hour},
					success: function(res){  
						data=JSON.parse(res);
					}
				});

				if(map != null)
					map.remove();
				map = L.map('mapid').setView(data[0].points[0], 13);
				L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
				        attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
				    }).addTo(map);

				<?php
				 	session_start();
					if(!isset($_SESSION['session_username'])){
						echo 'map.on(\'click\', function(ev){
								var latlng = map.mouseEventToLatLng(ev.originalEvent);
								x= latlng.lat;
								y= latlng.lng;
								console.log(x + \', \' + y);
								if(marker != null){
									map.removeLayer(marker);
									for(var i=0; i<nearby.length;i++){
										map.removeLayer(nearby[i]);
									}
									nearby = [];
								}

								marker = L.marker([x, y]).setOpacity(0.7).addTo(map);
								$.ajax({
									type : "POST",
									url  : "findLocation.php",
									data : {hour : hour, radius : radius, x : x, y : y},
									success: function(res){  
										console.log(res);
										var spots = JSON.parse(res);
										for(var i=0; i<spots.length;i++){
											nearby[i] = L.marker([spots[i][0], spots[i][1]]).addTo(map);
										}
									}
								});
							});';
					}
				?>
				var polygons = [];
				for(var i=0; i<data.length;i++){
					polygons[i] = L.polygon(data[i].points).addTo(map);
			    }
				color(data, polygons);
				<?php
				 	session_start();
					if(isset($_SESSION['session_username'])){
						echo 'bindPopups(data, polygons);';
					}
				?>
			    return false;
			}
		</script>
	</body>
</html>