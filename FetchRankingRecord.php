<?php
    $servername = "localhost";
   $username = "1044722";
   $password = "yusuke6635";
   $dbname = "1044722";
   
   $con = mysqli_connect($servername, $username, $password, $dbname);
   
  
   
   $statement = mysqli_prepare($con,"SELECT * FROM Ranking");
   mysqli_stmt_execute($statement);
   
   
   mysqli_stmt_store_result($statement);
   mysqli_stmt_bind_result($statement,$ranking_no,$name,$points);
   
  
   $ranking = array();
   
   while(mysqli_stmt_fetch($statement)){
	   $ranking["ranking_no"] = $ranking_no;
	   $ranking["name"] = $name;
	   $ranking["points"] = $points;
   }
   
    echo(json_encode($ranking));
    mysqli_close($con);
?>