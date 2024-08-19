// prüft ob auf der RecoverPasswordView ob es eine gültige E-Mail-Adresse ist und beim neuen Passwort setzen ob die Passwörter die Bedingungen erfüllen

function checkEmailForRecovery() {
	var email = document.getElementById("recoverEmail").value;
	var failMsg = "";
	var failField = document.getElementById('recoverEmailMsg');
	if(!email || email.indexOf('@') < 0 || email.indexOf('.') < 0) 	failMsg = "Bitte geben Sie eine gültige E-Mail Adresse ein."; 	
	failField.innerText = failMsg;
	if(failMsg) return false;
	else return true;	
}

function checkPasswordToRecover() {
	var password = document.getElementById("recoverPassword").value;
	var passwordCheck = document.getElementById("recoverPasswordCheck").value;
	var failMsg = "";
	var failField = document.getElementById('recoverPasswordMsg');
	if(password != passwordCheck) failMsg = "Die Passwörter müssen übereinstimmen."; 	
	else if(password.length < 8)	failMsg = "Das Passwort muss mindestens 8 Zeichen lang sein."; 	
	else if(!(/[A-Z]/.test(password)) || !(/\d/.test(password))) failMsg = "Das Passwort muss mindestens einen Großbuchstaben sowie eine Zahl beinhalten."
	failField.innerText = failMsg;
	if(failMsg) return false;
	else return true;	
}