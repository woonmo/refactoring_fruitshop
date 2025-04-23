document.querySelector('input[name="userid"]').focus();


// 아이디 입력 검사
const $userid = document.querySelector('input[name="userid"]');
const $email = document.querySelector('input[name="email"]');
let useridCheck = false;
$userid.addEventListener("blur", e => {
    const userid = $userid.value.trim();
    const email  = $email.value.trim() || "";
    // console.log("userid : ", userid);

    if(userid == "") {
        const $span = $userid.parentElement.querySelector("span.error");
        $span.innerHTML = "아이디를 입력하세요."
        $span.classList.add("red");
        $span.classList.remove("blue");
        return;
    }

    const regExp_userid = new RegExp(/^[a-z0-9][a-z0-9_]{4,15}$/);
    const bool = regExp_userid.test(userid);

    if(!bool) {
        const $span = $userid.parentElement.querySelector("span.error");
        $span.innerHTML = "올바른 아이디가 아닙니다.";
        $span.classList.add("red");
        $span.classList.remove("blue");
        return;
    }

    function success(json) {
        if(json.isUserIdExist) {
            const $span = $userid.parentElement.querySelector("span.error");
            $span.innerHTML = `${userid} 은 이미 사용중입니다.`;
            $span.classList.add("red");
            $span.classList.remove("blue");
        }
        else {
            const $span = $userid.parentElement.querySelector("span.error");
            $span.innerHTML = `${userid} 은 사용 가능합니다.`;
            $span.classList.add("blue");
            $span.classList.remove("red");
            useridCheck = true;
        }
    }

    function fail(err) {
        Swal.fire({
            icon: "error",
            title: "에러가 발생했습니다.",
            text: err
        });
    }

    const body = JSON.stringify({"userId": userid, "email": email});

    httpRequest("/api/users/duplicate", "POST", body, success, fail);
});// end of $userid.addEventListener("blur", e => {}) ----------------------



// 비밀번호
const $passwd = document.querySelector('input#passwd');
$passwd.addEventListener("blur", e => {
    const password = $passwd.value;
    const regExp_passwd = new RegExp(/^.*(?=^.{8,20}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&*+=-]).*$/g);
    const $span = document.querySelector("span#pwdError");

    if (!regExp_passwd.test(password)) {
        $span.innerHTML = "올바른 비밀번호가 아닙니다.";
        $span.classList.add("red");
    }
    else {
        $span.innerHTML = "";
        $span.classList.remove("red");
    }
});


// 비밀번호 확인
const $passwdcheck = document.querySelector('input#passwdcheck');
let passwordCheck = false;
$passwdcheck.addEventListener("blur", e => {
    const password = $passwd.value;
    const passwordcheck = $passwdcheck.value;
    const $span = document.querySelector("span#pwdError");

    const regExp_passwd = new RegExp(/^.*(?=^.{8,20}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&*+=-]).*$/g);

    if (!regExp_passwd.test(password)) {
        $span.innerHTML = "올바른 비밀번호가 아닙니다.";
        $span.classList.remove("blue");
        $span.classList.add("red");
    }
    else {
        if (password != passwordcheck) {
            $span.innerHTML = "비밀번호가 일치하지 않습니다.";
            $span.classList.remove("blue");
            $span.classList.add("red");
        }
        else {
            $span.innerHTML = "비밀번호가 일치합니다.";
            $span.classList.remove("red");
            $span.classList.add("blue");
            passwordCheck = true;
        }
    }
});


// 이름
const $name = document.querySelector("input[name='name']");
let nameCheck = false;
$name.addEventListener("blur", e => {
    const name = $name.value.trim();
    const nameReg = /^[가-힣]{2,6}$/;
    const $span = document.querySelector("span#nameError");

    if (name == "") {
        $span.innerHTML = "이름을 입력하세요.";
        $span.classList.add("red");
        return;
    }

    if (!nameReg.test(name)) {
        $span.innerHTML = "올바른 성명이 아닙니다.";
        $span.classList.add("red");
    }
    else {
        $span.innerHTML = "";
        $span.classList.remove("red");
        nameCheck = true;
    }
});


