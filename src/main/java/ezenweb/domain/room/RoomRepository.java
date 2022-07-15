package ezenweb.domain.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {

    //1. 거래방식
    @Query(value = "select * from room where trans = :trans and rprice > :minprice and rprice < :maxprice " , nativeQuery = true)
    List<RoomEntity> search(int trans, int minprice, int maxprice);
    
    //2. 가격 검색
    @Query(value = "select * from room where rprice > :minprice and rprice < :maxprice " , nativeQuery = true)
    List<RoomEntity> search(int minprice, int maxprice);
    
    //오버라이딩[상속받은 메소드를 재정의] vs 오버로딩[동일한 메소드명의 인수.타입이 다를 때] - 다형성
    
}
