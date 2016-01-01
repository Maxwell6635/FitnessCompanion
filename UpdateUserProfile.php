<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername, $username,$password,$dbname);
   
   $userID = $_POST["userID"];
   $email = $_POST["email"];
   $password = $_POST["password"];
   $name = $_POST["name"];
   $dob = $_POST["dob"];
   $gender = $_POST["gender"];
   $height = $_POST["height"];
   $weight = $_POST["initial_weight"];
   $image = $_POST["image"];
   $doj = $_POST["doj"];
   $reward = $_POST["reward_point"];
   $created_at = $_POST["created_at"];

   
   $statement = mysqli_prepare($con,
   "UPDATE User Set name = '$name' , email = '$email' , password = '$password' , gender = '$gender' , dob = '$dob', initial_weight = '$weight' , height = '$height'
                            , image = '$image', reward_point = '$reward' , created_at = '$created_at' WHERE id = '$userID' ");
   
   mysqli_stmt_execute($statement);
   mysqli_stmt_close($statement);
   
   mysqli_close($con);
?>