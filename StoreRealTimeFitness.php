<?php
   $servername = "mysql11.000webhost.com";
   $username = "a2592164_fitness";
   $password = "a2592164";
   $dbname = "a2592164_fitness";
   
   $con = mysqli_connect($servername, $username,$password,$dbname);
   
   $realtimefitnessID = $_POST["realtimefitnessID"];
   $stepNumber = $_POST["stepNumber"];
   $Time = $_POST["Time"];
  
  
   $statement = mysqli_prepare($con,"INSERT INTO RealTime_Fitness(RealTime_Fitness_ID , Capture_DateTime,Step_Number)Values(?,?,?)");
   mysqli_stmt_bind_param($statement,"ssi",$realtimefitnessID,$Time,$stepNumber);
   mysqli_stmt_execute($statement);
   mysqli_stmt_close($statement);
   
   mysqli_close($con);
?>