let amountSelectedSeats = 0;


function selectSeat(button, row, seat) {
    let ticketsZone = document.getElementById("ticketsZone")
    let image = button.children.item(0);
    if (button.getAttribute("data-status") === "active") {
        amountSelectedSeats++;
        button.setAttribute("data-status", "selected")
        image.setAttribute("src", "/seats/selected-seat.png")


        let divRow = document.createElement("div")
        divRow.setAttribute("class", "row")

        let rowSpan = document.createElement("span")
        rowSpan.setAttribute("class", "col-1")
        rowSpan.innerText = `${row} row`
        let seatSpan = document.createElement("span")
        seatSpan.setAttribute("class", "col-1")
        seatSpan.innerText = `${seat} seat`
        let priceSpan = document.createElement("span")
        priceSpan.setAttribute("class", "col-1")
        priceSpan.innerText = `100 UAH`

        divRow.appendChild(rowSpan)
        divRow.appendChild(seatSpan)
        divRow.appendChild(priceSpan)
        ticketsZone.appendChild(divRow)
    } else {
        amountSelectedSeats--;
        button.setAttribute("data-status", "active")
        image.setAttribute("src", "/seats/active-seat.png")

    }

    ticketsZone.style.display = amountSelectedSeats === 0 ? "none" : "block"

}
