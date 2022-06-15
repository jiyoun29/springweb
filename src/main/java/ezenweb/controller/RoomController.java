package ezenweb.controller;

import ezenweb.Dto.RoomDto;
import ezenweb.Service.RoomService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/room")
public class RoomController {


    @Autowired
    private RoomService roomService;


    @GetMapping("/write") //1.등록 페이지로 이동
    public String write(){
        return "room/write";
        //templates -> room 안에 -> write.html
    }

    @PostMapping("/write") //2.등록 처리
    @ResponseBody //객체반환 / 템플릿이 아닌 객체 반환시 사용되는 어노테이션
    public boolean write_save(RoomDto roomDto){
        //요청변수 중 dto 필드와 변수 명이 동일할 경우 자동 주입

//        public boolean write_save(@RequestParam("roomname") String roomname ,
//                @RequestParam("x") String x ,
//                @RequestParam("y") String y){ //RequestParam으로 변수 요청
//            //dto 생성
//            RoomDto roomDto = RoomDto.builder()
//                    .roomname(roomname)
//                    .x(x).y(y)
//                    .build();
System.out.println(roomDto.getRtitle());
        //service에 dto 전달
        roomService.room_save(roomDto);

//        System.out.println(roomDto.getRimg().get(0));

        return true;
    }
//
    //3.방 목록 페이지 이동
    @GetMapping("/list")
    public String list() {return "room/list";}

    @PostMapping("/roomlist")
    @ResponseBody //객체반환
    public Map<String, List<Map<String, String>>>
            room_list(@RequestBody Map<String, String> Location ){
                      //인수받기
        System.out.print(Location);

        //바로 리턴해서 사용
        return roomService.room_list(Location);

    }

    @GetMapping("/getroom")
    public JSONObject getroom(@RequestParam("rno") int rno,
                              HttpServletResponse response){
        //값 1개를 요청할때 requestparam. body는 여러개
        //rno을 요청해서 rno로 받음

        //예외처리
        try{
            JSONObject object = roomService.getroom(rno);

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            response.getWriter().println(object); //오브젝트 보내기

        } catch (Exception e){System.out.print(e);}


        //찍히는지 확인
//        roomService.getroom(rno);
//        System.out.println(rno);
        return null;
    }


//    public void roomlist(HttpServletResponse response) {
//        JSONObject object = roomService.room_list();
//      object.put("positions" , jsonArray);
//        try {  response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/json");
//            response.getWriter().print(object); } catch (Exception e){}}



}


/*
-----------------@RequestMapping("경로",method=requestMethod.GET)------------
    GetMapping : find, get 호출용 [  @RequestMapping("경로",method=requestMethod.GET) 와 동일]
    PostMapping : Save  [  @RequestMapping("경로",method=requestMethod.POST) 와 동일]
    PutMapping : update [  @RequestMapping("경로",method=requestMethod.PUT) 와 동일]
    DeleteMapping : delete  [  @RequestMapping("경로",method=requestMethod.DELETE) 와 동일]



    view ----> controller 변수 요청 방식
        1. HttpServletRequest request를 써서 String roomname = request.getParameter("roomname") 으로 요청
        2. @RequestParam("roomname") String roomname 으로 요청(단, 변수 모두 적어야 한다.)
        2-1. @RequestBody : JS -> controller 일때
        3. mapping 사용 시 DTO로 자동 주입 된다. <여러개 쓸 때에는 해당 mapping 사용>
            조건1 : Mapping
            조건2 : 요청변수명과 DTO 필드명이 동일한다.
        * 자료형이 다르고 판단이 어려울때 자동 형변환을 한다.(2번)


    controller ----> view(JS)
        1. 해당 클래스가 @RestController 이면 Return 가능(객체 전송 가능)
            vs 그냥 @Controller이면 메소드 return 값이 템플릿(html)
        2. HttpServletResponse response
            response.getWriter().print() <-어노테이션이 얻는 방식
        3. 어노테이션 사용. @ResponseBody (RestController이랑 같이 쓸 필요x)
            통째로 RestController을 쓰자. <- RequestMapping이 상위버전
            메소드 return 객체

 */
