<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername,$username,$password,$dbname);
   
   $sleepDataID = $_POST["id"];
   $userID = $_POST["user_id"];
   $movement = $_POST["movement"];
   $created_at = $_POST["created_at"];
   $updated_at = $_POST["updated_at"];
  
   $statement = mysqli_prepare($con, "INSERT INTO Sleep_Data (id, user_id, movement, created_at, updated_at) Values(?,?,?,?,?)");
   mysqli_stmt_bind_param($statement, "ssiss", $sleepDataID, $userID, $movement, $created_at, $updated_at);

   echo(mysqli_stmt_execute($statement));
   mysqli_stmt_close($statement);
   
   mysqli_close($con);
?>