<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
</head>
<body>

<h2>Dashboard</h2>

<p style="color:blue; font-weight:bold;">
    Available Balance : ₹ ${sessionScope.balance}
</p>

<form action="deposit" method="post">
    <input type="number" name="amount" placeholder="Deposit amount" required>
    <button>Deposit</button>
</form>

<form action="withdraw" method="post">
    <input type="number" name="amount" placeholder="Withdraw amount" required>
    <button>Withdraw</button>
</form>

<form action="transfer" method="post">
    <input type="email" name="email" placeholder="Receiver email" required>
    <input type="number" name="amount" placeholder="Transfer amount" required>
    <button>Transfer</button>
</form>

<a href="logout" style="color:red;">Logout</a>

</body>
</html>