// 이메일
let emailCheck = false;
$email.addEventListener("blur", e => {
    const userid = $userid.value.trim() || "";
    const email = $email.value.trim();
    const emailReg = new RegExp(/^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i);
    const $span = document.querySelector("span#emailError");

    if (!emailReg.test(email)) {
        $span.innerHTML = "올바른 이메일이 아닙니다.";
        $span.classList.remove("blue");
        $span.classList.add("red");
        return;
    }

    function success(json) {
        if(json.isEmailExist) {
            $span.innerHTML = `${email} 은 이미 사용중입니다.`;
            $span.classList.add("red");
            $span.classList.remove("blue");
        }
        else {
            $span.innerHTML = `${email} 은 사용 가능합니다.`;
            $span.classList.add("blue");
            $span.classList.remove("red");
            emailCheck = true;
        }
    }

    function fail(err) {
        Swal.fire({
            icon: "error",
            title: "에러가 발생했습니다.",
            text: err
        });
    }

    const body = JSON.stringify({"userId": userid, "email": email});

    httpRequest("/api/users/duplicate", "POST", body, success, fail);
});


// 전화번호 가운데
const $hp2 = document.querySelector('input[name="tel2"]');
const $hp3 = document.querySelector('input[name="tel3"]');
$hp2.addEventListener("keyup", e => {
    const hp2 = $hp2.value.trim();
    if (hp2.length == 4) {
        $hp3.focus();
    }
})

// 전화번호 마지막
let telCheck = false;
$hp3.addEventListener("blur", e => {
    const hp2 = $hp2.value.trim();
    const hp3 = $hp3.value.trim();
    const $span = document.querySelector('span#telError');

    const regExp_hp2 = new RegExp(/^[1-9][0-9]{3}$/);
    const regExp_hp3 = new RegExp(/^\d{4}$/);

    if (!regExp_hp2.test(hp2) || !regExp_hp3.test(hp3)) {
        $span.innerHTML = "올바른 연락처가 아닙니다.";
        $span.classList.add("red");
    }
    else {
        $span.innerHTML = "";
        $span.classList.remove("red");
        telCheck = true;
    }
});


