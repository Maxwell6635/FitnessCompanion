<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername, $username, $password, $dbname);
   
   $userID = $_POST["user_id"];
   
   $sql = "SELECT * FROM Realtime_Fitness WHERE user_id= '$userID'";
  
   echo(mysqli_stmt_execute($statement));
   $res = mysqli_query($con,$sql);
   
   $result = array();
  
   while($row = mysqli_fetch_array($res)){
    array_push($result, array(
    'id'=>$row[0],
    'userId'=>$row[1]),
    'capture_datetime'=>$row[2],
	'step_number'=>$row[3]
  ));
}
    echo json_encode(array("result"=>$result));
    mysqli_close($con);
?>