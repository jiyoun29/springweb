package ezenweb.Service;

import ezenweb.Dto.RoomDto;
import ezenweb.domain.RoomEntity;
import ezenweb.domain.RoomRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    //room 저장
    public boolean room_save(RoomDto roomDto){

        //dto -> entity 변환
        RoomEntity roomEntity = RoomEntity.builder()
                .roomname(roomDto.getRname())
                .x(roomDto.getX())
                .y(roomDto.getY())
                .rsell(roomDto.getRsell())
                .rprice(roomDto.getRprice())
                .rarea(roomDto.getRarea())
                .rcost(roomDto.getRcost())
                .rplace(roomDto.getRplace())
                .rcontent(roomDto.getRcontent())
                .rcar(roomDto.getRcar())
                .rmove(roomDto.getRmove())
                .rfloors(roomDto.getRfloors())
                .rbuilding(roomDto.getRbuilding())
                .raddress(roomDto.getRaddress())
                .rcontent(roomDto.getRcontent())
                .build();

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

                    //4. 엔티티에 파일명 저장
                    roomEntity.setRimg(uuidfile);

                } catch (Exception e) {System.out.print("파일저장실패"+e);}
            }
        }
        
        //저장이 제일 중요**
        roomRepository.save(roomEntity);
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
            
            
        //현재 보고 있는 지도 범위
        double qa = Double.parseDouble(Location.get("qa"));
                                                //put은 저장 get은 가져오기
        double ha = Double.parseDouble(Location.get("ha"));
        double oa = Double.parseDouble(Location.get("oa"));
        double pa = Double.parseDouble(Location.get("pa"));


        //1. 모든 엔티티 꺼내오기
        List<RoomEntity> roomEntityList = roomRepository.findAll();

        //2. 엔티티 -> Map
        for(RoomEntity entity : roomEntityList) { //리스트에서 엔티티 하나씩 꺼내오기

            //location 범위 내 좌표만 저장하기
            if (Double.parseDouble(entity.getY()) > qa
                    && Double.parseDouble(entity.getY()) < pa
                    && Double.parseDouble(entity.getX()) > ha
                    && Double.parseDouble(entity.getX()) < oa
            ) {

            //3. Map 객체 생성
            Map<String, String> map = new HashMap<>();
            map.put("rname", entity.getRoomname());
            map.put("lng", entity.getX());
            map.put("lat", entity.getY());
            map.put("rno", entity.getRno()+""); //문자열 반환을 위한 ""
            map.put("rimg", entity.getRimg());

            //4. 리스트 넣기
            Maplist.add(map);
            }
        }

        //반환타입 맞춰주기
        Map<String, List<Map<String, String>>> object = new HashMap<>();
        object.put("positions" , Maplist );
                //positions가 카카오 mpa의 key값
        return object;
    }




}
