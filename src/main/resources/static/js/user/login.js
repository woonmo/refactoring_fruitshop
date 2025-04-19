const loginButton = document.getElementById('btnLogin');

// 로그인 버튼 클릭 시
if (loginButton) {
    loginButton.addEventListener('click', (e) => {
       login();
    });
}


// 엔터키 입력 시
const inputPassword = document.getElementById("loginPasswd");
inputPassword.addEventListener('keyup', (e) => {
    if (e.keyCode === 13) {
        login();
    }
})



function login() {
    const userid   = document.getElementById('loginUserid');
    const password = document.getElementById('loginPasswd');

    // 아이디를 입력하지 않았을 경우
    if (userid.value.trim() == "") {
        Swal.fire({
            title: '아이디를 입력하세요!',
            icon: 'info',
            confirmButtonText: "확인"
        })
            .then(() => {
                userid.value = "";
                userid.focus();
            })
        return false;
    }

    // 비밀번호를 입력하지 않았을 경우
    if (password.value == "") {
        Swal.fire({
            title: '비밀번호를 입력하세요!',
            icon: 'info',
            confirmButtonText: "확인"
        })
            .then(() => {
                password.value = "";
                password.focus();
            })
        return false;
    }
    document.loginFrm.submit(); // 로그인 이동
}