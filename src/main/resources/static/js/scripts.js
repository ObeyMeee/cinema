let amountSelectedSeats = 0;


function selectSeat(button, row, seat, price) {
    let ticketsZone = document.getElementById("ticketsZone")
    let image = button.children.item(0);
    let ticketType = button.getAttribute("data-type");
    if (button.getAttribute("data-status") === "active") {
        amountSelectedSeats++;
        button.setAttribute("data-status", "selected")
        image.setAttribute("src", "/seats/selected-seat.png")

        let divRow = document.createElement("div")
        divRow.setAttribute("class", "row ticket-info")


        let inputRow = createInput("row", row)
        let inputSeat = createInput("seat", seat)
        let inputPrice = createInput("price", price)
        let inputTicketType = createInput("ticketType", ticketType)


        let divClassAuto1 = createDiv("col-auto")
        let divClassAuto2 = createDiv("col-auto")
        let divClassAuto3 = createDiv("col-auto")
        let divClassAuto4 = createDiv("col-auto")

        let spanRow = createSpan("row")
        let spanSeat = createSpan("seat")
        let spanPrice = createSpan("UAH")

        divClassAuto1.appendChild(inputRow)
        divClassAuto1.appendChild(spanRow)

        divClassAuto2.appendChild(inputSeat)
        divClassAuto2.appendChild(spanSeat)

        divClassAuto3.appendChild(inputPrice)
        divClassAuto3.appendChild(spanPrice)

        divClassAuto4.appendChild(inputTicketType)

        divRow.appendChild(divClassAuto1)
        divRow.appendChild(divClassAuto2)
        divRow.appendChild(divClassAuto3)
        divRow.appendChild(divClassAuto4)
        ticketsZone.prepend(divRow)
    } else {
        amountSelectedSeats--;
        button.setAttribute("data-status", "active")
        if (ticketType === "GOOD") {
            image.setAttribute("src", "/seats/active-good-seat.png")
        } else {
            image.setAttribute("src", "/seats/active-lux-seat.png")
        }

        let ticketsInfoWrapper = document.getElementsByClassName("row ticket-info");

        for (let ticketInfoDiv of ticketsInfoWrapper) {
            let divs = ticketInfoDiv.children
            let inputRowDiv = divs.item(0)
            let inputSeatDiv = divs.item(1)
            let inputRowElement = inputRowDiv.firstChild
            let inputSeatElement = inputSeatDiv.firstChild
            if (inputRowElement.value == row && inputSeatElement.value == seat) {
                ticketsZone.removeChild(ticketInfoDiv)
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

function createInput(attributeName, value) {
    let inputElement = document.createElement("input");
    inputElement.setAttribute("class", "form-control-plaintext ticket-info")
    inputElement.setAttribute("name", `${attributeName}`)
    inputElement.setAttribute("value", value)
    inputElement.readOnly = true
    return inputElement
}

function createDiv(className) {
    let divElement = document.createElement("div");
    divElement.setAttribute("class", className)
    return divElement
}

function createSpan(innerText) {
    let span = document.createElement("span")
    span.setAttribute("class", "ticket-info")
    span.innerText = innerText
    return span
}

let purchaseButton = document.getElementById("purchaseButton");
purchaseButton.addEventListener("click", function () {
    if (purchaseButton.getAttribute("data-user-roles-count") > 1) {
        alert("Our cats get their tickets for free. Enjoy film :)")
    }
    purchaseButton.disabled = true
    let form = document.getElementById("ticketsZone");
    form.submit()
})
let registrationButton = document.getElementById("registration-button");
registrationButton.addEventListener("click", function () {
    registrationButton.disabled = true
    let form = document.getElementById("registration-form");
    form.submit()
})
