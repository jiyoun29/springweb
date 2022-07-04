


function idfind(){
    $.ajax({
        url : "/member/idfind",
        data : {"mname" : $("#idmname").val() , "memail" : $("#idmemail").val() } ,
        success : function(re){
            if(re == ""){ alert("동일한 회원 정보가 없습니다.");
            } else {
                $("#findidbox").css("display","block");
            }
        }
    });
}

function pwfind(){
    $.ajax({
        url : "/member/pwfind",
        data : {"mid" : $("#pwmid").val() , "memail" : $("#pwmemail").val() } ,
        success : function(re){
            if(re == true){  //맞으면
                alert("해당 이메일로 임시 비밀번호 전송");
            } else { //아니면
                alert("동일한 회원 정보가 없습니다.") ;
            }
        }
    });
}
