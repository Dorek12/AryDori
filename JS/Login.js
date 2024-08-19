// checkt beim einloggen dass E-Mail und Passwort nicht leer sind

function checkLogin(event) {
  var emailInput = document.getElementById("emailLog").value;
  var passwordInput = document.getElementById("passwordLog").value;
  var failField = document.getElementById("passwordLogMsg");

  if (emailInput.trim() === "" || passwordInput === "") {
	  event.preventDefault();   
    failField.innerText = "Bitte geben Sie eine gültige E-Mail-Adresse und Passwort ein.";
  }
}