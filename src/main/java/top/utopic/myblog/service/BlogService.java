package top.utopic.myblog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.utopic.myblog.pojo.Blog;
import top.utopic.myblog.pojo.Tag;
import top.utopic.myblog.vo.BlogQuery;

import java.util.List;
import java.util.Map;

public interface BlogService {
    /**
     * 根据id查询博客信息
     * @param id
     * @return
     */
    Blog getBlog(Long id);
    /**
     * 分页查询博客信息
     * @param pageable
     * @param blogQuery
     * @return
     */
    Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery);

    /**
     * 增加博客信息
     * @param blog
     * @return
     */
    Blog saveBlog(Blog blog);

    /**
     * 根据id更新博客信息
     * @param id
     * @param blog
     * @return
     */
    Blog updateBlog(Long id,Blog blog);

    /**
     * 删除博客信息
     * @param id
     */
    void deleteBlog(Long id);

    /**
     * 查询前多少篇博客
     * @param size
     * @return
     */
    List<Blog> listRecommendBlogTop(Integer size);

    /**
     * 分页查询博客信息
     * @param pageable
     * @return
     */
    Page<Blog> listBlog(Pageable pageable);


    /**
     * 根据输入的信息查询博客
     * @param query
     * @param pageable
     * @return
     */
    Page<Blog> listBlog(String query, Pageable pageable);

    /**
     * 获得博客详情内容
     * @param id
     * @return
     */
    Blog getAndConvert(Long id);

    /**
     * 根据标签id分页查询博客
     * @param tagId
     * @param pageable
     * @return
     */
    Page<Blog> listBlog(Long tagId, Pageable pageable);

    /**
     * 获取博客的归档信息 key为年份，value为该年份对应的所有博客
     * @return
     */
    Map<String,List<Blog>> archiveBlog();

    /**
     * 统计所有博客的条数
     * @return
     */
    Long countBlog();
}
