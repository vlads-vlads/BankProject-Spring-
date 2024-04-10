document.addEventListener("DOMContentLoaded", function() {
    var accountId = sessionStorage.getItem('accountId');
    fetchTransactions(accountId);
    fetchBalance(accountId);

    var depositForm = document.querySelector('.form--deposit');
    var withdrawalForm = document.querySelector('.form--withdrawal');
    var transferForm = document.querySelector('.form--transfer');
    var userBalance = document.getElementById('userBalance');
    var transactionList = document.getElementById('transactionList');

    depositForm.addEventListener('submit', function(event) {
        event.preventDefault();
        var amount = parseFloat(depositForm.querySelector('.form__input--amount').value);
        depositMoney(amount);
    });

    withdrawalForm.addEventListener('submit', function(event) {
        event.preventDefault();
        var amount = parseFloat(withdrawalForm.querySelector('.form__input--amount').value);
        withdrawMoney(amount);
    });

    transferForm.addEventListener('submit', function(event) {
        event.preventDefault();
        var recipient = transferForm.querySelector('.form__input--recipient').value;
        var amount = parseFloat(transferForm.querySelector('.form__input--amount').value);
        transferMoney(recipient, amount);
    });

    function fetchTransactions(accountId) {
        fetch(`api/v1/accounts/transactions/${accountId}`, {
            method: 'GET'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch transactions');
            }
            return response.json();
        })
        .then(transactions => {

            displayTransactions(transactions);
        })
        .catch(error => console.error('Error fetching transactions:', error.message));
    }

    function fetchBalance(accountId) {
        fetch(`api/v1/accounts/${accountId}`, {
            method: 'GET'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch balance');
            }
            return response.json();
        })
        .then(account => {
            updateBalance(account.balance);
        })
        .catch(error => console.error('Error fetching balance:', error.message));
    }

    function depositMoney(amount) {
        var accountId = sessionStorage.getItem('accountId');
        fetch(`api/v1/accounts/deposit/${accountId}?amount=${amount}`, {
            method: 'PUT'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to deposit money');
            }
            return response.json();
        })
        .then(account => {
            updateBalance(account.balance);
            fetchTransactions(accountId);
        })
        .catch(error => console.error('Error depositing money:', error.message));
    }

    function withdrawMoney(amount) {
        var accountId = sessionStorage.getItem('accountId');
        fetch(`api/v1/accounts/withdrawal/${accountId}?amount=${amount}`, {
            method: 'PUT'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to withdraw money');
            }
            return response.json();
        })
        .then(account => {
            updateBalance(account.balance);
            fetchTransactions(accountId);
        })
        .catch(error => console.error('Error withdrawing money:', error.message));
    }

    function transferMoney(recipient, amount) {
        var accountId = sessionStorage.getItem('accountId');
        fetch(`api/v1/accounts/transfer/${accountId}/${recipient}?amount=${amount}`, {
            method: 'PUT'
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to transfer money');
            }
            return response.json();
        })
        .then(account => {
            updateBalance(account.balance);
            fetchTransactions(accountId);
            })
        .catch(error => console.error('Error transferring money:', error.message));
    }

     function displayTransactions(transactions) {
         transactionList.innerHTML = '';
         transactions.forEach(transaction => {
             var listItem = document.createElement('li');
             listItem.textContent = `${transaction.type}: ${transaction.amount}€`;


             listItem.classList.add('transaction-item', transaction.type.toLowerCase());

             transactionList.appendChild(listItem);
         });
     }

    function updateBalance(balance) {
        userBalance.textContent = balance + '€';
    };
});
