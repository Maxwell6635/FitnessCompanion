<?php
   $servername = "seekt.asia";
   $username = "seektasi_fitness";
   $password = "fitness53300";
   $dbname = "seektasi_bus";
   
   $con = mysqli_connect($servername, $username,$password,$dbname);

   if (!$con) {
     printf("Connect failed: %s\n", mysqli_connect_error());
     exit();
   }
   
   $healthprofileid = $_POST["id"];
   $userID = $_POST["user_id"];
   $weight= $_POST["weight"];
   $BP = $_POST["blood_pressure"];
   $RHR = $_POST["resting_heart_rate"];
   $ArmG = $_POST["arm_girth"];
   $ChestG = $_POST["chest_girth"];
   $CalfG = $_POST["calf_girth"];
   $ThighG = $_POST["thigh_girth"];
   $Waist = $_POST["waist"];
   $HIP = $_POST["hip"];
   $Time = $_POST["created_at"];
   $Update = $_POST["updated_at"];

   $statement = mysqli_prepare($con,"INSERT INTO `Health_Profile`(`id`, `user_id`, `weight`, `blood_pressure`, `resting_heart_rate`, `arm_girth`, `chest_girth`, `calf_girth`, `waist`, `hip`, `thigh_girth`, `create_at`, `updated_at`)
   VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
   if ($statement) {
     mysqli_stmt_bind_param($statement,"sidiiddddddss",$healthprofileid, $userID, $weight, $BP, $RHR, $ArmG, $ChestG, $CalfG, $ThighG, $Waist, $HIP, $Time, $Update);
    echo(mysqli_stmt_execute($statement));
}
 
   mysqli_stmt_close($statement);
   mysqli_close($con);
  
?>