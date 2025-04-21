// 에러 메시지 숨기기
document.querySelectorAll("span.error").forEach(element => element.style.display = "none");

// 쿠폰 테이블 숨김
const couponTable = document.querySelector("table#couponSelect");
if (couponTable) {
    couponTable.style.display = "none"; // 페이지 진입 시 숨김
}

getTotalPrice();
paymentPrice()

// 주문상품 총액을 구하는 함수
function getTotalPrice() {
    // 총 상품 가격 계산
    const rows = document.querySelectorAll('.productRow');  // 테이블 태그의 tr(각 상품)
    let totalPrice = 0;

    rows.forEach(row => {
        const price = parseInt(row.querySelector('.price').textContent.replace(/[^0-9]/g, '')) || 0;
        totalPrice += price;
    });

    // 적립 포인트 계산
    const point = Math.floor(totalPrice * 0.01);

    // 화면에 표시
    document.querySelector('span#total_oprice').textContent = formatPrice(totalPrice);
    document.querySelector('span#point').textContent = formatPrice(point);
}// end of function getTotalPrice() -------------------


// 금액을 텍스트 형태의 format 으로 변환해주는 함수
function formatPrice(price) {
    return price.toLocaleString() + " 원";
}



// 최종 결제 예정 금액을 구하는 함수
function paymentPrice() {
    // 상품 총 가격
    const totalPrice = parseInt(document.querySelector('span#total_oprice').textContent.replace(/[^0-9]/g, '')) || 0;
    // 할인액
    const discount = parseInt(document.querySelector('span#discount').textContent.replace(/[^0-9]/g, '')) || 0;
    // 배송비
    const deliveryFee = 2500;

    const paymentPrice = totalPrice + deliveryFee - discount;

    // 화면에 표시
    const $paymentPrice =  document.querySelectorAll('span.total_price');
    $paymentPrice.forEach(element => element.textContent = formatPrice(paymentPrice));
}// end of function paymentPrice() -------------------



// 우편번호 찾기 버튼 클릭 시
document.querySelector('span.btn-outline-secondary')
    .addEventListener('click', e => {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                let addr = ''; // 주소 변수
                let extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    // 조합된 참고항목을 해당 필드에 넣는다.
                    document.getElementById("extraAddress").value = extraAddr;

                } else {
                    document.getElementById("extraAddress").value = '';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('postcode').value = data.zonecode;
                document.getElementById("address").value = addr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("detailAddress").focus();
            }
        }).open();
    });


// 결제 진행 시 주문 상품명을 만들어주는 함수
function getOrderName() {
    const rows = document.querySelectorAll('.productRow');  // 테이블 태그의 tr(각 상품)

    if (rows.length > 1) {
        // 주문 상품이 두 개 이상이라면
        const productName = rows[0].querySelector('.prod_name').textContent;
        return `${productName} 외 ${rows.length - 1} 건`
    }

    const productName = rows[0].querySelector('.prod_name').textContent;
    return productName
}// end of function getOrderName() ------------------------


// 배송지 입력 정보를 확인하는 함수
function shipInfoValidation() {
    const name = $("input#name").val().trim();
    if (name == "") {
        Swal.fire({
            icon: "warning",
            title: "받는사람은 필수 입력사항입니다."
        });
        return false;
    }

    const postcode = $("input#postcode").val().trim();
    const address = $("input#address").val().trim();
    const extraAddress = $("input#extraAddress").val().trim();

    if (postcode == "" || address == "" || extraAddress == "") {
        Swal.fire({
            icon: "warning",
            title: "주소 정보는 필수 입력사항입니다."
        });
        return false;
    }

    const regExp_hp2 = new RegExp(/^[1-9][0-9]{3}$/);
    const regExp_hp3 = new RegExp(/^[\d]{4}$/);
    const hp2 = $("input#hp2").val().trim();
    const hp3 = $("input#hp3").val().trim();

    if (!regExp_hp2.test(hp2) || !regExp_hp3.test(hp3)) {
        // 연락처 국번이 형식에 맞지 않은 경우
        Swal.fire({
            icon: "warning",
            title: "올바른 연락처를 입력하세요."
        });
        return false;
    }

    const regExp_email = new RegExp(/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i);
    const email = $("input#email").val().trim();

    if (!regExp_email.test(email)) {
        Swal.fire({
            icon: "warning",
            title: "올바른 이메일을 입력하세요."
        });
        return false;
    }

    return true;
}// end of function shipInfoValidation()


