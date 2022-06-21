

board_list(1);
category_list();

//2. R 출력 //service 다 적고 넘어옴 (html과 연계)
function board_list(cno){
        $.ajax({
            url : "/board/getboardlist" ,
            data : {"cno" : cno},
            method : "GET",
            success : function( boardlist ){
//                console.log(boardlist);
//                let html = $("#boardtable").html();
    let html = '<tr><th width="10%">번호</th><th width="50%">제목</th><th width="10%">작성일</th>'+
        '<th width="10%">조회수</th><th width="10%">좋아요</th><th width="10%">작성자</th></tr>';
        //헤더 깔아주기

                for( let i = 0 ; i<boardlist.length ; i++ ){

                    html +=
        '<tr>'+
            '<td>'+boardlist[i].bno+'</td> '+
            '<td><a href="/board/view/'+boardlist[i].bno+'">'+boardlist[i].btitle+'<a></td> '+
//                   '<td><span onclick="view('+boardlist[i].bno+')">'+boardlist[i].btitle+'<span></td>'+
            '<td>'+boardlist[i].bindate+'</td>'+
            '<td>'+boardlist[i].bview+'</td>'+
            '<td>'+boardlist[i].blike+'</td>'+
            '<td>'+boardlist[i].mid+'</td>'+
         '</tr>';
            }
            $("#boardtable").html( html );
        }
    });
}


function category_list() {
    $.ajax({
        url : '/board/getcategorylist',
        success : function(categorylist){
//            console.log(categorylist);
            let html = "";
            for(let i = 0 ; i<categorylist.length; i++){
            html +=
                '<button onclick="board_list('+categorylist[i].cno+')">'+categorylist[i].cname+'</button>'
            }
            $("#categorybox").html( html );
        }
    });
}

function selectcategory(cno) {
    alert(cno);
}












//view 열기
//function view(bno){
//    $.ajax({
//        url : "/board/view/"+bno,
//        method : "GET",
//        success : function(re){
//            $("#boardlist").html(re);        }     }); }

