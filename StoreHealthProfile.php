<?php
   $servername = "mysql11.000webhost.com";
   $username = "a2592164_fitness";
   $password = "a2592164";
   $dbname = "a2592164_fitness";
   
   $con = mysqli_connect($servername, $username,$password,$dbname);
   
   $healthprofileid = $_POST["healthprofileid"];
   $userID = $_POST["userID"];
   $weight= $_POST["weight"];
   $BP = $_POST["BP"];
   $RHR = $_POST["RHR"];
   $ArmG = $_POST["ArmG"];
   $ChestG = $_POST["ChestG"];
   $CalfG = $_POST["CalfG"];
   $ThighG = $_POST["ThighG"];
   $Time = $_POST["Time"];
   $Waist = $_POST["Waist"];
   $HIP = $_POST["HIP"];
   
   $statement = mysqli_prepare($con,"INSERT INTO Health_Profile (Health_Profile_ID,User_ID,Weight,Blood_Pressure,Resting_Heart_Rate,Arm_Girth,Chest_Girth,Calf_Girth,Thigh_Girth,Waist,HIP,Record_DateTime)Values(?,?,?,?,?,?,?,?,?,?,?,?)");
   mysqli_stmt_bind_param($statement,"sisiisssssss",$healthprofileid, $userID,$weight,$BP,$RHR,$ArmG,$ChestG,$CalfG,$ThighG, $Waist,$HIP,$Time);
   mysqli_stmt_execute($statement);
   mysqli_stmt_close($statement);
   
   mysqli_close($con);
?>