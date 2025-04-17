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
        const productNo = parseInt(e.target.value);     // 상품 번호

        if (quantity > 1) {
            // DB 수량을 변경
            fetch("/api/carts", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    productNo: productNo,
                    quantity: --quantity,
                })
            })
                .then(response => {
                    if (response.status === 200 || response.status === 201) {
                        $input.value = quantity;    // 다시 값 넣어주기
                        updateItemTotal(e.target.closest('div.cart_item')); // 상품 총액 업데이트 함수 호출 현재 div 태그를 파라미터로 넘겨줌
                        updateCartSummary();    // 전체 수량 업데이트
                    }
                })
        }
    });
});

// 상품수량 증가 (+) 버튼 클릭 시
document.querySelectorAll("button.plus").forEach(button => {
    button.addEventListener("click", e => {
        const inventory = parseInt(e.target.nextElementSibling.value);  // 상품의 재고량
        const $input = e.target.previousElementSibling;     // 수량 input 태그
        let quantity = parseInt($input.value) || 0;     // input 태그 값을 가져옴
        const productNo = parseInt(e.target.value);     // 상품 번호

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
            // DB 수량을 변경
            fetch("/api/carts", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    productNo: productNo,
                    quantity: ++quantity,
                })
            })
                .then(response => {
                    if (response.status === 200 || response.status === 201) {
                        $input.value = quantity;    // 다시 값 넣어주기
                        updateItemTotal(e.target.closest('div.cart_item')); // 상품 총액 업데이트 함수 호출 현재 div 태그를 파라미터로 넘겨줌
                        updateCartSummary();    // 전체 수량 업데이트
                    }
                })
        }
    });
});


// 상품 삭제 버튼 클릭 이벤트
document.querySelectorAll("button.removeItem").forEach(button => {
    button.addEventListener("click", e => {

        const cartNo = parseInt(e.target.value);

        // DB 먼저 제거한다.
        fetch(`/api/carts/${cartNo}`, {
            method: "DELETE",
        })
            .then(response => {
               if (response.ok || response.status === 200) {
                   const cartItem = e.target.closest('div.cart_item');   // 속한 div 태그를
                   cartItem.remove();    // DOM 에서 제거
                   updateCartSummary();  // 상품 합계 수정
               }
            });
   });
});



// 상품 페이지로 이동하는 함수
function viewProduct(prodNo){
    window.location.replace(`/product/${prodNo}`);
}// end of function viewProduct(prodNo) --------------------


