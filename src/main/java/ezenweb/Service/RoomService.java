package ezenweb.Service;

import ezenweb.Dto.RoomDto;
import ezenweb.domain.RoomEntity;
import ezenweb.domain.RoomRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    //room 저장
    public boolean room_save(RoomDto roomDto){

        //dto -> entity 변환
        RoomEntity roomEntity = RoomEntity.builder()
                .roomname(roomDto.getRoomname())
                .x(roomDto.getX())
                .y(roomDto.getY())
                .build();
        //저장이 제일 중요***
        roomRepository.save(roomEntity);
        return true;
    }

    //2. room 호출
    public JSONObject room_list(){
            //2번에서 json 반환
        JSONArray jsonArray = new JSONArray();


        //1. 모든 엔티티 호출
        List<RoomEntity> roomEntityList = roomRepository.findAll();

        //2. 모든 엔티티->JSON 변환
        for(RoomEntity roomEntity : roomEntityList){

            JSONObject object = new JSONObject();

            object.put("rname", roomEntity.getRoomname());
            object.put("lng", roomEntity.getX());
            object.put("lat", roomEntity.getY());

            jsonArray.put(object);

        }

        JSONObject object = new JSONObject();
        object.put("positions",jsonArray);

        //3.반환
        return object;
    }

}
