

//write html에서 받음
function signup(){

    alert("작동");

    //1. 폼 가져오기
    //데이터만 빼오기, 폼 객체화
    let form = $("signupform")[0];
    let formdata = new FormData(form);

    $.ajax({
        url : "/member/signup",
        method : "POST",
        data : formdata,
        contentType : false,
        processData : flase,
        success : function(re){ alert(re);
            if(re == 1){ alert("회원가입 성공")
                location.href = "member/login";
            } else { alert("회원가입 실패 [서비스 오류]") }
        }
    });
}