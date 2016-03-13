<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername, $username, $password, $dbname);
   
   $userID = $_POST["user_id"];
   
   $sql = "SELECT * FROM Goal WHERE user_id= '$userID'";
  
   echo(mysqli_stmt_execute($statement));
   $res = mysqli_query($con,$sql);
   
   $result = array();
  
    while($row = mysqli_fetch_array($res)){
    array_push($result, array(
    'id'=>$row[0],
    'userId'=>$row[1],
    'goal_desc'=>$row[2],
    'goal_duration'=>$row[3],
    'goal_target'=>$row[4],
    'goal_done'=>$row[5],
    'created_at'=>$row[6],
    'updated_at'=>$row[7] 
  ));
}
    echo json_encode(array("result"=>$result));
    mysqli_close($con);
?>