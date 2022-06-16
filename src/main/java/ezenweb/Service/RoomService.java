package ezenweb.Service;

import ezenweb.Dto.LoginDto;
import ezenweb.Dto.RoomDto;
import ezenweb.domain.member.MemberEntity;
import ezenweb.domain.member.MemberRepository;
import ezenweb.domain.room.RoomEntity;
import ezenweb.domain.room.RoomRepository;
import ezenweb.domain.room.RoomimgEntity;
import ezenweb.domain.room.RoomimgRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.util.*;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;
    //리포지토리 선언
    @Autowired
    private RoomimgRepository roomimgRepository;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private MemberRepository memberRepository;




    //room 저장
    @Transactional
    public boolean room_save(RoomDto roomDto){
        //현재 로그인 된 세션 호출
        LoginDto loginDto = (LoginDto) request.getSession().getAttribute("login");
        // 현재 로그인 회원의 엔티티 찾기
        MemberEntity memberEntity = memberRepository.findById(loginDto.getMno()).get();


        //dto -> entity 변환
//        RoomEntity roomEntity = RoomEntity.builder()
//                .rtitle(roomDto.getRtitle())
//                .rlat(roomDto.getRlat())
//                .rlon(roomDto.getRlon())
//                .build();
        //여러번 사용하기 위해 가져옴
        RoomEntity roomEntity = roomDto.toentity(); //1. 객체 생성
                                            //자동으로 dto-> entity 변환 라이브러리 (안씀)
                                            //ModelMapper
                                    //        ModelMapper modelMapper = new ModelMapper();
                                    //        modelMapper.map(roomDto , RoomEntity.class);


        //저장이 제일 중요**
        //2. 저장. 우선적으로 룸 db에 저장한다. [pk저장] 해당 객체 매핑 []
        roomRepository.save(roomEntity);

            //현재 로그인된 엔티티를 찾아서 룸에 저장
            roomEntity.setMemberEntity(memberEntity);
            //현재 로그인된 회원 엔티티내 룸 리스트에 룸 엔티티 추가
            memberEntity.getRoomEntityList().add(roomEntity);


        //3. 입력받은 첨부파일을 저장한다.
                //선언
        String uuidfile = null;
        //첨부파일
        if(roomDto.getRimg().size() != 0){ //첨부파일이 존재하면 (0이 아니면, 1개 이상이면)

            //1. 반복문을 이용한 모든 첨부파일 호출
                // 저장 경로
            for(MultipartFile file : roomDto.getRimg()){

                //파일명이 동일하면 식별 문제 발생
                    // UUID 난수 생성
                UUID uuid = UUID.randomUUID();
                    // uuid + 파일명
                uuidfile = uuid.toString()+"_"+ file.getOriginalFilename().replaceAll("_","-");
                        //값을 빼와서 합침
                    //uuid와 파일명 구분_사용[만약에 파일명에_ 존재하면 문제발생 -> 파일명의 _없애기 -> 변경 ]


                //2. 경로 설정 (\:제어문자)
                String dir = "C:\\Users\\504\\IdeaProjects\\springweb\\src\\main\\resources\\static\\upload\\";
                String filePath = dir+uuidfile;
                                // dir+file.getOriginalFilename();
                               //실제 첨부파일 이름 : getOriginalFilename()

                //3. 첨부파일 업로드 처리
                try{
                    file.transferTo( new File(filePath));
                    //파일명.transferTo(새로운 경로->파일);


                    //관계 저장
                    //방이 저장된 상태에서
                    //1. 이미지 엔티티 객체 생성
                    RoomimgEntity roomimgEntity = RoomimgEntity.builder()
                            .rimg(uuidfile)
                            .roomEntity(roomEntity)
                            .build();

                    //2.엔티티 세이브
                    roomimgRepository.save(roomimgEntity);

                    //2. 이미지 엔티티를 룸 엔티티에 추가 (양방향)
                    roomEntity.getRoomimgEntitylist().add(roomimgEntity);

                    System.out.println(roomEntity.getRoomimgEntitylist().get(0).getRimg());



                    //4. 엔티티에 파일명 저장
//                    roomEntity.setRimg(uuidfile);
// 첨부파일.transferTo( 새로운 경로->파일 ) ;
                } catch (Exception e) {System.out.print("파일저장실패"+e);}
            }
        }
        return true;
    }

    //2. room 호출
        //반환타입 {키 : [{}, {}, {}]}
        //json vs 컬렉션 프레임 워크
        //JSONObject == MAP
        //JSONArray ==
            // {키 : 값} = 하나의 entry -> 여러개 가지고 있으면 map collection
            //[ {요소1}, {요소2} ,  {요소3} ] --> List collection
            //List < Map < String, String > >
            //{"positions' : []}
            //Map<String, array>



