package top.utopic.myblog.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.utopic.myblog.dao.BlogRepository;
import top.utopic.myblog.exception.NotFoundException;
import top.utopic.myblog.pojo.Blog;
import top.utopic.myblog.pojo.Type;
import top.utopic.myblog.service.BlogService;
import top.utopic.myblog.util.MarkdownUtils;
import top.utopic.myblog.util.myBeanUtils;
import top.utopic.myblog.vo.BlogQuery;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;

    /**
     * 根据id查询博客信息
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).get();
    }

    /**
     * 分页并且根据条件查询博客信息
     * @param pageable
     * @param blogQuery
     * @return
     */
    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //判断title是否为空并且模糊查询
                if (!"".equals(blogQuery.getTitle()) && blogQuery.getTitle() != null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+blogQuery.getTitle()+"%"));
                }
                //判断分类信息的id是否为空，并且根据id查询分类信息
                if (blogQuery.getTypeId()!=null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blogQuery.getTypeId()));
                }
                //判断是否推荐
                if (blogQuery.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blogQuery.isRecommend()));
                }
                //将list转为数组执行查询
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    /**
     * 增加博客信息
     * @param blog
     * @return
     */
    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    /**
     * 更新博客
     * myBeanUtils.getNullPropertyNames(blog) 过滤了更新时属性值为空的属性，只copy属性值不为空的属性
     * blog:数据源  b:目标对象
     * @param id
     * @param blog
     * @return
     */
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findById(id).get();
        if (b==null){
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,b, myBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    /**
     * 删除博客
     * @param id
     */
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    /**
     * 分页查询前多少条数据
     * @param size
     * @return
     */
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        //springboot2.2.1（含）以上的版本Sort已经不能再实例化了，构造方法已经是私有的了
        //所以采用 Sort.by方法 PageRequest同理
        //page，第几页，从0开始，默认为第0页
        //size，每一页的大小，默认为20
        //sort，排序相关的信息，例如sort=firstname&sort=lastname,desc表示在按firstname正序排列基础上按lastname倒序排列
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    /**
     * 分页查询博客信息
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }


    /**
     * 全局搜索，根据输入的信息进行博客的查询
     * @param query
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    /**
     * 将博客内容转换为HTML格式
     * @param id
     * @return
     */
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if (blog == null){
            throw new NotFoundException("该博客不存在");
        }
        //用于接收传递过来的博客，并且复制到该新的对象里面，用这个新对象进行操作，避免操作到数据库中
        //数据库中仍然要保持markdown格式的博客内容
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        //每调用一次这个方法就更新一次博客查看次数
        blogRepository.updateViews(id);
        return b;
    }

    /**
     * 联合查询根据标签查询博客
     * @param tagId
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }


    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    /**
     * 统计所有博客的条数
     * @return
     */
    @Override
    public Long countBlog() {
        return blogRepository.count();
    }
}
