// navigiert zwischen den verschiedenen Optionen bei der Bezahlung auf der CheckoutView


		document.addEventListener('DOMContentLoaded', function() {
			const paymentRows = document.querySelectorAll('.payment-method');
			const paymentContentRows = document.querySelectorAll('.collapse');

			paymentRows.forEach(function(row) {
				row.addEventListener('click', function() {
					const contentRow = this.nextElementSibling;

					paymentContentRows.forEach(function(otherContentRow) {
						if (otherContentRow !== contentRow) {
							otherContentRow.classList.remove('show');
							const inputFields = otherContentRow
									.querySelectorAll('input');
							inputFields.forEach(function(input) {
								input.value = ''; 
							});
						}
					});
					
					paymentRows.forEach(function(otherRow) {
						otherRow.classList.remove('selected-payment');
					});

					row.classList.add('selected-payment');
				});
			});
		});