<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername, $username, $password, $dbname);
   
   $fitnessRecordID = $_POST["fitnessRecordID"];
   $userID = $_POST["userID"];
   
   $statement = mysqli_prepare($con,"SELECT * FROM Fitness_Record WHERE id = ? AND user_id = ? ");
   mysqli_stmt_bind_param($statement,"si", $fitnessRecordID, $userID);
   mysqli_stmt_execute($statement);
   
   
   mysqli_stmt_store_result($statement);
   mysqli_stmt_bind_result($statement,$id, $userID, $record_distance, $record_duration, $record_calories, $record_step
                            , $average_heart_rate, $created_at, $updated_at, $activity_id);
   
  
   $fitness = array();
   
   while(mysqli_stmt_fetch($statement)){
	   $fitness["id"] = $id;
	   $fitness["userID"] = $userID;
	   $fitness["record_distance"] = $record_distance;
	   $fitness["record_duration"] = $record_duration;
	   $fitness["record_calories"] = $record_calories;
	   $fitness["record_step"] = $record_step;
	   $fitness["average_heart_rate"] = $average_heart_rate;
	   $fitness["created_at"] = $created_at;
	   $fitness["updated_at"] = $updated_at;	 
       $fitness["activity_id"] = $activity_id;
   }
   
    echo(json_encode($fitness));

    mysqli_stmt_close($statement);
    mysqli_close($con);
?>
?>