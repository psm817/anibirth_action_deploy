document.addEventListener('DOMContentLoaded', function() {
  var modal = document.getElementById("donateModal");
  var btn = document.getElementById("btnDonate");
  var span = document.getElementsByClassName("close")[0];

  console.log("Modal:", modal);
  console.log("Button:", btn);
  console.log("Close Span:", span);

  if (btn) {
    btn.addEventListener('click', function() {
      modal.style.display = "block";
    });
  } else {
    console.error("Button element not found!");
  }

  if (span) {
    span.addEventListener('click', function() {
      modal.style.display = "none";
    });
  } else {
    console.error("Close span element not found!");
  }

  window.addEventListener('click', function(event) {
    if (event.target == modal) {
      modal.style.display = "none";
    }
  });

    const submitDonationBtn = document.getElementById('submitDonation');
    submitDonationBtn.addEventListener('click', function() {
        const recipient = document.getElementById('recipient').value;
        const amount = document.getElementById('amount').value;
        if (recipient && amount) {
            // Make a donation request to the server
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '/donation/submit';

            const recipientInput = document.createElement('input');
            recipientInput.type = 'hidden';
            recipientInput.name = 'recipientId';
            recipientInput.value = recipient;
            form.appendChild(recipientInput);

            const amountInput = document.createElement('input');
            amountInput.type = 'hidden';
            amountInput.name = 'amount';
            amountInput.value = amount;
            form.appendChild(amountInput);

            document.body.appendChild(form);
            form.submit();
        } else {
            alert('모든 필드를 입력하세요.');
        }
    });
});
document.getElementById('donationForm').addEventListener('submit', function(event) {
    var amount = document.getElementById('amount').value;
    if (!amount) {
        event.preventDefault(); // Prevent form submission
        document.getElementById('amountError').style.display = 'block';
    }
});

