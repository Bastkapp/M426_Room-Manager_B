$(document).ready(function () {
    $("#loginForm").submit(checkLogin);
});

function checkLogin(form)
{
    console.log("Test")
    if(form.userid.value == "Admin" && form.pwd.value == "Admin001")
    {
        window.location.href="./Reservation_Admin.html"
    }
    else
    {
        alert("Fehlerhaftes Passwort oder Benutzername")
        return false;
    }
}