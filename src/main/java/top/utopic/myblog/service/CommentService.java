package top.utopic.myblog.service;

import top.utopic.myblog.pojo.Comment;

import java.util.List;

public interface CommentService {
    /**
     * 更具博客id查询该博客下的评论信息
     * @param blogId
     * @return
     */
    List<Comment> listCommentByBlogId(Long blogId);

    /**
     * 保存评论信息
     * @param comment
     * @return
     */
    Comment saveComment(Comment comment);
}
