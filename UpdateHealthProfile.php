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
   
   $statement = mysqli_prepare($con,"UPDATE Health_Profile Set Weight = '$weight' ,Blood_Pressure = '$BP' ,Resting_Heart_Rate = '$RHR' , Arm_Girth = '$ArmG' , Chest_Girth = ' $ChestG', Calf_Girth = '$CalfG' , Thigh_Girth = '$ThighG' , Waist = '$Waist', HIP = '$HIP' ,Record_DateTime = '$Time' WHERE Health_Profile_ID = '$healthprofileid' AND User_ID = '$userID' ");
   
   mysqli_stmt_execute($statement);
   mysqli_stmt_close($statement);
   
   mysqli_close($con);
?>