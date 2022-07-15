
///////////////////////////검색 관련 메소드 들

//0. 검색옵션 json 객체
let searchobject = {
    'trans' : 0,
    'minprice' : 0,
    'maxprice' : 1000000,
}

let changepricelist = $("input[name='pricebtn']");
changepricelist[0].value = 0;
changepricelist[1].value = 1000000;


//1. 거래방식 버튼을 클릭했을 때
function changetrans(){
    searchobject['trans'] = value; //지도한테 보내서 처리

//    $("tag명[name='이름']"); //여러개의 tag명 name 배열 가져오기
    let transbtnlist = $("button[name='transbtn"]);

    for(let i = 0; i<transbtnlist.length; i++){
        transbtnlist[i].classList.remove('btn-warning'); //기본적으로 remove로 놓기
        transbtnlist[i].classList.remove('btn-secondary');
            //반복문 돌면서 색이 변한다.
        if(i == value){ //선택된 버튼 클래스 교체[추가/삭제]
            //transbtnlist[i].classList; //해당 tag의 모든 class명 호출 가능
            transbtnlist[i].classList.add('btn-warning');
                            // .remove/add('클래스명','클래스명');
        } else { //선택 안 된 버튼 클래스 교체
            transbtnlist[i].classList.add('btn-secondary');
        }
    }
    console.log(searchobject);
}

//2. 가격변동이 있을 때
function changeprice(){
    let minprice = changepricelist[0].value;
    let maxprice = changepricelist[1].value;

    if(minprice > maxprice){ //만약 최소 금액 더 크면
        alert("최소 금액이 더 큽니다.")
        changepricelist[0].value = maxprice;
        minprice = maxprice;
    }

    //옵션 객체 담기
    searchobject['maxprice'] = maxprice;
    searchobject['minprice'] = minprice;

    //html 금액 표시
        //억 단위 계산
    let minpricemillion = (minprice/10000).toFixed(1); //소수점 첫째짜리까지
    let maxpricemillion = (minprice/10000).toFixed(1); //소수점 첫째짜리까지


    if(minpricemillion >= 1){ minpricemillion = minpricemillion +"억원";
    } else {  minpricemillion = minpricemillion +"만원";  }

    if(maxpricemillion >= 1){ maxpricemillion = maxpricemillion +"억원";
    } else {  maxpricemillion = maxpricemillion +"만원";  }

    $("#pricebox").html(minpricemillion+"~"+maxpricemillion+"원");

    console.log(searchobject);
}

//3. 초기화 버튼을 눌렀을 때
function searchreset(){
    searchobject['trans'] = 0;
    changepricelist[0].value = 0;
    changepricelist[0].value = 100000;
    changeprice();

    console.log(searchreset);
}



