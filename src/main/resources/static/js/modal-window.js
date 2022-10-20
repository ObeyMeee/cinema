function callModalView(action, attributeValue1, sessionTime) {
    let form = document.getElementsByName('confirm-delete').item(0)
    form.action = action

    let text = document.getElementById('confirmationText')
    text.textContent = `Are you sure you want to delete ${attributeValue1} `
    if (sessionTime == null) {
        text.textContent += '?'
    } else {
        text.textContent += `at ${sessionTime}?`
    }
}