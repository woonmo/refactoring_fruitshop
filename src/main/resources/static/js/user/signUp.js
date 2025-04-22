document.querySelector('input[name="userid"]').focus();


// 아이디 입력 검사
const $userid = document.querySelector('input[name="userid"]');
const $email = document.querySelector('input[name="email"]');
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
        $span.innerHTML = "올바른 아이디가 아닙니다."
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

    console.log(body);

    httpRequest("/api/users/duplicate", "POST", body, success, fail);
});// end of $userid.addEventListener("blur", e => {}) ----------------------