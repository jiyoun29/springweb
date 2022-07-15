
getfrommsglist();
function getfrommsglist(){
    $.ajax({
        url : '/member/getfrommsglist',
        success:function(object){
          console.log(object);
            let html="<tr><th>받는사람</th><th>내용</th><th>받은날짜/시간</th></tr>";

            for(let i = 0; i<object.length; i++){
                let color = "";
                if(object[i].isread){color = "#d3d3d3";} //만약에 읽은 메시지이면

                html+= '<tr style="color:'+color+'" onclick="msgread('+object[i].msgno+')">';
                html+= '<td><input type="checkbox"></td>';
                html+= '<td>'+object[i].to+'</td>';
                html+= '<td>'+object[i].msg+'</td>';
                html+= '<td>'+object[i].date+'</td>';
                html+= '/<tr>';
            }

            $("#frommsgtable").html(html);
        }
    });
}

gettomsglist();
function gettomsglist(){
    $.ajax({
        url : '/member/gettomsglist',
        success:function(object){
             console.log(object);
            let html="<tr><th>보낸사람</th><th>내용</th><th>받은날짜/시간</th></tr>";

            for(let i = 0; i<object.length; i++){
                let color = "";
                if(object[i].isread){color = "#d3d3d3";} //만약에 읽은 메시지이면

                html+= '<tr style="color:'+color+'">';
                //id = "중복이름x" class,name ="중복이름o"
                html+= '<td><input name="chkbox" type="checkbox" value="'+object[i].msgno+'" onclick="oncheckbox()"></td>';
                html+= '<td>'+object[i].from+'</td>';
                html+= '<td onclick="msgread('+object[i].msgno+')">'+object[i].msg+'</td>';
                html+= '<td>'+object[i].date+'</td>';
                html+= '/<tr>';
            }
            $("#tomsgtable").html(html);
        }
    });
}

let deletelist = []; //삭제할 쪽지의 번호를 저장하는 배열

function msgdelete(){ //현재 deletelist 배열을 ajax controller 전달
    alert("선택된 메세지를 삭제합니다.");
    $.ajax({
    url: '/member/msgdelete',
    data: JSON.stringify(deletelist),
    method : "DELETE",
    contentType : "application/json",
    success : function(object){
        if(object){ alert("선택된 메세지 삭제 성공");
         getisread(); gettomsglist(); deletelist=[];}
         //안읽은메세지개수, 받은메시지리스트, 삭제리스트 초기화
    }
    });
}



function oncheckbox(){ // 체크박스를 클릭했을때
//모든 체크된 체크박스 값 가져오기
    //1. 모든 체크박스의 객체 호출
    let chkboxlist = $("input[name='chkbox']"); // 위에 있는 체크박스 이름과 동일해야 합니다..~
    //2. 반복문 이용한 체크된 박스의 value 값을 deletelist 저장
    deletelist = []; //삭제리스트 초기화
    for(let i = 0; i < chkboxlist.length; i++){
        if(chkboxlist[i].checked == true){ deletelist.push( chkboxlist[i].value); }  //   코드 확인해보세요~~
    }
}


function msgread( msgno ){ //해당 메세지 내용을 클릭했을때 상세정보 모달 출력
    alert("읽었으면 모달")
    isread(msgno); //읽음 처리
}

function isread(msgno){ //해당 메세지 번호의 읽음처리 업데이트
    alert("수정합니다~~   " + msgno );
    $.ajax({
        url : '/member/isread',
        method : 'put',
        data : {"msgno":msgno} , //   , 빠져 있었네요
        success:function(object){ getisread();  gettomsglist();  }
    });
}

