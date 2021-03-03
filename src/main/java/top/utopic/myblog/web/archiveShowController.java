package top.utopic.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import top.utopic.myblog.service.BlogService;

@Controller
public class archiveShowController {
    @Autowired
    private BlogService blogService;

    /**
     * 归档页面，按年份分组排序将数据传送到前端
     * @param model
     * @return
     */
    @GetMapping("/archives")
    public String archives(Model model){
        model.addAttribute("archiveMap",blogService.archiveBlog());
        model.addAttribute("blogCount",blogService.countBlog());
        return "archive";
    }
}
