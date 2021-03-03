package top.utopic.myblog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.utopic.myblog.pojo.Type;

import java.util.List;

public interface TypeService {
    /**
     * 增加
     * @param type
     * @return
     */
    Type saveType(Type type);

    /**
     * 按id查找
     * @param id
     * @return
     */
    Type getType(Long id);

    /**
     * 按名字查找
     * @param name
     * @return
     */
    Type getTypeByName(String name);

    /**
     * 分页查询 Pageable得到一个带分页信息的sql语句。
     * @param pageable
     * @return
     */
    Page<Type> listType(Pageable pageable);

    /**
     * 更新类型数据
     * @param id
     * @param type
     * @return
     */
    Type updateType(Long id, Type type);

    /**
     * 删除类型数据
     * @param id
     */
    void deleteType(Long id);


    /**
     * 查询所有类型信息不用分页
     * @return
     */
    List<Type> listType();

    /**
     * 查询前多少个类型参数
     * @param size
     * @return
     */
    List<Type> listTypeTop(Integer size);
}
