package top.utopic.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.utopic.myblog.pojo.Tag;
import top.utopic.myblog.service.BlogService;
import top.utopic.myblog.service.TagService;

import java.util.List;

@Controller
public class tagShowController {
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;

    /**
     * 根据标签id查询博客信息并且分页展示
     * @param pageable
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = "/tags/{id}")
    public String tags(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       @PathVariable Long id, Model model){
        List<Tag> tags = tagService.listTagTop(10000);
        if (id == -1) {
            id = tags.get(0).getId();
        }
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.listBlog(id,pageable));
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
