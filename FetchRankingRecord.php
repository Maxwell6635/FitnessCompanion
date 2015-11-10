<?php
    $servername = "localhost";
   $username = "1044722";
   $password = "yusuke6635";
   $dbname = "1044722";
   
   $con = mysqli_connect($servername, $username, $password, $dbname);
   
   $sql = "SELECT * FROM Ranking";
   
   $res = mysqli_query($con,$sql);
   
   $result = array();
  
   while($row = mysqli_fetch_array($res)){
    array_push($result,
    array('ranking_no'=>$row[0],
    'name'=>$row[1],
    'points'=>$row[2]
  ));
}

 
    echo json_encode(array("result"=>$result));
    mysqli_close($con);
?>