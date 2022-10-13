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

        let childrenRows = ticketsZone.children
        for (const childRow of childrenRows) {
            let rowInfo = childRow.children;
            let rowSpan = rowInfo.item(0);
            let seatSpan = rowInfo.item(1);
            if (rowSpan.innerHTML === `${row} row` && seatSpan.innerHTML === `${seat} seat`) {
                ticketsZone.removeChild(childRow)
                break
            }
        }
    }
    if (amountSelectedSeats === 0) {
        ticketsZone.style.display = "none"

    } else {
        ticketsZone.style.display = "block"
    }

}
