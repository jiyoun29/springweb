

board_list();




//2. R 출력 //service 다 적고 넘어옴 (html과 연계)
function board_list(){
        $.ajax({
            url : "/board/getboardlist" ,
            method : "GET",
            success : function( boardlist ){
                console.log(boardlist);
                let html = $("#boardtable").html();

                for( let i = 0 ; i<boardlist.length ; i++ ){
                    html +=
            '<tr>'+
                    '<td>'+boardlist[i].bno+'</td> '+
                    '<td><a href="/board/view/'+boardlist[i].bno+'" style="text-decoration: none;">'+boardlist[i].btitle+'<a></td> '+
//                   '<td><span onclick="view('+boardlist[i].bno+')">'+boardlist[i].btitle+'<span></td>'+
                    '<td>'+boardlist[i].bdate+'</td>'+
                    '<td>'+boardlist[i].bview+'</td>'+
                    '<td>'+boardlist[i].blike+'</td>'+
             '</tr>';
                }
                $("#boardtable").html( html );
            }
        });
}


//view 열기
//function view(bno){
//    $.ajax({
//        url : "/board/view/"+bno,
//        method : "GET",
//        success : function(re){
//            $("#boardlist").html(re);        }     }); }


