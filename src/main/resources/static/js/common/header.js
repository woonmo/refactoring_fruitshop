$("a.menu").hover(function(e) {
    $(e.target).css({"color": "red", "transition": '0.75s'});
}, function(e) {
    $(e.target).css({"color": "black", "transition": '0.75s'});
})

function openNav() {
    document.getElementById("mySidenav").style.width = "15%";
}

function closeNav() {
    document.getElementById("mySidenav").style.width = "0";
}

const isLogin = loginData.isLogin;

// console.log(isLogin);



// 데이터베이스에 요청하는 함수
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
                return response.json();
            }
            else {
                throw new Error("요청 실패");
            }
        })
        .then(data => {
            console.log(data);
            success(data);
        })
        .catch(err => {
            fail(err);
        });
}// end of function httpRequest(url, method, body, success, fail) -------------------------

