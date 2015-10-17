<?php
   $servername = "mysql11.000webhost.com";
   $username = "a2592164_fitness";
   $password = "a2592164";
   $dbname = "a2592164_fitness";
   
   $con = mysqli_connect($servername, $username,$password,$dbname);
   
   $fitnessRecordID = $_POST["fitnessRecordID"];
   $userID = $_POST["userID"];
   $fitnessActivity = $_POST["fitnessActivity"];
   $recordDuration = $_POST["recordDuration"];
   $recordDistance = $_POST["recordDistance"];
   $recordCalories = $_POST["recordCalories"];
   $recordStep = $_POST["recordStep"];
   $HR = $_POST["HR"];
   $Time = $_POST["Time"];
  
   
  
   $statement = mysqli_prepare($con,"INSERT INTO Fitness_Record (Fitness_Record_ID,User_ID,Fitness_Activity,Record_Duration,Record_Distance,Record_Calories,Record_Step,Average_Heart_Rate,Fitness_Record_DateTime)Values(?,?,?,?,?,?,?,?,?)");
   mysqli_stmt_bind_param($statement,"sisiiiiss",$fitnessRecordID,$userID,$fitnessActivity,$recordDuration,$recordDistance,$recordCalories,$recordStep,$HR,$Time);
   mysqli_stmt_execute($statement);
   mysqli_stmt_close($statement);
   
   mysqli_close($con);
?>