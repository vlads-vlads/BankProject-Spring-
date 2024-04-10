const loginForm = document.getElementById("loginForm");

loginForm.addEventListener("submit", function(event) {
    event.preventDefault();
    login();
});

function login() {
    var formData = {
        name: document.getElementById("username").value,
        password: document.getElementById("password").value
    };

    fetch("api/v1/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(formData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to authenticate user');
        }
        return response.json();
    })
    .then(account => {
        console.log(account.balance);
        console.log(account.id);
        sessionStorage.setItem('accountId', account.id);
        sessionStorage.setItem('accountBalance', account.balance);
        window.location.href = 'account.html';

    })
    .catch(error => {
        console.error('Error fetching account information:', error.message);
        alert('Failed to fetch account information. Please try again later.');
    });
}

