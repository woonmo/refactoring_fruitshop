// 검색 버튼 클릭 시
const searchButton = document.getElementById("searchIcon");

if (searchButton) {
    searchButton.addEventListener("click", (e) => {
       search();
    });
}// end of  if (searchButton) ------------------


// 검색창에서 엔터 클릭 시
const searchInput = document.getElementById("productSearch");

if (searchInput) {
    searchInput.addEventListener("keyup", (e) => {
        if (e.keyCode === 13) {
            search();
        }
    });
}// end of if (searchInput) ----------------------


// 상품 검색
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
}// end of function search() -------------------


// 상품 상세로 이동
function showProductDetail(prodNo) {
    window.location.href = "/product/"+prodNo;
}// end of function showProductDetail(prodNo) ---------------------