package top.utopic.myblog.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.utopic.myblog.pojo.Blog;
import top.utopic.myblog.pojo.User;
import top.utopic.myblog.service.BlogService;
import top.utopic.myblog.service.TagService;
import top.utopic.myblog.service.TypeService;
import top.utopic.myblog.vo.BlogQuery;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    //代码片段，用于局部更新使用
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    /**
     * 分页多条件查询博客信息
     * @param pageable
     * @param blogQuery
     * @param model
     * @return
     */
    @GetMapping(value = "/blogs")
    public String blogs(@PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC)
                        Pageable pageable, BlogQuery blogQuery, Model model) {
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        return LIST;
    }

    /**
     * 局部查询，不查询分类信息，用于局部更新
     * @param pageable
     * @param blogQuery
     * @param model
     * @return
     */
    @PostMapping(value = "/blogs/search")
    public String search(@PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC)
                                Pageable pageable, BlogQuery blogQuery, Model model) {
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        return "admin/blogs :: blogList";
    }

    /**
     * 点击进入编写博客页面
     * @param model
     * @return
     */
    @GetMapping(value = "/blogs/input")
    public String input(Model model){
        //初始化标签信息和分类信息
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return INPUT;
    }
    //初始化标签信息和分类信息函数
    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }

    /**
     * 点击编辑进入查看并且编辑该博客
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = "/blogs/{id}/input")
    public String editInput(@PathVariable Long id,Model model){
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }

    /**
     * 添加和修改博客信息，如果博客不存在就新增博客，如果存在就修改博客
     * @param blog
     * @param attributes
     * @param session
     * @return
     */
    @PostMapping(value = "/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId() == null){
            b = blogService.saveBlog(blog);
        }else {
            b = blogService.updateBlog(blog.getId(),blog);
        }

        if (b == null){
            attributes.addFlashAttribute("message","操作失败");
        }else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return REDIRECT_LIST;
    }

    /**
     * 根据id删除博客信息
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","操作成功");
        return REDIRECT_LIST;
    }
}
