package top.utopic.myblog.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.utopic.myblog.pojo.Tag;
import top.utopic.myblog.pojo.Type;
import top.utopic.myblog.service.TagService;
import top.utopic.myblog.service.TypeService;

import javax.validation.Valid;

@Controller
@RequestMapping(value ="/admin" )
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 分页查询，每页五条数据，按id逆序排序
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping(value = "/tags")
    public String types(@PageableDefault(size = 5,sort = {"id"},direction = Sort.Direction.DESC)
                                Pageable pageable, Model model) {
        model.addAttribute("page",tagService.listTag(pageable));
        return "admin/tags";
    }

    /**
     * 跳转到新增页面
     * @param model
     * @return
     */
    @GetMapping("/tags/input")
    public String put(Model model){
        //定义一个空对象并且传递到前端页面
        model.addAttribute("tag",new Tag());
        return "admin/tags-input";
    }

    /**
     * 根据id查询是否存在该分类信息，并将其传递到修改页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id,Model model){
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tags-input";
    }

    /**
     * 新增分类，首先先校验该分类，判断是否存在分类，不存在才能继续添加
     * 结果必须重定向到分类页面，否则无法将新增后的数据传递过去
     * @param tag 校验分类名称
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 !=null){
            result.rejectValue("name","nameError","不能重复添加标签");
        }
        //入错存在错误，返回输入页面
        if (result.hasErrors()){
            return "admin/tags-input";
        }
        //新增分类
        Tag t = tagService.saveTag(tag);
        if (t==null){
            attributes.addFlashAttribute("message","新增失败");
        }else {
            attributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/tags";
    }


    /**
     * 根据id先查找到分类信息，如何进行更新
     * @param tag
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/tags/{id}")
    public String post(@Valid Tag tag, BindingResult result,@PathVariable Long id, RedirectAttributes attributes){
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 !=null){
            result.rejectValue("name","nameError","不能重复添加标签");
        }
        //入错存在错误，返回输入页面
        if (result.hasErrors()){
            return "admin/tags-input";
        }
        //跟新分类信息
       Tag t = tagService.updateTag(id,tag);
        if (t == null){
            attributes.addFlashAttribute("message","更新失败");
        }else {
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping(value = "/tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message","删除成功！");
        return "redirect:/admin/tags";
    }
}
