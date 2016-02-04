<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername, $username,$password,$dbname);
   
   $userID = $_POST["userID"];
   $gcm_regid = $_POST["gcmID"];
   
   $statement = mysqli_prepare($con, "UPDATE User Set gcm_regid = '$gcm_regid' WHERE id = '$userID' ");
   
   mysqli_stmt_execute($statement);
   mysqli_stmt_close($statement);
   
   mysqli_close($con);
?>