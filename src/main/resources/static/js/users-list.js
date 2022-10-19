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