//    public JSONObject room_list(){
//            //2번에서 json 반환
//        JSONArray jsonArray = new JSONArray();
//
//
//        //1. 모든 엔티티 호출
//        List<RoomEntity> roomEntityList = roomRepository.findAll();
//
//        //2. 모든 엔티티->JSON 변환
//        for(RoomEntity roomEntity : roomEntityList){
//
//            JSONObject object = new JSONObject();
//
//            object.put("rname", roomEntity.getRoomname());
//            object.put("lng", roomEntity.getX());
//            object.put("lat", roomEntity.getY());
//
//            jsonArray.put(object);
//
//        }
//
//        JSONObject object = new JSONObject();
//        object.put("positions",jsonArray);
//
//        //3.반환
//        return object;
//    }

    //2.
    public  //접근제한자
        Map<String,List<Map<String, String>>> //반환타입
                room_list( //메소드명
                Map<String, String> Location){ //인수

            //4-1.리스트 선언
            List<  Map<String , String >  > Maplist = new ArrayList<>();
            
            
        //현재 보고 있는 지도 범위 //put은 저장 get은 가져오기
        double qa = Double.parseDouble(Location.get("qa"));
        double pa = Double.parseDouble(Location.get("pa"));
        double ha = Double.parseDouble(Location.get("ha"));
        double oa = Double.parseDouble(Location.get("oa"));


        //1. 모든 엔티티 꺼내오기
        List<RoomEntity> roomEntityList = roomRepository.findAll();

        //2. 엔티티 -> Map
        for(RoomEntity entity : roomEntityList) { //리스트에서 엔티티 하나씩 꺼내오기

            //location 범위 내 좌표만 저장하기
            if (Double.parseDouble(entity.getRlon()) > qa
                    && Double.parseDouble(entity.getRlon()) < pa
                    && Double.parseDouble(entity.getRlat()) > ha
                    && Double.parseDouble(entity.getRlat()) < oa
            ) {

            //3. Map 객체 생성
            Map<String, String> map = new HashMap<>();
            map.put("rno", entity.getRno()+""); //문자열 반환을 위한 ""
            map.put("rtitle", entity.getRtitle());
            map.put("rlat", entity.getRlat());
            map.put("rlon", entity.getRlon());
//            map.put("rimg", entity.getRimg());
            map.put("rimg" , entity.getRoomimgEntitylist().get(0).getRimg());

            //4. 리스트 넣기
            Maplist.add(map);
            }
        }

        //반환타입 맞춰주기
        Map<String, List<Map<String, String>>> object = new HashMap<>();
        object.put("positions" , Maplist );
                //positions가 카카오 mpa의 key값
        System.out.println(object.toString());
        return object;
    }

    public JSONObject getroom(int rno){

        //1.해당 룸 번호의 룸 엔티티 찾기 (하나만 빼오기)
        Optional<RoomEntity> optionalroomEntity = roomRepository.findById(rno);
        RoomEntity roomEntity = optionalroomEntity.get();

        //2.해당 엔티티 -> json 객체 변환
        JSONObject object = new JSONObject();

            //1. json에 엔티티 필드값 넣기
        object.put("rtitle" , roomEntity.getRtitle());

        JSONArray jsonArray = new JSONArray();
            //2. 룸엔티티에 저장된 룸 이미지를 반복문을 이용한 룸이미지를 jsonarray에 저장
        //룸 별로 이미지가 여러개라 반복문을 통해 뽑음
        for(RoomimgEntity roomimgEntity : roomEntity.getRoomimgEntitylist()){
            jsonArray.put(roomimgEntity.getRimg()); //하나씩 리스트에 담기
        }
        
        //3.jsonarray를 json객체에 포함
        object.put("rimglist", jsonArray);
//        System.out.println(object);//확인
        return object;
    }

    /////////////////////////////////////////////////////////////

    //현재 로그인된 회원이 등록한 방 목록 호출
    public JSONArray myroomlist(){
        JSONArray jsonArray = new JSONArray();

        //현재 로그인된 회원 엔티티 찾기
        LoginDto loginDto = (LoginDto) request.getSession().getAttribute("login");
        MemberEntity memberEntity = memberRepository.findById(loginDto.getMno()).get();
        //찾은 회원 엔티티의 방 목록 json형으로 반환
        for(RoomEntity entity : memberEntity.getRoomEntityList()){
            JSONObject object = new JSONObject();//빼오기
            object.put("rno",entity.getRno());
            object.put("rtitle",entity.getRtitle());
            object.put("rimg",entity.getRoomimgEntitylist().get(0).getRimg());
            object.put("rdate",entity.getModifiedate()); //baseTime에 Getter추가해서 빼내옴
            jsonArray.put(object);
        }
        return jsonArray;
    }


    //룸 삭제 메소드
    @Transactional
    public boolean delete(int rno){
        RoomEntity roomEntity = roomRepository.findById(rno).get();
        if(roomEntity != null){//만일 entity가 없으면
            //해당 엔티티를 삭제
            roomRepository.delete(roomEntity);
            return true;
        } else {
            return false;
        }
    }






}
