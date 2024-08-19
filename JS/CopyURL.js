// Link kopieren bei Artikel merken

	function copyURL() {
		var popup = document.getElementById("popup_link");
		var currentURL = document.getElementById("shareBtn").value;
		navigator.clipboard.writeText(currentURL);
		popup.style.display = "block";
		setTimeout(function() {
			popup.style.display = "none";
		}, 3000);
	}