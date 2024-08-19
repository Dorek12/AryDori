// navigiert was sichtbar auf der ProfileView ist 
document.addEventListener("DOMContentLoaded", function() {
    showContent("overview");
});

	function showContent(contentId) {
	    var contents = document.getElementsByClassName("menu-content");
	    for (var i = 0; i < contents.length; i++) {
	        contents[i].style.display = "none";
	    }

	    var menuItems = document.getElementsByClassName("menu-item");
	    for (var i = 0; i < menuItems.length; i++) {
	        menuItems[i].classList.remove("selected");
	    }

	    document.getElementById(contentId).style.display = "block";
	    
	    var clickedMenuItem = document.querySelector('[onclick="showContent(\'' + contentId + '\')"]');
	    								
	    clickedMenuItem.parentNode.classList.add("selected");
	}
	
	// dient zur Passwortkontrolle beim Passwort ändern in der ProfileView
	
	document.addEventListener("DOMContentLoaded", function() {
    const currentPasswordInput = document.getElementById("currentPassword");
    const newPasswordInput = document.getElementById("newPassword");
    const confirmNewPasswordInput = document.getElementById("confirmNewPassword");
    const validatePasswordButton = document.getElementById("validatePasswordButton");
    const errorMessage = document.getElementById("errorMessage");

    function isFormValid() {
        const currentPassword = currentPasswordInput.value;
        const newPassword = newPasswordInput.value;
        const confirmNewPassword = confirmNewPasswordInput.value;

        // Überprüfe, ob die Bedingungen erfüllt sind
        const isLengthValid = newPassword.length >= 8;
        const hasUpperCase = /[A-Z]/.test(newPassword);
        const hasNumber = /[0-9]/.test(newPassword);
        const passwordsMatch = newPassword === confirmNewPassword;
        const newPasswordNotEqualCurrent = newPassword !== currentPassword;

        return {
            isValid:
                isLengthValid &&
                hasUpperCase &&
                hasNumber &&
                passwordsMatch &&
                newPasswordNotEqualCurrent,
            messages: {
                length: isLengthValid ? "" : "Das Passwort muss mindestens 8 Zeichen lang sein. ",
                uppercase: hasUpperCase ? "" : "Das Passwort muss mindestens einen Großbuchstaben enthalten. ",
                number: hasNumber ? "" : "Das Passwort muss mindestens eine Zahl enthalten. ",
                match: passwordsMatch ? "" : "Die Passwörter stimmen nicht überein. ",
                notEqual: newPasswordNotEqualCurrent ? "" : "Das neue Passwort darf nicht mit dem aktuellen Passwort identisch sein. "
            }
        };
    }

    function updateButtonState() {
        const { isValid, messages } = isFormValid();

        if (isValid) {
            validatePasswordButton.disabled = false;
            errorMessage.style.display = "none";
        } else {
            validatePasswordButton.disabled = true;
            errorMessage.style.display = "block";
            errorMessage.textContent = Object.values(messages).join(" ");
        }
    }

    // Füge Event Listener hinzu, um die Validierung bei Eingabe auszulösen
    currentPasswordInput.addEventListener("input", updateButtonState);
    newPasswordInput.addEventListener("input", updateButtonState);
    confirmNewPasswordInput.addEventListener("input", updateButtonState);

    updateButtonState();
});

