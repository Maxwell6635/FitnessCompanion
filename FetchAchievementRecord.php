<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername, $username, $password, $dbname);
   
   $userID = $_POST["user_id"];
   
   
   $sql = "SELECT * FROM Achievement Where user_id = '$userID'";
   $res = mysqli_query($con,$sql);
   
   $result = array();
  
   while($row = mysqli_fetch_array($res)){
    array_push($result, array(
    'id'=>$row[0],
    'user_id'=>$row[1],
    'milestones_name'=>$row[2],
	'milestones_result'=>$row[3],
	'created_at'=>$row[4],
	'updated_at'=>$row[5]
  ));
}
    echo json_encode(array("result"=>$result));
    mysqli_close($con);
?>