
//html과 맞운 다음 boardcontroller 맞춰서 진행

//1. C 쓰기
function board_save(){
    //폼으로 한번에 받기
    let form = $("#saveform")[0];
    let formdata = new FormData(form);

    $.ajax({
        url : "/board/save",
        data : formdata,
        method : "POST",
        processData : false,
        contentType : false,
        success : function(re){
            alert(re);
        }
    });
}
