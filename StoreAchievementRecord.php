<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername,$username,$password,$dbname);
   
   $achievementID = $_POST["id"];
   $userID = $_POST["user_id"];
   $milestoneName = $_POST["milestoneName"];
   $milestoneResult = $_POST["milestoneResult"];
   $created_at = $_POST["createdAt"];
   $updateAt = $_POST["updateAt"];
  
  
   $statement = mysqli_prepare($con,"INSERT INTO Achievement (id, user_id, milestones_name, milestones_result, created_at, updated_at)
                    Values(?,?,?,?,?,?)");
   mysqli_stmt_bind_param($statement,"sisiss", $achievementID, $userID, $milestoneName, $milestoneResult, $created_at, $updateAt);

   echo(mysqli_stmt_execute($statement));
   mysqli_stmt_close($statement);
   
   mysqli_close($con);
?>
