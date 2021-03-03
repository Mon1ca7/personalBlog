package top.utopic.myblog.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import top.utopic.myblog.pojo.Blog;

import java.util.List;

//JpaSpecificationExecutor 完成多条件查询，并且支持分页与排序,实现复杂动态查询。
public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {

    /**
     * 分页查询前多少篇博客
     * @param pageable
     * @return
     */
    @Query("select b from Blog  b where b.recommend = true")
//  @Query(value = "select * from t_blog where b.recommend = true",nativeQuery = true)
    List<Blog> findTop(Pageable pageable);

    //select * form t_blog b where b.title like '%?%' or b.content like '%?%';
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    /**
     * 更新浏览次数
     * @param id
     * @return
     */
    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id = ?1")
    int updateViews(Long id);

    /**
     * 格式化查询年份信息并对其进行排序分组
     * @return
     */
   @Query(value = "select date_format(b.update_time,'%Y') as year from t_blog b GROUP BY year order by year DESC ",nativeQuery = true)
    List<String> findGroupYear();

    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1")
    List<Blog> findByYear(String year);
}
