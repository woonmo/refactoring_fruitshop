
const searchButton = document.getElementById("searchIcon");

// 검색 버튼 클릭 시
if (searchButton) {
    searchButton.addEventListener("click", (e) => {
       search();
    });
}

// 검색창에서 엔터 클릭 시
const searchInput = document.getElementById("productSearch");
if (searchInput) {
    searchInput.addEventListener("keyup", (e) => {
        if (e.keyCode === 13) {
            search();
        }
    });
}


function search() {
    const product = document.getElementById("productSearch").value.trim();
    if (product) {
        const currentURL = window.location.pathname;            // 현재 URL /products or /products/SPRING ...
        const newURL = currentURL + "?product=" + product;      // 새로운 URL /products?product=감 or /products/SPRING?product=감 ...
        window.location.href = newURL;
    }
    else {
        const currentURL = window.location.pathname;
        window.location.href = currentURL;
    }
}