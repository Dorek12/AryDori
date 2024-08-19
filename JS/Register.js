// RegisterView werden die Daten gefüllt die für die Angabe des Geburtstages relevant sind
// Jahr bis 1900 aufwärts
const currentYear = new Date().getFullYear();
const yearSelect = document.getElementById("birth_year");
for (let year = currentYear; year >= 1900; year--) {
  const option = document.createElement("option");
  option.value = year;
  option.text = year;
  yearSelect.appendChild(option);
}

// Monat
const monthSelect = document.getElementById("birth_month");
const months = ["Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember"];
for (let i = 0; i < months.length; i++) {
  const option = document.createElement("option");
  option.value = i + 1; 
  option.text = months[i];
  monthSelect.appendChild(option);
}

// Tage abhängig vom Monat
function updateDays() {
  const selectedMonth = parseInt(monthSelect.value);
  const daySelect = document.getElementById("birth_day");
  daySelect.innerHTML = ""; 

  const daysInMonth = new Date(currentYear, selectedMonth, 0).getDate(); // Holt die Tage die der ausgewählte Monat hat
  for (let day = 1; day <= daysInMonth; day++) {
    const option = document.createElement("option");
    option.value = day;
    option.text = day;
    daySelect.appendChild(option);
  }
}

// checkt beim registrieren ob Bedingugnen erfüllt sind

function checkRegister() {	
	var password = document.getElementById("passwordRegister").value;
	var passwordCheck = document.getElementById("passwordRegisterCheck").value;
	var email = document.getElementById("emailRegister").value;
	var firstName = document.getElementById("first_name").value;
    var lastName = document.getElementById("last_name").value;
    var number = document.getElementById("number").value;
	var failMsgEmail = checkEmailRegister(email);	
	var failMsgPassword = checkPasswordRegister(password); 
	var failMsgPasswordMatch = checkPasswords(password, passwordCheck); 
	var failMsgUser = checkUser(firstName, lastName, number);	
	if (!grecaptcha.getResponse()) {
		alert("Bitte füllen Sie das reCaptcha aus.");
        return false;
    }	

	return failMsgEmail && failMsgPassword && failMsgPasswordMatch && failMsgUser;		
	
}

function checkEmailRegister(email) {
	var failMsg = "";
	var emailRegisterField = document.getElementById("emailRegisterMsg");
	if(!email || email.indexOf('@') < 0 || email.indexOf('.') < 0) {
		failMsg = "Bitte geben Sie eine gültige E-Mail Adresse ein."; 
	} else {
		failMsg = "";
	}
	
	emailRegisterField.innerText = failMsg;
	if(failMsg) return false;
	else return true;	
}

function checkPasswords(password, passwordCheck) {
	var failMsg = "";
	var passwordRegisterCheckField = document.getElementById("passwordRegisterCheckMsg");
	if(password != passwordCheck) {
		failMsg = "Die Passwörter müssen übereinstimmen."; 	
	} else {
		failMsg = "";
	}
	
	passwordRegisterCheckField.innerText = failMsg;
	if(failMsg) return false;
	else return true;
}

function checkPasswordRegister(password) {
	var failMsg = "";
	var passwordRegisterField = document.getElementById("passwordRegisterMsg");
		
	if(!(/[A-Z]/.test(password)) || !(/\d/.test(password))) {
		 failMsg = "Das Passwort muss mindestens einen Großbuchstaben sowie eine Zahl beinhalten."
	} else if(password.length < 8) {
		failMsg = "Das Passwort muss mindestens 8 Zeichen lang sein."; 
	} else {
		failMsg = "";
	}	
	passwordRegisterField.innerText = failMsg;
    if(failMsg) return false;
    else return true;	
} 

function checkUser(firstName, lastName, number) {
    var failMsgFirstName = "";
    var failMsgLastName = "";
    var failMsgNumber = "";
    var first_nameField = document.getElementById("first_nameMsg");
    var last_nameField = document.getElementById("last_nameMsg");
    var numberField = document.getElementById("numberMsg");

    if (!firstName) {
		failMsgFirstName = "Bitte tragen Sie einen Vornamen ein.";
	} else {
		failMsgFirstName = "";
	}
    if (!lastName) {
         failMsgLastName = "Bitte tragen Sie einen Nachnamen ein.";        
	} else {
		failMsgLastName = "";
	}
	if(numberField) {
		if (!/^\d+$/.test(number)) {
         failMsgNumber = "Die Telefonnummer darf nur Nummern enthalten."      
	    }
	} else {
		failMsgNumber = "";
	}     
        
    first_nameField.innerText = failMsgFirstName;    
    last_nameField.innerText = failMsgLastName; 
    numberField.innerText = failMsgNumber;
    
    // Nummer ist nicht gefordert deshalb auch im letzten Check nicht dabei
    
    if(failMsgFirstName || failMsgLastName) return false;
	else return true;	         
    
}


// ändert die Tage wenn ein anderer Monat ausgewählt wird
monthSelect.addEventListener("change", updateDays);
updateDays(); 



