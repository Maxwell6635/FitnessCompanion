<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername, $username, $password, $dbname);
   
  
   $user_id = $_POST["id"];
 
   $statement = mysqli_prepare($con,"SELECT * FROM Health_Profile WHERE user_id = ?");
   mysqli_stmt_bind_param($statement,"s",$user_id);
   mysqli_stmt_execute($statement);
   
   
   mysqli_stmt_store_result($statement);
   mysqli_stmt_bind_result($statement,$health_profile_id,$user_id,$weight,$blood_pressure,$resting_heartrate,$arm_girth,$chest_girth,$calf_girth
                           ,$thigh_girth,$waist,$hip,$created_at,$updated);
   
  
   $health = array();
   $record = array();
   
   while(mysqli_stmt_fetch($statement)){
	   $health["health_profile_id"] = $health_profile_id;
	   $health["id"] = $user_id;
	   $health["weight"] = $weight;
	   $health["blood_pressure"] = $blood_pressure;
	   $health["resting_heartrate"] = $resting_heartrate;
	   $health["arm_girth"] = $arm_girth;
	   $health["chest_girth"] = $chest_girth;
	   $health["calf_girth"] = $calf_girth;
	   $health["thigh_girth"] = $thigh_girth;
	   $health["waist"] = $waist;
	   $health["hip"] = $hip;
	   $record[] = $health;
   }
   
    echo(json_encode($record));
  
    mysqli_close($con);
?>