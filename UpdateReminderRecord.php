<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername,$username,$password,$dbname);
   
   $reminderID = $_POST["id"];
   $userID = $_POST["user_id"];
   $availability = $_POST["availability"];
   $activity_id = $_POST["activity_id"];
   $repeats = $_POST["repeat"];
   $time = $_POST["time"];
   $day = $_POST["day"];
   $date = $_POST["date"];
   $created_at = $_POST["created_at"];
   $updated_at = $_POST["updated_at"];

   $statement = mysqli_prepare($con,"UPDATE Reminder Set availability = '$availability'  , 
														 activity_id = '$activity_id' 
														 repeats = '$repeats' , 
                                                         time = ' $time' , 
														 day = '$day' , 
														 date = '$date' ,
														 created_at = '$created_at' , 
														 updated_at = '$updateAt' ,  
									                  WHERE id = '$reminderID' AND user_id = '$userID' ");

   mysqli_stmt_execute($statement);
   mysqli_stmt_close($statement);
   
   mysqli_close($con);
?>