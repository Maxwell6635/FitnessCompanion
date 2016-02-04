<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername, $username, $password, $dbname);
   
   $sql = "SELECT * FROM Activity_Plan";
   
   $res = mysqli_query($con,$sql);
   
   $result = array();
  
   while($row = mysqli_fetch_array($res)){
    array_push($result, array(
    'id'=>$row[0],
    'user_id'=>$row[1],
    'type'=>$row[2],
	'name'=>$row[3],
	'description'=>$row[4],
	'estimate_calories'=>$row[5],
	'duration'=>$row[6],
	'created_at'=>$row[7],
	'updated_at'=>$row[8],
	'trainer_id'=>$row[9]
  ));
}
    echo json_encode(array("result"=>$result));
    mysqli_close($con);
?>