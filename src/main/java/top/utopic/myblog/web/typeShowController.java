package top.utopic.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.utopic.myblog.pojo.Type;
import top.utopic.myblog.service.BlogService;
import top.utopic.myblog.service.TypeService;
import top.utopic.myblog.vo.BlogQuery;

import java.util.List;

@Controller
public class typeShowController {

    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;

    /**
     * 根据分类查询该分类下的所有博客
     * @param pageable
     * @param id
     * @param model
     * @return
     */
    @GetMapping(value = "/types/{id}")
    public String types(@PageableDefault(size = 8,sort = ("updateTime"),direction = Sort.Direction.DESC)
                        Pageable pageable, @PathVariable Long id, Model model){
        //查询前10000个分类，相当于查询所有
        List<Type> types = typeService.listTypeTop(10000);
        if (id == -1){
            //默认获取第一个分类的id
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types",types);
        //调用查询方法传入分页和查询的条件
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        model.addAttribute("activeTypeId",id);
        return "types";
    }
}
