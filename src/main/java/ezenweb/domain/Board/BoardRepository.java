package ezenweb.domain.Board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //생략 가능
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {
        //JpaRepository : DAO의 역할
}
