// Hier nur JavaScript, dass auf jeder Seite implementiert werden muss

// Setzt die Zahlen im Header bei Warenkorb und Wunschliste 
// Achtung: auf anderen Seite EventListener benutzer, ein weiteres 
// window.onload könnte zu Problemen führen
window.onload = function() {
		var cartCount = document.getElementById("cartCount").value;
	    var wishlistCount = document.getElementById("wishlistCount").value;
		if (cartCount != null && cartCount != 0)
			updateItemCount('cart_itemCount', cartCount);
		if (wishlistCount != null && wishlistCount != 0)
			updateItemCount('wishlist_itemCount', wishlistCount);
	}

	function updateItemCount(itemCountId, count) {
		var itemCount = document.getElementById(itemCountId);
		itemCount.innerHTML = count;
	}

// Account und Sternebewertung Popup 

function showPopup(popupId) {
  var popup = document.getElementById(popupId);
  if (popup.style.display == "none") {
    popup.style.display = "block";
  } else {
    popup.style.display = "none";
  }
}

function closePopup(popupId) {
	var popup = document.getElementById(popupId);
    if (popup) {
        popup.style.display = 'none';
    }
}

// Email und Password check im Header

function checkInput() {	
	var password = document.getElementById("checkPassword").value;
	var email = document.getElementById("checkEmail").value;
	var failMsgEmail = checkEmail(email);	
	var failMsgPassword = checkPassword(password); 		
	return failMsgEmail && failMsgPassword;	
}

function checkEmail(email) {
	var failMsg = "";
	var failField = document.getElementById('emailMsg');
	if(!email || email.indexOf('@') < 0 || email.indexOf('.') < 0) 	failMsg = "Bitte geben Sie eine gültige E-Mail Adresse ein."; 	
	failField.innerText = failMsg;
	if(failMsg) return false;
	else return true;	
}

function checkPassword(password) {
	var failMsg = "";
	var failField = document.getElementById('passwordMsg');
	if(!password) 	failMsg = "Bitte geben Sie Ihr Passwort ein."; 	
	failField.innerText = failMsg;
	if(failMsg) return false;
	else return true;	
} 

// lädt Seite neu wenn sie über die Browser Navigation betreten wird

if (window.performance.navigation.type === 2) {
		window.location.reload();
	}

