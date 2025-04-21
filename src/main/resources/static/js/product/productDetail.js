// 수량 증감 및 총 금액 계산
let qty = window.productData.inventory > 0 ? 1 : 0; // 초기 수량 (재고가 0이면 0, 아니면 1)
const prodInventory = window.productData.inventory; // 재고량
const prodPrice = window.productData.price;         // 상품 가격
const prodNo = window.productData.prodNo;           // 상품 번호
const qtyInput = document.querySelector("input.qty");   // 수량 input 태그
const totalPriceSpan = document.getElementById("totalPrice");       // 총 가격, 수량을 나타내주는 태그


// 초기 총 금액 표시
updateTotalPrice();

if (prodInventory !== 0) {
    // - 버튼 클릭
    document.querySelector("button.minus").addEventListener("click", function () {
        if (qty > 1 && prodInventory !== 0) {
            qty--;
            qtyInput.value = qty;
            updateTotalPrice();
        } else if (prodInventory === 0) {
            // alert("현재 해당 상품은 품절입니다.");
            Swal.fire({
                title: `현재 해당 상품은 품절입니다.`,
                icon: 'info',
                confirmButtonText: '확인'
            });
        }
    });

    // + 버튼 클릭
    document.querySelector("button.plus").addEventListener("click", function () {
        if (qty < prodInventory && prodInventory !== 0) {
            qty++;
            qtyInput.value = qty;
            updateTotalPrice();
        } else if (qty >= prodInventory) {
            // alert(``);
            Swal.fire({
                title: `현재 해당 상품은 ${prodInventory}개 까지\n구매 가능합니다.`,
                icon: 'info',
                confirmButtonText: '확인'
            });

        } else if (prodInventory === 0) {
            Swal.fire({
                title: `현재 해당 상품은 품절입니다.`,
                icon: 'info',
                confirmButtonText: '확인'
            });
        }
    });
}

// 총 금액 계산 및 표시 함수
function updateTotalPrice() {
    const totalPrice = prodPrice * qty;
    totalPriceSpan.textContent = `${totalPrice.toLocaleString()} 원 (${qty}개)`;
}


// 장바구니 추가
const addCartButton = document.getElementById("addCart");
if (addCartButton) {
    addCartButton.addEventListener("click", function () {
       if (!isLogin) {
           // 로그인을 안 한 경우
           Swal.fire({
               title: "로그인 후 이용 가능합니다!",
               icon: "warning",
           })
           .then((result) => {
               window.location.href = "/login";
           });
       }
       else {
           // 로그인을 한 경우
           fetch("/api/carts", {
               method: "POST",
               headers: {
                   "Content-Type": "application/json"
               },
               body: JSON.stringify({
                   "productNo" : prodNo,
                   "quantity": qty
               })
           })
               .then((response) => {
                   if (response.status === 200 || response.status === 201) {
                       Swal.fire({
                           title: "상품을 장바구니에 담았습니다!",
                           icon: "success",
                           confirmButtonText: "장바구니 보러가기",
                           showCancelButton: true,
                           cancelButtonText: "계속 쇼핑"
                       })
                           .then((result) => {
                               if (result.isConfirmed) {
                                   window.location.replace("/carts");
                               }
                           });
                   }
               });// end of fetch("/api/carts", {}) -----------------
       }

    });
}// end of if (addCartButton) -------------------



// 주문하기 버튼 클릭 시 상풍번호, 상품 수량을 만들어서 넘겨준다.
// 주문서를 응답받아 문제가 없을 경우 주문서 페이지로 redirect
const checkoutButton = document.getElementById("checkout");
if (checkoutButton) {
    checkoutButton.addEventListener("click", function () {
        if (!isLogin) {
            // 로그인을 안 한 경우
            Swal.fire({
                title: "로그인 후 이용 가능합니다!",
                icon: "warning",
            })
                .then((result) => {
                    window.location.href = "/login";
                });
        }
        else{
            const quantity = parseInt(qtyInput.value) || 0;
            if (quantity === 0) {
                Swal.fire({
                    icon: "warning",
                    title: "최소 주문수량은 1개 입니다!!",
                    confirmButtonText: "확인"
                })
                return;
            }
            else {
                fetch("/api/orders/initiate", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        "productNo" : prodNo,
                        "quantity": quantity
                    })
                })
                    .then(response => {
                        if (response.status === 400) {
                            Swal.fire({
                                icon: "error",
                                title: "처리 도중 에러가 발생했습니다..",
                                confirmButtonText: "확인"
                            })
                            return null;
                        }
                        else {
                            return response.json();
                        }
                    })
                    .then(result => {
                        if (result === null) {return};
                        const draftId = result.draftId;
                        window.location.replace(`/orders/draft/${draftId}`);
                    })
            }
        }
    });
}// end of if (checkoutButton) --------------------