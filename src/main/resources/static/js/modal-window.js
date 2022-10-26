function callModalViewToDelete(action, elementToBeDeleted) {
    let form = document.getElementsByName('confirm-delete').item(0)
    form.action = action
    let text = document.getElementById('confirmationText')
    text.textContent = `Are you sure you want to delete ${elementToBeDeleted} ?`
}

function callModalViewToDeleteSession(action, elementToBeDeleted, sessionTime) {
    let form = document.getElementsByName('confirm-delete').item(0)
    form.action = action
    let text = document.getElementById('confirmationText')
    text.textContent = `Are you sure you want to delete ${elementToBeDeleted} at ${sessionTime} ?`
}