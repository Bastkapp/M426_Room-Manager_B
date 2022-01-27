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