<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername, $username, $password, $dbname);
   
   $sql = "SELECT * FROM Event";
   $res = mysqli_query($con,$sql);
   
   $result = array();
  
   while($row = mysqli_fetch_array($res)){
    array_push($result, array(
    'id'=>$row[0],
    'banner'=>mysqli_real_escape_string(base64_encode($row[1])),
    'url'=>$row[2],
	'created_at'=>$row[3],
	'updated_at'=>$row[4]
  ));
}
    echo json_encode(array("result"=>$result));
    mysqli_close($con);
?>