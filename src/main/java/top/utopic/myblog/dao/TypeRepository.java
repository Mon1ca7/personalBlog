package top.utopic.myblog.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.utopic.myblog.pojo.Type;
import java.util.List;

public interface TypeRepository extends JpaRepository<Type,Long> {
    //按类型名查找类型
    Type findByName(String name);

    /**
     * 分页查询类型
     * @param pageable
     * @return
     */
    @Query("select t from Type t")
    List<Type> findTop(Pageable pageable);
}
