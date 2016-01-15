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
   
   $email = $_POST["email"];
   $password = $_POST["password"];
   
   $statement = mysqli_prepare($con,"SELECT * FROM User WHERE email = ? AND password = ? ");
   mysqli_stmt_bind_param($statement,"ss",$email,$password);
   mysqli_stmt_execute($statement);
   
   
   mysqli_stmt_store_result($statement);
   mysqli_stmt_bind_result($statement,$id,$name,$email,$password,$gender,$dob,$weight,$height,$image,$reward,$doj,$update,$token);
   
  
   $user = array();
   
   while(mysqli_stmt_fetch($statement)){
	   $user["id"] = $id;
	   $user["name"] = $name;
	   $user["email"] = $email;
	   $user["password"] = $password;
	   $user["dob"] = $dob;
	   $user["gender"] = $gender;
	   $user["height"] = $height;
	   $user["weight"] = $weight;
	   $user["doj"] = $doj;	 
       $user["update"] = $update;
       $user["image"] = base64_encode($image);
	   $user["reward"] = $reward;
   }
   
    echo(json_encode($user));

    mysqli_stmt_close($statement);
    mysqli_close($con);
?>