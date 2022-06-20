package ezenweb.Service;

import ezenweb.Dto.BoardDto;
import ezenweb.domain.Board.BoardEntity;
import ezenweb.domain.Board.BoardRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {


    // jsp : DAO 호출   //  JPA : Repository 호출
    //dao=Repository 호출
    @Autowired //자동 빈 생성[자동 생성자 이용한 객체에 메모리 할당]
    private BoardRepository boardRepository; //메모리 할당

    //1. C [인수 : dto] why? 게시물 dto를 받아야함
    @Transactional
    public boolean save(BoardDto boardDto){
        //1. 특정 로직 []


        //2. dto -> entity
        int bno = boardRepository.save(boardDto.toentity()).getBno();
            //save된 bno 반환

        if(bno >= 1){
            return true;
        } else {
            return false;
        }
    } //save end

    //2. R [뭘 주고 받을지 생각 -> 인수 : x. 반환 : json or map 선택] (js쓸거면 json 반환추천)
//    @Transactional
    public JSONArray getboardlist(){ //모두 호출
        JSONArray jsonArray = new JSONArray();

        //불러오기
        List<BoardEntity> boardEntities = boardRepository.findAll();
        //리스트에 있는 모든 entity를 json으로 변환시킴
        for(BoardEntity entity : boardEntities){
            JSONObject object = new JSONObject();
            object.put("bno", entity.getBno());
            object.put("btitle", entity.getBtitle());
            object.put("bdate", entity.getCreatedate());
            object.put("bview", entity.getBview());
            object.put("blike", entity.getBlike());
            //담은 후
            jsonArray.put(object); //반환시킴
        }

        return jsonArray;
    } //getboardlist end
    
    
    //2. R: 게시물 개별조회
    public JSONObject getboard(int bno){
        Optional<BoardEntity> optional = boardRepository.findById(bno);
        BoardEntity entity = optional.get(); //빼내옴
        //json으로 리턴
        JSONObject object = new JSONObject();
        object.put("bno", entity.getBno());
        object.put("btitle", entity.getBtitle());
        object.put("bcontent", entity.getBcontent());
        object.put("bview", entity.getBview());
        object.put("blike", entity.getBlike());
        object.put("bindate", entity.getCreatedate());
        object.put("bmodate", entity.getModifiedate());
        return object;
    }
    
    
    

    //3. U [인수 : 게시물 번호, 수정할 내용들 -> dto]
    @Transactional
    public boolean update(BoardDto boardDto){
        //수정하기
        Optional<BoardEntity> optionalBoard = boardRepository.findById(boardDto.getBno());
        BoardEntity boardEntity = optionalBoard.get();
            //key값을 이용한 entity 찾기
            // boardRepository.findById(boardDto.getBno()).get(); <바로 다 빼옴
        boardEntity.setBtitle(boardDto.getBtitle());
        boardEntity.setBcontent(boardDto.getBcontent());

        return true;
    }

    //4. D
    @Transactional
    public boolean delete(int bno){
        BoardEntity boardEntity = boardRepository.findById(bno).get();
        boardRepository.delete(boardEntity);

        return true;
    }



}
