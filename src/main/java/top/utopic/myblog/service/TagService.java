package top.utopic.myblog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.utopic.myblog.pojo.Tag;
import top.utopic.myblog.pojo.Type;

import java.util.List;

public interface TagService {
    /**
     * 增加
     * @param tag
     * @return
     */
    Tag saveTag(Tag tag);

    /**
     * 按id查找
     * @param id
     * @return
     */
    Tag getTag(Long id);

    /**
     * 按名字查找
     * @param name
     * @return
     */
    Tag getTagByName(String name);

    /**
     * 分页查询 Pageable得到一个带分页信息的sql语句。
     * @param pageable
     * @return
     */
    Page<Tag> listTag(Pageable pageable);

    /**
     * 更新类型数据
     * @param id
     * @param tag
     * @return
     */
    Tag updateTag(Long id, Tag tag);

    /**
     * 删除类型数据
     * @param id
     */
    void deleteTag(Long id);

    /**
     * 无条件查询所有标签信息
     * @return
     */

    List<Tag> listTag();

    /**
     * 查询带有指定标签的数据
     * @param ids
     * @return
     */
    List<Tag> listTag(String ids);

    /**
     * 查询前多少个标签
     * @param size
     * @return
     */
    List<Tag> listTagTop(Integer size);
}
