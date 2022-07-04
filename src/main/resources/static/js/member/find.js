


function idfind(){
    $.ajax({
        url : "/member/idfind",
        data : {"mname" : $("#idmname").val() , "memail" : $("#idmemail").val() } ,
        success : function(re){
            alert( re +"결과확인" );
        }
    });
}

function pwfind(){
    $.ajax({
        url : "/member/pwfind",
        data : {"mid" : $("#pwmid").val() , "memail" : $("#pwmemail").val() } ,
        success : function(re){
            alert( re +"결과확인" );
        }
    });
}
