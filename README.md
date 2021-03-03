# 个人博客

**很早之前就有想要做一个自己的博客项目，在我学完了一个Spring Boot课程后，就想拿此项目来练练手，熟悉熟悉刚学的知识，下面对该项目进行一个总结。**

## 1.实现的功能

该博客实现了如下的功能：

![image-20210211152108870](C:\Users\86137\AppData\Roaming\Typora\typora-user-images\image-20210211152108870.png)

## 2.用到的技术

**前端：**

semantic UI框架（第一次使用，比较简约，我很喜欢），排版插件：typo，动画插件：animate（没怎么使用到），代码高亮插件：prism，markdown编辑器插件：editormd，目录插件：tocbot，生成二维码插件：qrcode，页面滚动插件：waypoints......

**后端：**

Spring Boot + JPA + thymeleaf模板引擎,刚开始时我是想用mybatis的，但后来想了想，JPA对我来说是一个新的概念，所以抱着学习的态度我就用了JPA。

**数据库：**

MySQL8.0

## 3.一些个人想法

##### 一：关于JPA与mybatis的比较：

​		在做项目的过程中我总结了一下：

​       Spring Data JPA的优点：完全面向对象，只需要考虑Java的实体对象的设计，在开发的过程中，我只需要写好实体类，再加上一些注解就可以自动帮我把实体对象映射到数据库中，并且建立起相应的数据表。除此之外，该框架提供的crud方法，帮我省去了大量sql语句的编写，我只需要直接调用方法就行了。

​	Spring Data JPA的缺点：在面对一些复杂的数据操作时，他就显得不是那么的灵活了，虽然他内部提供了一些方法来应对复杂的数据操作，但是这些方法的可读性并不强，甚至说是有些繁琐。而且有些方法随着版本的更新，就没法使用了，在做项目的过程中，我就发现一些方法使用不了，在找了一些资料才找到了替代方法。

​     Mybatis：在用了Spring Data JPA的时候，我发现Mybatis操作数据库的缺点就暴露出来了，sql语句编写量太大，面对复杂的多表查询的时候开发人员的sql语句功底有一定的要求。但是由于涉及到数据库操作的时候都需要编写SQL语句这也使得mybatis变得很灵活，而且还支持动态sql编程。

##### 二：功能开发方面的总结：

在做这个博客开发的时候，我认为最复杂的功能就是留言功能的开发，直到现在我都有些地方没弄懂。起初我以为只是提交留言就好了，但做起来，我才发现没那么简单，父子级留言之间的迭代关系确实把我绕晕了。

##### 三:存在的一些问题：

在做完这个博客项目时候，测试的时候我也发现了一些问题，但目前我还没有改，因为是计划在年前部署的。

###### 1.后台标签管理的问题：

   写好博客该发布的时候，要选择博客的标签，这个地方有一个可以输入可以搜索的下拉选择框，而下拉框中的内容都是从标签数据库中读取的内容。也就是说，标签只能选择现有的标签，这但事实上这个地方的逻辑应该是，如果数据库中没有该标签，使用者应该可以自定义标签，并且将其插入到数据库中。当然由于这是个人博客，目前只提供给个人使用，这一点也无关紧要了。

###### 2.关于保存和发布的问题：

在博客的实体对象中，有一个属性叫published，如果为1就是发布博客，为0就是保存为草稿，但最终无论是发布还是保存为草稿的博客都会插入到数据库中。因此这个功能还存在一个小小的bug，目前我也不打算修改。因为用的是JPA提供和数据库交互的方法，所以代码不是很灵活，因此修改的时候会存在很多的问题。我也考虑过用条件查询，也就是只查询发布的博客信息，但由于这个查询是分页查询，所以在使用组合条件查询的时候会报错，这也是我觉得jpa这种方式存在的缺陷。

##### 四:需要完善的地方及待开发的功能：

1.完善保存和发布的功能，我觉得这个功能用Redis会比较好一点，当然这只是我的个人看法。或者自己写分页+条件的组合查询。

2.完善后台发布博客时选择标签的问题，有就是可以在发布博客的时候自己定义标签，而不能总是选择已有的标签，并且将自定义的标签也插入到数据库中。

3.可以在后台再加上一个接收消息的页面，管理员可以直接在后台对游客留言的进行回复。

4.在后台登录的时候再加上一个安全校验。

5.前后端还是要分离，不分离的话代码的耦合度太高了。

6.就这样了，以后的再加吧。

## 4.我认为的难点

在我做个项目的过程中，我认为对于我来说最麻烦的地方在于评论的展示功能，这是一个两层式的评论展示方式。下面是实现的具体代码，当然这个代码并不是我自己写的。

```java
  /**
     * 根据博客评论时间查询评论信息
     * @param blogId
     * @return
     */
    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by("createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
        return eachComment(comments);
    }

    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments){
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();
            for(Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);//顶节点添加到临时存放集合
        if (comment.getReplyComments().size()>0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size()>0) {
                    recursively(reply);
                }
            }
        }
    }
```

## 5.写在最后

我认为这个小型的个人博客项目写完之后确实学到了不少东西，挺适合刚学完springboot拿来练练手的。当然这个项目可以在B站搜：springboot个人博客项目找到它。链接：https://www.bilibili.com/video/BV1PE411f7gS?from=search&seid=1135058093366002125

这是我的博客地址：www.utopic.top

具体的更多细节代码中有大量注解。