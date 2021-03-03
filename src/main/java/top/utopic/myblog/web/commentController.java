package top.utopic.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import top.utopic.myblog.pojo.Comment;
import top.utopic.myblog.pojo.User;
import top.utopic.myblog.service.BlogService;
import top.utopic.myblog.service.CommentService;

import javax.servlet.http.HttpSession;

@Controller
public class commentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    /**
     * 根据id获取到评论信息
     * @param blogId
     * @param model
     * @return
     */
    @GetMapping(value = "/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    /**
     * 保存用户评论，首先检测是否为管理员用户
     * 如果是管理员用户则取出管理员用户信息
     * 如果不是则设置评论用户信息
     * 然后保存评论
     * @param comment
     * @param session
     * @return
     */
    @PostMapping(value = "/comments")
    public String post(Comment comment, HttpSession session){
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user = (User) session.getAttribute("user");
        if (user != null){
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }else {
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }
}
