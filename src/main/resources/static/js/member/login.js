function login(){
    //유효성 검사 : 이메일 인증 여부
    $.ajax({
        url : "/member/authmailcheck",
        data : { mid : $("#mid").val() },
        success : function(re){
            if(re == 1){
                $("#loginform").submit(); //전송
            } else if(re == 2 || re == 3) {
                alert("간편 로그인을 이용해주세요.")
            } else {
                alert("이메일 인증 후 다시 시도해주시기 바랍니다.");
            }
        }
    });

//    $.ajax({
//        url : "/member/logincontroller",
//        method : "POST",
//        data : {"mid" : $("#mid").val() , "mpw" : $("#mpw").val() },
//        success : function(re){
//            if(re == true){
//                alert("로그인 성공");
//                location.href="/"; //메인페이지로 매핑
//            } else { alert("로그인 실패"); }
//        }
//    });
}