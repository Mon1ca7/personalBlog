package top.utopic.myblog.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.utopic.myblog.dao.TypeRepository;
import top.utopic.myblog.exception.NotFoundException;
import top.utopic.myblog.pojo.Type;
import top.utopic.myblog.service.TypeService;

import java.util.List;
import java.util.Optional;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeRepository typeRepository;

    /**
     * 调用JpaRepository的save方法新增类型数据
     * @param type
     * @return
     */
    @Transactional //开启事务
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    /**
     * 调用方法根据id查数据
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.findById(id).get();
    }

    /**
     * 根据类型名查询数据
     * @param name
     * @return
     */
    @Transactional
    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    /**
     *分页查询
     * @param pageable
     * @return
     */
    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    /**
     * 更新数据，首先查找数据存不存在
     * @param id
     * @param type
     * @return
     */
    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.findById(id).get();
        if (t==null){
            throw new NotFoundException("不存在该类型");
        }
        //将新的对象转化更新到数据库中
        BeanUtils.copyProperties(type,t);
        return typeRepository.save(t);
    }

    /**
     * 删除
     * @param id
     */
    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

    /**
     * 不分页查询所有类型信息
     * @return
     */
    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    /**
     * 排序分页查询前多少条数据
     * @param size
     * @return
     */
    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return typeRepository.findTop(pageable);
    }
}
