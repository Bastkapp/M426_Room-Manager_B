$(document).ready(function () {
    loadReservations();

});

/**
 * loads the reservations from the webservice
 *
 */
function loadReservations() {
    $
        .ajax({
            url: "./resource/reservation/list",
            dataType: "json",
            type: "GET"
        })
        .done(showReservations)
        .fail(function (xhr, status, errorThrown) {
            if (xhr.status == 404) {
                $("#message").text("Keine Reservationen vorhanden");
            } else {
                $("#message").text("Fehler beim Lesen der Reservationen");
            }
        })
    /**
     * listener for submitting the form
     */
    $("#reservationadminForm").submit(saveReservation);

    /**
     * listener for button [abbrechen], redirects to reservationsliste
     */
    $("#cancel").click(function () {
        window.location.href = "../Reservation_List.html";
    });
}

/**
 * shows all reservations as a table
 */
function showReservations(reservationData) {

    let table = document.getElementById("reservationList");
    clearTable(table);

    $.each(reservationData, function (reservation) {
        if (reservation.id) {
            let row = table.insertRow(-1);

            let cell = row.insertCell(-1);
            cell.innerHTML = reservation.room;

            cell = row.insertCell(-1);
            cell.innerHTML = reservation.event;

            cell = row.insertCell(-1);
            cell.innerHTML = reservation.start;

            cell = row.insertCell(-1);
            cell.innerHTML = reservation.end;
        }
    });
}


function clearTable(table) {
    while (table.hasChildNodes()) {
        table.removeChild(table.firstChild);
    }
}

/**
 * sends the reservation data to the webservice
 * @param form the form being submitted
 */
function saveReservation(form) {
    form.preventDefault();
    var id="";
    if ($.urlParam('id') !== null) {
        id = "?id=" + $.urlParam('id');
    }
    $
        .ajax({
            url: "./resource/reservation/save" + id,
            dataType: "text",
            type: "POST",
            data: $("#reservationadminForm").serialize()
        })
        .done(function (jsonData) {
            window.location.href = "./Reservation_Admin.html";
        })
        .fail(function (xhr, status, errorThrown) {
            showMessage("error", xhr.responseText);
        })
}