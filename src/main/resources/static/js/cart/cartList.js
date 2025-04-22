// 초기 합계 계산
updateCartSummary();

// 장바구니 선택 등에 따른 상품 총액을 계산해주는 함수
function updateCartSummary() {
    let totalSum = 0;   // 장바구니 합계
    let selectedSum = 0;    // 선택 상품 합계


    document.querySelectorAll("div.cart_item").forEach(item => {   // 각 상품 div 태그 각 div.cart_item 의 내부를 탐색
        const totalText = item.querySelector(".prodtotal").textContent; // 상품 총액을 표시해주는 태그의 text (4,000원)
        const totalPrice = parseInt(totalText.replace(/[^0-9]/g, '')) || 0; // [^0-9]: 숫자가 아닌 모든 문자 -> 이것을 ''로 치환, || 0 은 parseInt 를 실행하지 못할 경우 0으로

        if (totalPrice === 0) {
            alert("에러가 발생하였습니다.");
        }

        totalSum += totalPrice;     // 선택 상품의 금액을 장바구니 합계에 더함

        const checkbox = item.querySelector("input[name='selectedItems']"); // 체크박스 태그
        if (checkbox.checked) {
            // 체크박스에 체크가 되어 있다면
            selectedSum += totalPrice;
        }
    });

    // 합계 표시해주기
    document.querySelector("div.selectsum > p").textContent = `선택상품 합계: ${selectedSum.toLocaleString()} 원`;
    document.querySelector("div.cartsum > p").textContent = `장바구니 합계: ${totalSum.toLocaleString()} 원`;
}// end of function updateCartSummary() ----------


// 체크 박스 클릭 이벤트
document.querySelectorAll("input[name='selectedItems']").forEach(checkbox => {
    checkbox.addEventListener("change", updateCartSummary);   // 체크 박스 업데이트 마다 updateCartSummary() 호출
});




// 상품 총액 업데이트 함수
function updateItemTotal(cartItem) {
    const quantity = parseInt(cartItem.querySelector(".prodcount").value) || 0;     // input 태그에 쓰여진 수량
    const priceText = cartItem.querySelector(".price").textContent;     // 각 상품 가격 (3,000원)
    const price = parseInt(priceText.replace(/[^0-9]/g, '')) || 0;  // 가격에서 숫자만 추출

    const totalPrice = price * quantity;
    const $totalElement = cartItem.querySelector(".prodtotal");  // 상품 총액

    $totalElement.textContent = `${totalPrice.toLocaleString()} 원`;
}// end of function updateItemTotal(cartItem) -----------------


// 수량 조절 이벤트
// 상품수량 감소 (-) 버튼 클릭 시
document.querySelectorAll("button.minus").forEach(button => {
    button.addEventListener("click", e => {
        const $input = e.target.nextElementSibling;     // 수량 input 태그
        let quantity = parseInt($input.value) || 0;     // input 태그 값을 가져옴
        const productNo = parseInt(e.target.value) || 0;     // 상품 번호

        if (quantity > 1) {

            let body = JSON.stringify({
                productNo: productNo,
                quantity: --quantity
            })

            function success() {
                $input.value = quantity;    // 다시 값 넣어주기
                updateItemTotal(e.target.closest('div.cart_item')); // 이벤트가 발생한 태그에서 가장 상위에 있는 'div.cart_item' 태그를 찾아줌 return: element 즉 <div class='cart_item'>...</div> 를 넘겨주는 것임.
                updateCartSummary();    // 전체 수량 업데이트
            }

            function fail() {
                Swal.fire({
                    icon: "error",
                    title: "수량 변경에 실패했습니다."
                })
            }

            httpRequest("/api/carts", "POST", body, success, fail);
        }
    });
});

