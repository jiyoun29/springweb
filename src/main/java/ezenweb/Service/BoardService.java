package ezenweb.Service;

import ezenweb.Dto.BoardDto;
import ezenweb.Dto.LoginDto;
import ezenweb.Dto.MemberDto;
import ezenweb.domain.Board.BoardEntity;
import ezenweb.domain.Board.BoardRepository;
import ezenweb.domain.Board.CategoryEntity;
import ezenweb.domain.Board.CategoryRepository;
import ezenweb.domain.member.MemberEntity;
import ezenweb.domain.member.MemberRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
public class BoardService {


    // jsp : DAO 호출   //  JPA : Repository 호출
    //dao=Repository 호출
    @Autowired //자동 빈 생성[자동 생성자 이용한 객체에 메모리 할당]
    private BoardRepository boardRepository; //메모리 할당

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    //1. C [인수 : dto] why? 게시물 dto를 받아야함
    @Transactional
    public boolean save(BoardDto boardDto){
        //1. 세션 호출 (로그인 형변환)
        LoginDto loginDto= (LoginDto) request.getSession().getAttribute("login");

        if(loginDto != null){ //로그인이 되어 있으면
            //2.로그인 된 회원의 엔티티 찾기
            Optional<MemberEntity> optionalMember = memberRepository.findById(loginDto.getMno());
                //findById(pk키) -> 반환타입 : Optional클래스 [null포인터를 가지고 null값을 가진다(저장).]
                if(optionalMember.isPresent()){
                    //null 검사 : optional 클래스 내 메소드 : isPresent() : null이 아니면
                    //3. dto -> entity
                        //만일 기존에 있는 카테고리 이면
                        boolean sw = false;
                        int cno = 0;
                        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
                        for(CategoryEntity entity : categoryEntityList){
                            if(entity.getCname().equals(boardDto.getCategory())){
                                sw = true; //중복이 있을 경우 true
                                cno = entity.getCno();
                            }
                        }
                        CategoryEntity categoryEntity = null; //메모리 할당 없이 선언
                        if (!sw){ //true가 아니면 카테고리 생성
                            //1. 카테고리 먼저 생성
                            categoryEntity = CategoryEntity.builder()
                                .cname(boardDto.getCategory())
                                .build();
                            categoryRepository.save(categoryEntity);
                        } else {
                            categoryEntity = categoryRepository.findById(cno).get();
                        }

                    BoardEntity boardEntity = boardRepository.save(boardDto.toentity());
                    //save -> 엔티티 -> 반환타입 : 저장된 엔티티
                    //4. 작성자 엔티티 추가
                    boardEntity.setMemberEntity(optionalMember.get());
                    boardEntity.setCategoryEntity( categoryEntity );

                        //카테고리 엔티티에 게시물 연결
                        categoryEntity.getBoardEntityList().add(boardEntity);
                        //회원 엔티티에 게시물 연결
                        optionalMember.get().getBoardEntityList().add(boardEntity);

                    //5.반환
                    return true;
                }
        } else { //로그인이 안 되어 있는 경우
        return  false; }
    return false;
    } //save end

    //2. dto -> entity
    //       int bno = boardRepository.save(boardDto.toentity()).getBno(); //save된 bno 반환
//        if(bno >= 1){return true;} else {return false;}






    //2. 뭘 주고 받을지 생각 -> 인수 : x. 반환 : json or map 선택] (js쓸거면 json 반환추천)
    public JSONArray getboardlist( int cno){ //모두 호출
        JSONArray jsonArray = new JSONArray();

        //불러오기
        List<BoardEntity> boardEntities = boardRepository.findAll();
        //리스트에 있는 모든 entity를 json으로 변환시킴
        for(BoardEntity entity : boardEntities){
            if(entity.getCategoryEntity().getCno() == cno) {
                JSONObject object = new JSONObject();
                object.put("bno", entity.getBno());
                object.put("btitle", entity.getBtitle());
                object.put("bindate", entity.getCreatedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
                object.put("bview", entity.getBview());
                object.put("blike", entity.getBlike());
                object.put("mid", entity.getMemberEntity().getMid());
                //담은 후
                jsonArray.put(object); //반환시킴
            }
        }
        return jsonArray;
    } //getboardlist end


    //2. R :개별조회[게시물 번호]
    @Transactional //수정처리 하고 있으므로 추가
    public JSONObject getboard(int bno){

        //조회수 증가 처리[기준:ip /24시간]
        String ip = request.getRemoteAddr(); //사용자의 ip가져오기

        Optional<BoardEntity> optional = boardRepository.findById(bno);
        BoardEntity entity = optional.get(); //빼내옴

            //세션 호출
            Object com = request.getSession().getAttribute(ip+bno);
                if(com == null){ //만일 세션이 있으면
                    //ip와 bno 합쳐서 세션(서버 내 저장소) 부여
                    request.getSession().setAttribute(ip+bno , 1);
                    request.getSession().setMaxInactiveInterval(60*60*24); //세션 허용시간[초단위]
                    //조회수 증가 처리
                    entity.setBview(entity.getBview()+1);
                }

        //json으로 리턴
        JSONObject object = new JSONObject();
        object.put("bno", entity.getBno());
        object.put("btitle", entity.getBtitle());
        object.put("bcontent", entity.getBcontent());
        object.put("bview", entity.getBview());
        object.put("blike", entity.getBlike());
        object.put("bindate", entity.getCreatedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")) );
        object.put("bmodate", entity.getModifiedate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")) );
        object.put("mid", entity.getMemberEntity().getMid());
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



    //5. 카테고리 호출 메소드
    public JSONArray getcategorylist(){
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
        JSONArray jsonArray = new JSONArray();
        for(CategoryEntity entity : categoryEntityList){

            JSONObject object = new JSONObject();
            object.put("cno", entity.getCno());
            object.put("cname",entity.getCname());
            jsonArray.put(object);
        }
        return jsonArray;
    }






}
