<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername, $username, $password, $dbname);
  
   if (!$con) {
     printf("Connect failed: %s\n", mysqli_connect_error());
     exit();
   }
   
   $id = $_POST["id"];
   $user_id = $_POST["user_id"];
   $result;
   
   $statement = mysqli_prepare($con,"DELETE FROM Activity_Plan WHERE id = ? AND user_id = ? ");
   mysqli_stmt_bind_param($statement,"si",$id, $user_id);
 
   if (mysqli_stmt_execute($statement)) { 
     $result = 1;
   } else {
     $result = 0;
   }
   
   echo(json_encode($result));

   mysqli_stmt_close($statement);
   mysqli_close($con);
?>