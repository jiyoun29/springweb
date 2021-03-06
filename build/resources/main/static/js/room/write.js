//전역변수로 임의의 값
//let x = null;
//let y = null;


//저장 메소드
function save(){
//    let roomname = $("#roomname").val();

//1. 폼 가져오기
    let form = $("#saveform")[0]; //인덱스 [] 넣지 않으면 설정값까지 가져오므로 인덱스 가져오기
    let formdata = new FormData(form);

    $.ajax({
        url : "/room/write",
        method : "POST",//putmapping : 등록, 세이브에 자주 사용 (get은 이동)
        data : formdata ,
            // { "roomname" : roomname , "x":x , "y":y },
            //첨부파일 보내려면 컨텐츠 타입 추가
        contentType : false, //첨부파일 전송시 사용되는 속성
        processData : false, //true 넣으면 문자열이 날아간다.
        success : function(re){
            alert("java와 통신 성공");
        }
    });
}




///////////////////////////////////////////////////////////////
// 2. 다음 주소 -> 좌표
    var mapContainer = document.getElementById('map'), // 지도를 표시할 div

        mapOption = {
            center: new daum.maps.LatLng(37.537187, 127.005476), // 지도의 중심좌표
            level: 5 // 지도의 확대 레벨
        };

    //지도를 미리 생성
    var map = new daum.maps.Map(mapContainer, mapOption);
    //주소-좌표 변환 객체를 생성
    var geocoder = new daum.maps.services.Geocoder();
    //마커를 미리 생성
    var marker = new daum.maps.Marker({
        position: new daum.maps.LatLng(37.537187, 127.005476),
        map: map
    });

    function sample5_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                var addr = data.address; // 최종 주소 변수

                // 주소 정보를 해당 필드에 넣는다.
                document.getElementById("sample5_address").value = addr;

                // 주소로 상세 정보를 검색 [  geocoder : 주소 -> 좌표  ]
                geocoder.addressSearch( data.address , function(results, status) {
                    // 정상적으로 검색이 완료됐으면
                    if (status === daum.maps.services.Status.OK) {

                        var result = results[0]; //첫번째 결과의 값을 활용

                        // 해당 주소에 대한 좌표를 받아서
                        var coords = new daum.maps.LatLng(result.y, result.x);
                                // 해당 좌표를 전역변수로 이동
                                $("#x").val(result.x);
                                $("#y").val(result.y);

                        // 지도를 보여준다.
                        mapContainer.style.display = "block";
                        map.relayout();
                        // 지도 중심을 변경한다.
                        map.setCenter(coords);
                        // 마커를 결과값으로 받은 위치로 옮긴다.
                        marker.setPosition(coords)
                    }
                });
            }
        }).open();
    }