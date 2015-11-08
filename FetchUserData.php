<?php
    $servername = "localhost";
   $username = "1044722";
   $password = "yusuke6635";
   $dbname = "1044722";
   
   $con = mysqli_connect($servername, $username, $password, $dbname);
   
  
   $email = $_POST["email"];
   $password = $_POST["password"];
   
   $statement = mysqli_prepare($con,"SELECT * FROM User_Profile WHERE User_Email = ? AND Password = ? ");
   mysqli_stmt_bind_param($statement,"ss",$email,$password);
   mysqli_stmt_execute($statement);
   
   
   mysqli_stmt_store_result($statement);
   mysqli_stmt_bind_result($statement,$id,$email,$password,$name,$dob,$age,$gender,$height,$weight,$doj,$reward);
   
  
   $user = array();
   
   while(mysqli_stmt_fetch($statement)){
	   $user["id"] = $id;
	   $user["email"] = $email;
	   $user["password"] = $password;
	   $user["name"] = $name;
	   $user["dob"] = $dob;
	   $user["age"] = $age;
	   $user["gender"] = $gender;
	   $user["height"] = $height;
	   $user["weight"] = $weight;
	   $user["doj"] = $doj;
	   $user["reward"] = $reward;
   }
   
    echo(json_encode($user));
  
    mysqli_close($con);
?>