function addParam() {
    let searchInput = document.getElementById("search-input")
    const searchValue = searchInput.value
    let searchRef = document.getElementById("search-ref")
    let href = searchRef.getAttribute("href");

    if (searchValue === '' || searchValue === null) {
        searchRef.setAttribute("href", href)
    }
    searchRef.setAttribute("href", `${href}&value=${searchValue}`)
}

function setAction(action) {
    document.deleteUser.action = action
}

const exampleModal = document.getElementById('confirm')
exampleModal.addEventListener('show.bs.modal', event => {
    const button = event.relatedTarget
    const userLogin = button.getAttribute('data-bs-whatever')
    let text = document.getElementById('confirmationText');
    text.textContent = `Are you sure you want to delete ${userLogin} ?`
})
