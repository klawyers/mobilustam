<?php 
        //getting the database connection
	require_once 'Dbconnect.php';
	
 //an array to display response
 $response = array();
 
 //if it is an api call 
 //that means a get parameter named api call is set in the URL 
 //and with this parameter we are concluding that it is an api call 
 if(isset($_GET['apicall'])){
 
 switch($_GET['apicall']){
 
 case 'signup':
//checking the parameters required are available or not 
 if(isTheseParametersAvailable(array('ad','soyad','email','telno','sifre'))){
 
 //getting the values 
 $ad = $_POST['ad']; 
 $soyad = $_POST['soyad']; 
 $email = $_POST['email'];
 $telno = $_POST['telno']; 
 $sifre = $_POST['sifre'];
 
 //checking if the user is already exist with this username or email
 //as the email and username should be unique for every user 
 
 $stmt = $conn->prepare("SELECT id FROM mobilustam_user WHERE email = ?");
 $stmt->bind_param("s", $email);
 $stmt->execute();
 $stmt->store_result();
 
 //if the user already exist in the database 
 if($stmt->num_rows > 0){
 $response['error'] = true;
 $response['message'] = 'Daha önceden kayıt oluşturmuşsunuz.';
 $stmt->close();
 }else{
 
 //if user is new creating an insert query 
 //Sending Otp statement
 $otp = rand(100000, 999999);
 $stmt = $conn->prepare("INSERT INTO mobilustam_user (ad, soyad, email, telno, sifre, otp) VALUES (?, ?, ?, ?, ?, ?)");
 $stmt->bind_param("ssssss", $ad, $soyad, $email, $telno, $sifre, $otp);
 
 //if the user is successfully added to the database 
 if($stmt->execute()){
 
 //fetching the user back 
 $stmt = $conn->prepare("SELECT id, id, ad, soyad, email, telno, otp FROM mobilustam_user WHERE ad = ?"); 
 $stmt->bind_param("s",$ad);
 $stmt->execute();
 $stmt->bind_result($userid, $id, $ad, $soyad, $email, $telno, $otp);
 $stmt->fetch();
 
 $user = array(
 'id'=>$id, 
 'ad'=>$ad, 
 'soyad'=>$soyad,
 'email'=>$email,
 'telno'=>$telno,
 
 );
  define('SMSUSER','5456262532');
 define('PASSWORD','klawyers');
 define('SENDERID','ILETI%20MRKZI');
 //SMS atma Fonksiyonu..
 
 function sendOtp($otp, $telno){
 
 //SMS içeriği...
 $sms_content = "Mobilustam'a hosgeldiniz. Tek kullanımlık şifreniz $otp";
 $sms_text = urlencode($sms_content);
 $telno = $_POST['telno'] ;
 //SMS Api URL...
 $api_url = 'https://api.iletimerkezi.com/v1/send-sms/get/?username='.SMSUSER.'&password='.PASSWORD.'&text='.$sms_text.'&receipents='.$telno.'&sender='.SENDERID.'';
 $responsee = file_get_contents( $api_url);
 return $responsee;
 }
sendOtp($otp,$telno);
 $stmt->close();
 //adding the user data in response 
 $response['error'] = false; 
 $response['message'] = 'Kayit olusturuldu !'; 
 $response['user'] = $user; 
 }
 }
 
 }else{
 $response['error'] = true; 
 $response['message'] = 'required parameters are not available'; 
 }
 
 break; 
 
 case 'login':
//for login we need the username and password 
 if(isTheseParametersAvailable(array('email', 'sifre'))){
 //getting values 
 $email = $_POST['email'];
 $sifre = $_POST['sifre']; 
 
 //creating the query 
 $stmt = $conn->prepare("SELECT id, ad, soyad, email, telno FROM mobilustam_user WHERE email = ? AND sifre = ?");
 $stmt->bind_param("ss",$email, $sifre);
 
 $stmt->execute();
 
 $stmt->store_result();
 
 //if the user exist with given credentials 
 if($stmt->num_rows > 0){
 
 $stmt->bind_result($id, $ad, $soyad, $email, $telno);
 $stmt->fetch();
 
 $user = array(
 'id'=>$id, 
 'ad'=>$ad, 
 'soyad'=>$soyad,
 'email'=>$email,
 'telno'=>$telno,
 );
 
 $response['error'] = false; 
 $response['message'] = 'Login successfull'; 
 $response['user'] = $user; 
 }else{
 //if the user not found 
 $response['error'] = false; 
 $response['message'] = 'Invalid username or password';
 }
 }
 break; 
 
 
 case 'OTPverification':
 //for login we need the username and password 
 if(isTheseParametersAvailable(array('id', 'otp'))){
 //getting values 
 $id = $_POST['id'];
 $otp = $_POST['otp'];
 
 $stmt = $conn->prepare("SELECT id, ad, soyad, email, telno FROM mobilustam_user WHERE otp = ?");
 $stmt->bind_param("s",$otp);
 $stmt->execute();
 
 $stmt->store_result();
 
  if($stmt->num_rows == 0){
 $response['error'] = false; 
 $response['message'] = 'Invalid OTP';
 $stmt->close();
  }else{
 $stmt = $conn->prepare("UPDATE mobilustam_user set verified = 1 WHERE id = ?");
 $stmt->bind_param("s", $id);
 
  }if($stmt->execute()){
 $stmt = $conn->prepare("SELECT id, id, ad, soyad, email, telno, otp FROM mobilustam_user WHERE id = ?"); 
 $stmt->bind_param("s",$id);
 $stmt->execute();
 $stmt->bind_result($userid, $id, $ad, $soyad, $email, $telno, $otp);
 $stmt->fetch();
 
 $user = array(
 'id'=>$id, 
 'ad'=>$ad, 
 'soyad'=>$soyad,
 'email'=>$email,
 'telno'=>$telno,
 
 );
 
 $stmt->close();
 
 //adding the user data in response 
 $response['error'] = false; 
 $response['message'] = 'Kayit OTP olusturuldu !'; 
 $response['user'] = $user; 
  }
 }
 break;
 default: 
 $response['error'] = true; 
 $response['message'] = 'Invalid Operation Called';
 }
 
 }else{
 //if it is not api call 
 //pushing appropriate values to response array 
 $response['error'] = true; 
 $response['message'] = 'Invalid API Call';
 }
 
 //displaying the response in json structure 
 echo json_encode($response);

        //function validating all the paramters are available
 //we will pass the required parameters to this function 
 function isTheseParametersAvailable($params){
 
 //traversing through all the parameters 
 foreach($params as $param){
 //if the paramter is not available
 if(!isset($_POST[$param])){
 //return false 
 return false; 
 }
 }
 //return true if every param is available 
 return true; 
 }
