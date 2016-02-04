<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername, $username, $password, $dbname);
   
   $userID = $_POST["user_id"];
   
   $sql = "SELECT * FROM Fitness_Record WHERE user_id= '$userID'";
  
   echo(mysqli_stmt_execute($statement));
   $res = mysqli_query($con,$sql);
   
   $result = array();
  
    while($row = mysqli_fetch_array($res)){
    array_push($result, array(
    'id'=>$row[0],
    'userId'=>$row[1],
    'record_distance'=>$row[3],
	'record_duration'=>$row[4],
	'record_calories'=>$row[5],
    'record_step'=>$row[6],
	'average_heart_rate'=>$row[7],
	'created_at'=>$row[8],
    'updated_at'=>$row[9],
	'activity_id'=>$row[10]
  ));
}
    echo json_encode(array("result"=>$result));
    mysqli_close($con);
?>