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
   
   $statement = mysqli_prepare($con,"SELECT email FROM User");
   mysqli_stmt_execute($statement);
   
   mysqli_stmt_store_result($statement);
   mysqli_stmt_bind_result($statement, $email);
   
  
   $user = array();
   
   while(mysqli_stmt_fetch($statement)){
	   $user["email"] = $email;
   }
   
    echo(json_encode($user));

    mysqli_stmt_close($statement);
    mysqli_close($con);
?>