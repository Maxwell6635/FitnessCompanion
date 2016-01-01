<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername,$username,$password,$dbname);
   
   $fitnessRecordID = $_POST["id"];
   $userID = $_POST["user_id"];
   $activitiesID = $_POST["activities_id"];
   $recordDuration = $_POST["record_duration"];
   $recordDistance = $_POST["record_distance"];
   $recordCalories = $_POST["record_calories"];
   $recordStep = $_POST["record_step"];
   $HR = $_POST["average_heart_rate"];
   $createdAt = $_POST["created_at"];
   $updateAt = $_POST["created_at"];
   
  
   $statement = mysqli_prepare($con,"INSERT INTO Fitness_Record (id,user_id,record_distance,record_duration,record_calories,record_step,average_heart_rate,created_at,updated_at,activity_id)
                    Values(?,?,?,?,?,?,?,?,?,?)");
   mysqli_stmt_bind_param($statement,"siiididssi",                                   $fitnessRecordID,$userID,$recordDistance,$recordDuration,$recordCalories,$recordStep,$HR,$createdAt,$updateAt,$fitnessActivity);

   mysqli_stmt_execute($statement);
   mysqli_stmt_close($statement);
   
   mysqli_close($con);
?>