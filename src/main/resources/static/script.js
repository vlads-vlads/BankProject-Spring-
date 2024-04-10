function createAccount() {
    var accountName = document.getElementById("accountName").value;
    var email = document.getElementById("accountEmail").value;
    var accountPassword = document.getElementById("accountPassword").value;

    var accountData = {
        name: accountName,
        email: email,
        password: accountPassword
    };

    fetch('api/v1/accounts', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(accountData)
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        }
        throw new Error('Failed to create account');
    })
    .then(data => {
        document.getElementById("output").innerHTML = "Account created successfully!";
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById("output").innerHTML = "Error creating account: " + error.message;
    });
}



document.getElementById("accountForm").addEventListener("submit", function(event) {
    event.preventDefault();
    createAccount();
});