// 우편번호 찾기 이미지 클릭 시
document.querySelector('img#zipcodeSearch')
.addEventListener("click", e => {
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

let addressCheck = false;
document.querySelector('input#detailAddress')
    .addEventListener("blur", e => {
        const address = document.querySelector('input#address').value;
        if (address != "") {
            document.querySelector('span#addressError').innerHTML = "";
            document.querySelector('span#addressError').classList.remove("red");
            addressCheck = true;
        }
    });



// 생년월일 클릭시 달력나오는 Jquery의 datepicker() 사용
$("input#datepicker").datepicker({

    dateFormat: 'yy-mm-dd'
    ,showOtherMonths: true
    ,showMonthAfterYear:true
    ,changeYear: true
    ,changeMonth: true
    ,yearSuffix: "년"
    ,monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
    ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월']
    ,dayNamesMin: ['일','월','화','수','목','금','토']
    ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일']
    ,maxDate:"today"
    ,yearRange:"1900:today"
    ,showButtonPanel:true
    ,closeText: "닫기"
    ,currentText: "오늘"
});


// 회원가입 요청하는 함수
function goRegister() {

    // 아이디 검사
    if (!useridCheck) {
        const $span = document.querySelector('span#useridError');
        $span.innerHTML = "올바른 아이디가 아닙니다.";
        $span.classList.add("red");
        $span.classList.remove("blue");
        return;
    }

    // 비밀번호 검사
    if(!passwordCheck) {
        const $span = document.querySelector('span#pwdError');
        $span.innerHTML = "비밀번호가 일치하지 않습니다.";
        $span.classList.add("red");
        $span.classList.remove("blue");
        return;
    }


    // 성명 검사
    if(!nameCheck) {
        const $span = document.querySelector("span#nameError");
        $span.innerHTML = "올바른 성명이 아닙니다.";
        $span.classList.add("red");
        return;
    }


    // 이메일을 입력했는지 검사
    if(!emailCheck) {
        const $span = document.querySelector("span#emailError");
        $span.innerHTML = "올바른 이메일이 아닙니다.";
        $span.classList.add("red");
        return;
    }


    // 연락처를 입력했는지 검사
    if(!telCheck) {
        const $span = document.querySelector('span#telError');
        $span.innerHTML = "올바른 연락처가 아닙니다.";
        $span.classList.add("red");
        return;
    }


    // 주소 입력했는지 검사
    if(!addressCheck) {
        const $span = document.querySelector('span#addressError');
        $span.innerHTML = "주소를 입력하세요.";
        $span.classList.add("red");
        return;
    }

    // 성별 입력했는지 검사
    const gender_check = $("input:radio[name='gender']:checked").length;
    if(gender_check == 0) {
        $("input:radio[name='gender']").parent().find("span.error").html("성별을 선택하세요.").addClass("red");
        return;
    }
    else {
        $("input:radio[name='gender']").parent().find("span.error").html("").removeClass("red");
    }

    // 생년월일 입력했는지 검사
    const birthday =  $("input#datepicker").val();
    const regExp_birthday = new RegExp(/^\d{4}-\d{2}-\d{2}$/);
    regbool = regExp_birthday.test(birthday);
    if(!regbool) {
        $("input#datepicker").parent().find("span.error").html("생년월일을 입력하세요.").addClass("red");
        return;
    }
    else {
        $("input#datepicker").parent().find("span.error").html("").removeClass("red");
    }

    // 약관 동의 했는지 검사
    if($("input:checkbox[id='agree']:checked").length == 0) {
        $("span#agreecheck").html("약관 동의는 필수사항입니다.").addClass("red");
        return;
    }
    else {
        $("span#agreecheck").html("").removeClass("red");
    }

    function success(data) {
        Swal.fire({
            icon: "success",
            title: "회원가입에 성공했습니다.",
            text: `회원 아이디: ${data.userId}`
        })
            .then(() => {
                window.location.replace("/");
            });

    }

    function fail(err) {
        Swal.fire({
            icon: "error",
            title: "회원가입 중 에러가 발생했습니다.",
            text: err
        })
    }

    const body = getUserInput();

    // console.log(body);
    /*
        {"userid":"user11234","password":"qwer1234$","email":"test@test.com","name":"테스트","tel":"010-2332-4324","gender":"여","zipcode":"15517","address":"경기 안산시 상록구 상록수로 61","detailAddress":"김밥","extraAddress":" (본오동)","birthday":"2014-04-02"}
     */
    httpRequest("/api/users", "POST", body, success, fail);
}// end of function goRegister() ------------------


// 입력한 회원의 정보를 리턴하는 함수
function getUserInput() {
    const userId = $userid.value.trim();
    const password = $passwd.value;
    const email = $email.value.trim();
    const name = $name.value.trim();

    const hp1 = document.querySelector('select#tel1').value;
    const hp2 = $hp2.value;
    const hp3 = $hp3.value;
    const tel = hp1 +"-"+ hp2 +"-"+ hp3;

    const gender = document.querySelector('input[name="gender"]:checked').value;

    const zipcode = document.querySelector('input#postcode').value;
    const address = document.querySelector('input#address').value;
    const detailAddress = document.querySelector('input#detailAddress').value || "";
    const extraAddress = document.querySelector('input#extraAddress').value;

    const birthday = document.querySelector('input[name="birthday"]').value;

    const userInfo = {
        "userid": userId,
        "password": password,
        "email": email,
        "name": name,
        "tel": tel,
        "gender": gender,
        "zipcode": zipcode,
        "address": address,
        "detailAddress": detailAddress,
        "extraAddress": extraAddress,
        "birthday": birthday
    }

    return JSON.stringify(userInfo);
}// end of function getUserInput() -----------
