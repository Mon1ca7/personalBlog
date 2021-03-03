package top.utopic.myblog.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.utopic.myblog.pojo.Tag;
import top.utopic.myblog.pojo.Type;
import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {
    //按标签名查找标签
    Tag findByName(String name);

    /**
     * 分页查询标签
     * @param pageable
     * @return
     */
    @Query("select t from Tag t")
    List<Tag> findTop(Pageable pageable);
}
