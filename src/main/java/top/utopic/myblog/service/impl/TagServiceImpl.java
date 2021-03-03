package top.utopic.myblog.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.utopic.myblog.dao.TagRepository;
import top.utopic.myblog.exception.NotFoundException;
import top.utopic.myblog.pojo.Tag;
import top.utopic.myblog.service.TagService;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;

    @Transactional //开启事务
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional //开启事务
    @Override
    public Tag getTag(Long id) {
        return tagRepository.findById(id).get();
    }

    @Transactional //开启事务
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional //开启事务
    @Override
    public Page<Tag> listTag(Pageable pageable) {

        return tagRepository.findAll(pageable);
    }

    @Transactional //开启事务
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag tag1 = tagRepository.findById(id).get();
        if (tag1 == null) {
            throw new NotFoundException("不存在该标签");
        }
        //将新的对象转化更新到数据库中
        BeanUtils.copyProperties(tag, tag1);
        return tagRepository.save(tag);
    }

    @Transactional //开启事务
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    /**
     * 无条件查询所有标签信息
     * @return
     */
    @Transactional //开启事务
    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    /**
     * 根据给定的id集合查询所有对应的实体，返回实体集合。
     * @param ids
     * @return
     */
    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAllById(convertToList(ids));
    }

    /**
     * 排序分页查询前多少条数据
     * @param size
     * @return
     */
    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort  = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return tagRepository.findTop(pageable);
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i = 0; i < idarray.length; i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }
}
