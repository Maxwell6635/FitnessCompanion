<?php

   $servername = "localhost";
   $username = "1044722";
   $password = "yusuke6635";
   $dbname = "1044722";

$con = mysqli_connect($servername,$username,$password,$dbname);

/* Check Connection */
if(mysqli_connect_errno()){
	printf("Connect failed: %s\n", mysqli_connect_error());
	exit();
}

 $row_cnt = array();

if($result = mysqli_query($con,"SELECT * FROM User_Profile")){
	$row_cnt["user_id"] = mysqli_num_rows($result);
	
}

echo(json_encode($row_cnt));

mysqli_close($con);
?>