// 결제하기 버튼 클릭 시
async function paymentInit() {

    // 배송 정보 입력을 확인한다.
    const isValid = shipInfoValidation();

    if (!isValid) {
        return;
    }

    // 주문자 정보
    const user = await getUserInfo();
    const ordererName = user.name;
    const ordererEmail = user.email;
    const ordererTel = user.tel;


    var IMP = window.IMP;     // 생략가능
    IMP.init('imp38080720');  // 중요!!  아임포트에 가입시 부여받은 "가맹점 식별코드".

    // 결제요청하기
    IMP.request_pay({
        pg : 'html5_inicis', // 결제방식 PG사 구분
        pay_method : 'card',	// 결제 수단
        merchant_uid : 'fruit_' + new Date().getTime(), // 가맹점에서 생성/관리하는 고유 주문번호
        name : getOrderName(),   // 주문페이지 상품명
        amount : 100,  // '${coinmoney}'  결제 금액 number 타입. 필수항목.
        buyer_email : ordererEmail,  // 구매자 email
        buyer_name : ordererName,	   // 구매자 이름
        buyer_tel : ordererTel,   // 구매자 전화번호 (필수항목)
        buyer_addr : '',
        buyer_postcode : '',
        m_redirect_url : ''  // 휴대폰 사용시 결제 완료 후 action : 컨트롤러로 보내서 자체 db에 입력시킬것!
    }, function(rsp) {


        if ( rsp.success ) { // PC 데스크탑용
            // === 결제 성공 시 주문 테이블에 insert 완료 후 주문상세, 결제 테이블에 정보를 insert 한다 === //

            var msg = '결제가 완료되었습니다.';
            msg += '고유ID : ' + rsp.imp_uid;
            msg += '상점 거래ID : ' + rsp.merchant_uid;
            msg += '결제 금액 : ' + rsp.paid_amount;
            msg += '카드 승인번호 : ' + rsp.apply_num;

            alert(msg);

        } else {
            // 취소했을 경우
            alert("결제에 실패하였습니다.");
        }

    }); // end of IMP.request_pay()----------------------------
}// end of function paymentInit() ----------------------


// 배송지 입력 메뉴 핸들링
const shipMenu = document.querySelectorAll('div.shipInput_title');
shipMenu.forEach(menu => {
   menu.addEventListener('click', e => {
       shipMenu.forEach(element=> element.classList.remove('active'));
        menu.classList.add('active');

        const index = getIndex(menu, shipMenu);

        switch (index) {
            case 0: // 기본 배송지
                alert("기본 배송지");
                break;
            case 1: // 회원 정보 사용
                useUserInfo().then(response => {return});
                break;
            case 2: // 배송지 선택
                alert("배송지 선택");
                break;
            case 3: // 직접 입력
                useWriteInfo();
                break;
        }
   });
});// end of shipMenu.forEach(menu => {}) ----------------------


// 여러 선택자 중 인덱스 정보를 알아오는 함수
function getIndex(targetElement, nodeList) {
    const elementsArray = Array.from(nodeList); // NodeList 를 배열로 변환

    return elementsArray.findIndex(element => element === targetElement); // findIndex 로 인덱스 찾기
}// end of function getIndex(targetElement, selector) ----------------------


// 로그인한 회원정보를 가져오는 함수
async function getUserInfo() {
    const response = await fetch("/api/users/me", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    });

    const user = await response.json();
    return user;
}// end of async function getUserInfo() --------------------


// 회원 정보를 배송 정보로 사용 시 정보를 입력하는 함수
async function useUserInfo() {
    const user = await getUserInfo();
    // console.log("userInfo: "+ JSON.stringify(user));

    // 휴대폰 번호 자르기
    const telParts = user.tel.split("-");

    document.querySelector("input#name").value = user.name;
    document.querySelector("input#postcode").value = user.zipcode;
    document.querySelector("input#address").value = user.address;
    document.querySelector("input#detailAddress").value = user.detailAddress;
    document.querySelector("input#extraAddress").value = user.extraAddress;
    document.querySelector("input#hp2").value = telParts[1];
    document.querySelector("input#hp3").value = telParts[2];

}// end of function useUserInfo() ---------------------------


// 배송 정보를 직접 입력을 원할 경우
function useWriteInfo() {
    document.querySelector("input#name").value = "";
    document.querySelector("input#postcode").value = "";
    document.querySelector("input#address").value = "";
    document.querySelector("input#detailAddress").value = "";
    document.querySelector("input#extraAddress").value = '';
    document.querySelector("input#hp2").value = "";
    document.querySelector("input#hp3").value = "";
}// end of function useUserInfo() --------------------