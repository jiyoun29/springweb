
//마커클러스터 지도사용


//0. 현재 내 위치 위도 경도 구하기
    navigator.geolocation.getCurrentPosition(function(position) {
        var lat = position.coords.latitude, // 위도
            lon = position.coords.longitude; // 경도
    //전부 감싸기 (지금 잠깐 닫음 열려면 맨 아래에 } 넣기)
//배포에서는 0번 삭제, 1번의 lat lon에 좌표 임시로 넣기


//1.log map 변수
 var map = new kakao.maps.Map(document.getElementById('map'), { // 지도를 표시할 div
        center : new kakao.maps.LatLng(lat , lon), // 지도의 중심좌표 //현재 접속된 디바이스 좌표 37.334659113378, 126.85837456757
        level : 5 // 지도의 확대 레벨
    });



//2. 클러스터 [마커 짖ㅂ합] 변수
    // 마커 클러스터러를 생성합니다
    // 마커 클러스터러를 생성할 때 disableClickZoom 값을 true로 지정하지 않은 경우
    // 클러스터 마커를 클릭했을 때 클러스터 객체가 포함하는 마커들이 모두 잘 보이도록 지도의 레벨과 영역을 변경합니다
    // 이 예제에서는 disableClickZoom 값을 true로 설정하여 기본 클릭 동작을 막고
    // 클러스터 마커를 클릭했을 때 클릭된 클러스터 마커의 위치를 기준으로 지도를 1레벨씩 확대합니다
    var clusterer = new kakao.maps.MarkerClusterer({
        map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
        averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
        minLevel: 6, // 클러스터 할 최소 지도 레벨
        disableClickZoom: true // 클러스터 마커를 클릭했을 때 지도가 확대되지 않도록 설정한다
    });

    // 데이터를 가져오기 위해 jQuery를 사용합니다
    // 데이터를 가져와 마커를 생성하고 클러스터러 객체에 넘겨줍니다
        //$.get("통신할 URL", function(반환인수))

        //3. d여러개 Map -> 클러스터 저장 [모든 지도 좌표 가져오기]
//    $.get("/room/roomlist", function(data) { //매핑생성****필요****
////        console.log(data); //통신확인
//
//        // 데이터에서 좌표 값을 가지고 마커를 표시합니다
//        // 마커 클러스터러로 관리할 마커 객체는 생성할 때 지도 객체를 설정하지 않습니다
//        var markers = $(data.positions).map(function(i, position) {
//            return new kakao.maps.Marker({
//                position : new kakao.maps.LatLng(position.lat, position.lng)
//            });
//        });
//
//        // 클러스터러에 마커들을 추가합니다
//        clusterer.addMarkers(markers);
//    });

    //6. 마커 이미지 변경
    // 마커 이미지의 주소
    var markerImageUrl = 'http://192.168.17.148:8081/img/4481380.png', //배포시 배포주소로 바꾸기
        markerImageSize = new kakao.maps.Size(40, 42), // 마커 이미지의 크기
        markerImageOptions = {
            offset : new kakao.maps.Point(20, 42)// 마커 좌표에 일치시킬 이미지 안의 좌표
        };

    // 마커 이미지를 생성한다
    var markerImage = new kakao.maps.MarkerImage(markerImageUrl, markerImageSize, markerImageOptions);


    //4. 지도 완료
    // 지도 시점 변화 완료 이벤트를 등록한다 [ idle(드래그 완료마다) vs bounds_changed(드래그 중에) ]
    kakao.maps.event.addListener(map, 'idle', function () {

    //클러스터 초기화
        clusterer.clear();

        //사이드바에 넣을 html 변수 선언
        let html = "";

    $.ajax({
        url : '/room/roomlist' ,
        data : JSON.stringify(  map.getBounds() ) , // 현재 보고 있는 지도 범위 [ 동서남북 좌표 ]
        method : 'POST' ,
        contentType : 'application/json' ,
        success : function( data ){
                console.log(data); // 통신 확인
            //만약에 데이터가 없으면 메시지를 사이드바에 띄우기
            if(data.positions.length == 0){
                html += '<div>검색된 방이 없습니다.</div>'
            }


        //마커 생성
          // 데이터에서 좌표 값을 가지고 마커를 표시합니다
          // 마커 클러스터러로 관리할 마커 객체는 생성할 때 지도 객체를 설정하지 않습니다
          var markers = $(data.positions).map(function(i, position) {


            //마커 하나 생성
            var marker = new kakao.maps.Marker({
                  position : new kakao.maps.LatLng(position.rlon, position.rlat) ,
                   image : markerImage // 마커의 이미지
              });

                // 마커에 클릭 이벤트를 등록한다 (우클릭 : rightclick)
                kakao.maps.event.addListener(marker, 'click', function() {
                    getroom(position.rno);
               });

                //사이드바에 추가할 html 구성
                html +=
                    '<div class="row" onclick="getroom('+position.rno+')"> <div class="col-md-6">'+
                            '<img src="/upload/'+position.rimg+'" width="100%">'+
                       '</div>  <div class="col-md-6">'+
                           '<div>집번호 : <span>'+position.rno+'</span></div> <br>'+
                           '<div>집이름 :<span>'+position.rtitle+'</span> </div><br>'
                    +'</div> </div>';

                return marker; //변수 만들고 리턴
           //마커 하나 생성 end

          }); //marker end

        // 클러스터러에 마커들을 추가합니다
        clusterer.addMarkers(markers);

            //반복문이 끝내고 해당 html을 해당 id에 추가
            $("#sidebar").html(html);

        } //success end
    }); //ajax end


//        var mapBounds = map.getBounds(),
//            message = '지도의 남서쪽, 북동쪽 영역좌표는 ' +
//                        mapBounds.toString() + '입니다.';
//        console.log(mapBounds);
    }); //이벤트 end


    //두가지 섞기

    // 지도 영역 변화 이벤트를 등록한다 [움직일때마다]
//    kakao.maps.event.addListener(map, 'bounds_changed', function () {
//        console.log(message);    });



    //5. 클릭 이벤트
    // 마커 클러스터러에 클릭이벤트를 등록합니다
    // 마커 클러스터러를 생성할 때 disableClickZoom을 true로 설정하지 않은 경우
    // 이벤트 헨들러로 cluster 객체가 넘어오지 않을 수도 있습니다
    kakao.maps.event.addListener(clusterer, 'clusterclick', function(cluster) {

        // 현재 지도 레벨에서 1레벨 확대한 레벨
        var level = map.getLevel()-1;

        // 지도를 클릭된 클러스터의 마커의 위치를 기준으로 확대합니다
        map.setLevel(level, {anchor: cluster.getCenter()});
    });


}); //현재 내 위치의 위도,경도 구하기 and


//모달에 특정 방 정보 출력 메소드
//메소드화
function getroom(rno){

     //해당 모달에 데이터 넣기
        $.ajax({
            url : "/room/getroom",
            method : "GET",
            data : {"rno":rno},
            success : function(room){
                    let imgtag = "";
                //응답 받은 데이터를 모달 데이터에 넣기
                console.log(room.rimglist);

                for(let i = 0 ; i<room.rimglist.length ; i++){
                    //반복문 돌면서 div 추가
                    if(i == 0){ //첫번째 이미지에 active 속성 추가
                        imgtag +=
            '<div class="carousel-item active">'+
            '<img src="/upload/'+room.rimglist[i]+'" class="d-block w-100" alt="..."> </div>';
                    } else{
                        imgtag +=
             '<div class="carousel-item">'+
             '<img src="/upload/'+room.rimglist[i]+'" class="d-block w-100" alt="..."> </div>';
                     }
            }
      $("#modalimglist").html( imgtag );
                             // 모달 띄우기
                             $("#modalbtn2").click();
            }
        });

