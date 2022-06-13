package ezenweb.controller;

import ezenweb.Dto.RoomDto;
import ezenweb.Service.RoomService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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

    @PutMapping("/write") //2.등록 처리
    @ResponseBody //객체반환 / 템플릿이 아닌 객체 반환시 사용되는 어노테이션
    public boolean write_save(RoomDto roomDto){
        //요청변수 중 dto 필드와 변수 명이 동일할 경우 자동 주입

//        public boolean write_save(@RequestParam("roomname") String roomname ,
//                @RequestParam("x") String x ,
//                @RequestParam("y") String y){ //RequestParam으로 변수 요청
//
//            //dto 생성
//            RoomDto roomDto = RoomDto.builder()
//                    .roomname(roomname)
//                    .x(x).y(y)
//                    .build();
        
        //service에 dto 전달
        roomService.room_save(roomDto);

//        System.out.println("입력값"+roomDto.toString());

        return true;
    }

    //3.방 목록 페이지 이동
    @GetMapping("/list")
    public String list() {

        return "room/list";
    }

    @GetMapping("/roomlist")
    public void roomlist(HttpServletResponse response) {
        JSONObject object = new JSONObject();
//        JSONArray jsonArray = roomService.room_list();

  //      object.put("positions" , jsonArray);

        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(object);
        } catch (Exception e){}


    }



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
        3. mapping 사용 시 DTO로 자동 주입 된다. <여러개 쓸 때에는 해당 mapping 사용>
            조건1 : Mapping
            조건2 : 요청변수명과 DTO 필드명이 동일한다.
            


 */

