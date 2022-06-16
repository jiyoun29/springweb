function login(){

    $.ajax({
        url : "/member/login",
        method : "POST",
        data : {"mid" : $("#mid").val() , "mpw" : $("#mpw").val() },
        success : function(re){
            if(re == true){
                alert("로그인 성공");
                location.href="/"; //메인페이지로 매핑
            } else { alert("로그인 실패"); }
        }
    });
}