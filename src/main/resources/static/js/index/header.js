$("a.menu").hover(function(e) {
    //alert("야야호");
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