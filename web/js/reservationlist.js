$(document).ready(function () {
    loadreservations();

    let table = document.getElementById("reservationlist");
    clearTable(table);

    let row = table.insertRow(-1);
    let cell = row.insertCell(-1);
    cell.innerHTML = "RaumId";

    cell = row.insertCell(-1);
    cell.innerHTML = "EventId";

    cell = row.insertCell(-1);
    cell.innerHTML = "ZeitVon";

    cell = row.insertCell(-1);
    cell.innerHTML = "ZeitBis";
});

function loadreservations() {
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

function showReservations(reservationData) {

    let table = document.getElementById("reservationlist");
    clearTable(table);

    $.each(reservationData, function (reservationId, reservation) {
        if (reservation.roomId) {
            let row = table.insertRow(-1);
            let cell = row.insertCell(-1);
            cell.innerHTML = reservation.room.roomId;

            cell = row.insertCell(-1);
            cell.innerHTML = reservation.event.eventId;

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