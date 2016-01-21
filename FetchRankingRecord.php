<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername, $username, $password, $dbname);
   
   $sql = "SELECT * FROM Ranking ORDER BY Ranking.Points DESC";
   
   $res = mysqli_query($con,$sql);
   
   $result = array();
   
  
   while($row = mysqli_fetch_array($res)){
    array_push($result,
    array('id'=>$row[0],
	'userID'=>$row[1],
    'points'=>$row[2],
	'fitnessRecordID'=>$row[3],
	'type'=>$row[4],
	'created_at'=>$row[5],
	'updated_at'=>$row[6]
  ));
}

 
    echo json_encode(array("result"=>$result));
    mysqli_close($con);
?>