<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername,$username,$password,$dbname);
   
   $fitnessRecordID = $_POST["id"];
   $userID = $_POST["user_id"];
   $goal_desc = $_POST["goal_desc"];
   $goal_duration = $_POST["goal_duration"];
   $goal_target = $_POST["goal_target"];
   $createdAt = $_POST["createdAt"];
   $updateAt = $_POST["updateAt"];
   
  
   $statement = mysqli_prepare($con,"INSERT INTO Goal (id, user_id, goal_desc, goal_duration, goal_target, created_at, updated_at)
                    Values(?,?,?,?,?,?,?)");
   mysqli_stmt_bind_param($statement,"sisiiss", $fitnessRecordID, $userID, $goal_desc, $goal_duration, $goal_target, $createdAt, $updateAt);

   echo(mysqli_stmt_execute($statement));
   mysqli_stmt_close($statement);
   
   mysqli_close($con);
?>