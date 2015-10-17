<?php
   $servername = "mysql11.000webhost.com";
   $username = "a2592164_fitness";
   $password = "a2592164";
   $dbname = "a2592164_fitness";
   
   $con = mysqli_connect($servername, $username,$password,$dbname);
   
   $email = $_POST["email"];
   $password = $_POST["password"];
   $name = $_POST["name"];
   $dob = $_POST["dob"];
   $age = $_POST["age"];
   $gender = $_POST["gender"];
   $height = $_POST["height"];
   $weight = $_POST["weight"];
   $doj = $_POST["doj"];
   $reward = $_POST["reward"];
   
   
   $statement = mysqli_prepare($con,"INSERT INTO User_Profile (User_Email,Password,Name,Date_Of_Birth,Age,Gender,Initial_Weight,Height,Date_Of_Join,Reward_Point)Values(?,?,?,?,?,?,?,?,?,?)");
   mysqli_stmt_bind_param($statement,"ssssissssi",$email,$password,$name,$dob,$age,$gender,$height,$weight,$doj,$reward);
   mysqli_stmt_execute($statement);
   mysqli_stmt_close($statement);
   
   mysqli_close($con);
?>