// 상품수량 증가 (+) 버튼 클릭 시
document.querySelectorAll("button.plus").forEach(button => {
    button.addEventListener("click", e => {
        const inventory = parseInt(e.target.nextElementSibling.value) || 0;  // 상품의 재고량
        const $input = e.target.previousElementSibling;     // 수량 input 태그
        let quantity = parseInt($input.value) || 0;     // input 태그 값을 가져옴
        const productNo = parseInt(e.target.value) || 0;     // 상품 번호

        if (quantity == inventory) {
            Swal.fire({
                icon: "info",
                title: `주문 가능 수량은 ${inventory}개 입니다.`,
                confirmButtonText: "확인"
            })
                .then((result) => {
                    $input.value = inventory;
                    return;
                })
        }
        else {

            const body = JSON.stringify({
                productNo: productNo,
                quantity: ++quantity,
            })

            function success() {
                $input.value = quantity;    // 다시 값 넣어주기
                updateItemTotal(e.target.closest('div.cart_item')); // 상품 총액 업데이트 함수 호출 현재 div 태그를 파라미터로 넘겨줌
                updateCartSummary();    // 전체 수량 업데이트
            }

            function fail() {
                Swal.fire({
                    icon: "error",
                    title: "수량 변경에 실패했습니다."
                })
            }
            httpRequest("/api/carts", "POST", body, success, fail);
        }
    });
});


// 상품 삭제 버튼 클릭 이벤트
document.querySelectorAll("button.removeItem").forEach(button => {
    button.addEventListener("click", e => {

        const cartNo = parseInt(e.target.value);

        function success() {
            const cartItem = e.target.closest('div.cart_item');   // 속한 div 태그를
            cartItem.remove();    // DOM 에서 제거
            updateCartSummary();  // 상품 합계 수정
        }

        function fail() {
            Swal.fire({
                icon: "error",
                title: "수량 변경에 실패했습니다."
            })
        }

        httpRequest(`/api/carts/${cartNo}`, "DELETE", null, success, fail);
   });
});



// 데이터베이스에 접근히여 장바구니 정보를 변경하는 함수
function httpRequest(url, method, body, success, fail) {

    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
        },
        body: body
    })
        .then(response => {
            if (response.status === 200 || response.status === 201) {
                return success();
            }
            else {
                return fail();
            }
        });
}// end of function httpRequest(url, method, body, success, fail) -------------------------


// 상품 페이지로 이동하는 함수
function viewProduct(prodNo){
    window.location.replace(`/product/${prodNo}`);
}// end of function viewProduct(prodNo) --------------------


// 회원의 장바구니를 비워주는 함수
function emptyCart() {
    Swal.fire({
        icon: "question",
        title: "장바구니를 비우시겠습니까?",
        confirmButtonText: "비우기",
        showCancelButton: true,
        cancelButtonText: "취소"
    })
        .then((result) => {
            if (result.isConfirmed) {
                function success() {
                    Swal.fire({
                        icon: "success",
                        title: "장바구니를 비웠습니다.",
                        confirmButtonText: "확인",
                    })
                        .then((result) => {
                            window.location.reload();
                        })

                }

                function fail() {
                    Swal.fire({
                        icon: "error",
                        title: "장바구니를 비우는 중 오류가 발생했습니다.",
                        confirmButtonText: "확인",
                    });
                }
                httpRequest("/api/carts", "DELETE", null, success, fail);
            }
        });
}// end of function emptyCart() -------------------


// 선택한 일부 상품을 주문하는 함수
function pickOrder() {
    const selected = document.querySelectorAll("input[name='selectedItems']:checked");
    initiateOrder(selected);
}// end of function pickOrder() --------------------



// 전체 상품을 주문하는 함수
function pickAll() {
    const selected = document.querySelectorAll("input[name='selectedItems']");
    initiateOrder(selected);
}// end of function pickAll() -----------------------



// 주문서 작성하기
function initiateOrder(selected) {

    const cartItemList = [];
    selected.forEach(checkbox => {
        const cartItemDiv = checkbox.closest("div.cart_item");

        const prodNo = checkbox.value;
        const quantityInput = cartItemDiv.querySelector("input.prodcount");
        const quantity = parseInt(quantityInput.value) || 0;

        cartItemList.push({
            productNo: parseInt(prodNo),
            quantity: quantity
        });
    })

    if (cartItemList.length === 0) {
        Swal.fire({
            icon: "warning",
            title: "주문할 상품을 선택하세요!"
        })
        return;
    }

    fetch("/api/orders/initiate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "cartItemList": cartItemList
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
        .then(data => {
           const draftId = data.draftId;
           window.location.replace(`/orders/draft/${draftId}`);
        });
}// end of function initiateOrder() ------------------------