
function member_delete(){
    $.ajax({
        url : '/member/delete',
        method : 'DELETE',
        data : {"mpw":$("#mpw").val()},
        success : function(re){
        alert(re);
            if(re == true){
                alert("회원탈퇴 성공");
                location.href = "/member/logout";
            } else {
            }
        }
    });
}