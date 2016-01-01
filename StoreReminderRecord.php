<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername,$username,$password,$dbname);
   
   $reminderID = $_POST["id"];
   $userID = $_POST["user_id"];
   $repeats = $_POST["repeats"];
   $time = $_POST["time"];
   $day = $_POST["day"];
   $date = $_POST["date"];
   $availability = $_POST["availability"];
   $created_at = $_POST["createdAt"];
   $updateAt = $_POST["updateAt"];
   $activity_id = $_POST["activity_id"];
   
  
   $statement = mysqli_prepare($con,"INSERT INTO Reminder (id, user_id, repeats, time, day, date, availability, created_at, updated_at, activity_id)
                    Values(?,?,?,?,?,?,?,?,?,?)");
   mysqli_stmt_bind_param($statement,"sisssiisss", $reminderID, $userID, $repeats, $time, $day, $date, $availability, $created_at, $updateAt, $activity_id);

   echo(mysqli_stmt_execute($statement));
   mysqli_stmt_close($statement);
   
   mysqli_close($con);
?>