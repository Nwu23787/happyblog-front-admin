package com.happyblog.service.impl;

import com.happyblog.entity.constants.Constants;
import com.happyblog.entity.enums.*;
import com.happyblog.entity.po.Blog;
import com.happyblog.entity.po.BlogCategory;
import com.happyblog.entity.query.BlogCategoryQuery;
import com.happyblog.entity.query.BlogQuery;
import com.happyblog.entity.vo.PaginationResult;
import com.happyblog.entity.vo.SimplePage;
import com.happyblog.entity.vo.web.BlogVO;
import com.happyblog.exception.BusinessException;
import com.happyblog.mappers.BlogCategoryMapper;
import com.happyblog.mappers.BlogMapper;
import com.happyblog.service.BlogService;
import com.happyblog.utils.CopyTools;
import com.happyblog.utils.ImageUtils;
import com.happyblog.utils.StringTools;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 业务接口实现
 */
@Service("blogService")
public class BlogServiceImpl implements BlogService {

    @Resource
    private BlogMapper<Blog, BlogQuery> blogMapper;

    @Resource
    private BlogCategoryMapper<BlogCategory, BlogCategoryQuery> blogCategoryMapper;

    @Resource
    private ImageUtils imageUtils;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<Blog> findListByParam(BlogQuery param) {
        List<Blog> blogList = this.blogMapper.selectList(param);
        if (param.getAutoGetSummary() != null && param.getAutoGetSummary()) {
            for (Blog blog : blogList) {
                if (StringTools.isEmpty(blog.getSummary()) && !StringTools.isEmpty(blog.getContent())) {
                    String textContent = StringTools.delHTMLTag(blog.getContent());
                    blog.setSummary(textContent.length() > Constants.LENGTH_300 ? textContent.substring(0, Constants.LENGTH_300) :
                            textContent);
                }
            }
        }
        return blogList;
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(BlogQuery param) {
        return this.blogMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResult<Blog> findListByPage(BlogQuery param) {
        int count = this.blogMapper.selectCount(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();
        int pageNo = 0;
        if (null != param.getPageNo()) {
            pageNo = param.getPageNo();
        }
        SimplePage page = new SimplePage(pageNo, count, pageSize);
        param.setSimplePage(page);
        List<Blog> list = this.findListByParam(param);
        PaginationResult<Blog> result = new PaginationResult<>(count, pageSize, pageNo, page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(Blog bean) throws BusinessException {
        try {
            return this.blogMapper.insert(bean);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("信息已经存在");
        }
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<Blog> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.blogMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<Blog> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.blogMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 根据BlogId修改
     */
    @Override
    public Integer updateByBlogId(Blog bean, String blogId) {
        return this.blogMapper.updateByBlogId(bean, blogId);
    }


    /**
     * 根据BlogId删除
     */
    @Override
    public Integer deleteByBlogId(String blogId) {
        return this.blogMapper.deleteByBlogId(blogId);
    }


    /**
     * 根据BlogId获取对象
     */
    @Override
    public Blog getBlogByBlogId(String blogId) {
        return this.blogMapper.selectByBlogId(blogId);
    }

    @Override
    public void saveBlog(Blog blog) throws BusinessException {
        if (BlogTypeEnum.SPECIAL.getType().equals(blog.getBlogType()) && (blog.getCategoryId() == null || blog.getCategoryId() == 0)) {
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }

        if (StringTools.isEmpty(blog.getSummary()) && !StringTools.isEmpty(blog.getContent())) {
            String textContent = StringTools.delHTMLTag(blog.getContent());
            blog.setSummary(textContent.length() > Constants.LENGTH_300 ? textContent.substring(0, Constants.LENGTH_300) :
                    textContent);
        }

        if (blog.getCategoryId() != 0) {
            BlogCategory category = blogCategoryMapper.selectByCategoryId(blog.getCategoryId());
            if (category == null || !category.getCategoryType().equals(blog.getBlogType())) {
                throw new BusinessException("博客分类/专题不存在");
            }
            blog.setCategoryName(category.getCategoryName());
        } else {
            blog.setCategoryName(Constants.DEFAULT_BLOG_CATEGORY_NAME);
        }

        blog.setCover(imageUtils.resetImage(blog.getCover(), null));
        String content = blog.getContent();
        //替换图片
        if (!StringTools.isEmpty(content)) {
            String month = imageUtils.resetImageHtml(content);
            //避免替换博客中template关键，所以前后带上/
            String replaceMonth = "/" + month + "/";
            content = content.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
            blog.setContent(content);
            String markdownContent = blog.getMarkdownContent();
            if (!StringTools.isEmpty(markdownContent)) {
                markdownContent = markdownContent.replace(Constants.FILE_FOLDER_TEMP, replaceMonth);
                blog.setMarkdownContent(markdownContent);
            }
        }
        //排序
        if (BlogTypeEnum.SPECIAL.getType().equals(blog.getBlogType())) {
            BlogQuery blogQuery = new BlogQuery();
            blogQuery.setpBlogId(blog.getpBlogId());
            blogQuery.setCategoryId(blog.getCategoryId());
            blogQuery.setStatus(BlogStatusEnum.STATUS_1.getStatus());
            blogQuery.setDelType(BlogDelTypeEnum.NORMAL.getType());
            Integer count = blogMapper.selectCount(blogQuery);
            blog.setSort(count + 1);
        }
        if (blog.getBlogId() == null) {
            Date curDate = new Date();
            blog.setCreateTime(curDate);
            blog.setLastUpdateTime(curDate);
            blog.setBlogId(StringTools.getRandomString(Constants.LENGTH_10));
            blogMapper.insert(blog);
        } else {
            blog.setSort(null);
            blogMapper.updateByBlogId(blog, blog.getBlogId());
        }
    }

    @Override
    public List<Blog> getBlogListTree(Integer categoryId, Integer showType) {
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setCategoryId(categoryId);
        blogQuery.setDelType(BlogDelTypeEnum.NORMAL.getType());
        blogQuery.setOrderBy("sort asc");
        List<Blog> blogList = this.blogMapper.selectList(blogQuery);
        blogList = convertLine2Tree(blogList, "0");
        List<Blog> resultList = new ArrayList<>();
        Blog root = new Blog();
        root.setBlogId("0");
        if (BlogTreeShowTypeEnum.ALL.getType().equals(showType)) {
            root.setTitle("全部");
        } else if (BlogTreeShowTypeEnum.WITH_CATEGORY.getType().equals(showType)) {
            BlogCategory blogCategory = this.blogCategoryMapper.selectByCategoryId(categoryId);
            root.setTitle(blogCategory.getCategoryName());
        } else if (BlogTreeShowTypeEnum.ONLY_BLOG.getType().equals(showType)) {
            return blogList;
        }
        root.setCategoryId(categoryId);
        root.setChildren(blogList);
        resultList.add(root);
        return resultList;
    }

    public List<Blog> convertLine2Tree(List<Blog> dataList, String pid) {
        List<Blog> children = null;
        for (Blog m : dataList) {
            if (m.getBlogId() != null && m.getpBlogId() != null && m.getpBlogId().equals(pid)) {
                m.setChildren(convertLine2Tree(dataList, m.getBlogId()));
                if (children == null) {
                    children = new ArrayList<>();
                }
                children.add(m);
            }
        }
        return children;
    }

    @Override
    public List<BlogVO> getBlogListTree4VO(Integer categoryId, Integer showType) {
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setCategoryId(categoryId);
        blogQuery.setStatus(BlogStatusEnum.STATUS_1.getStatus());
        blogQuery.setDelType(BlogDelTypeEnum.NORMAL.getType());
        blogQuery.setOrderBy("sort asc");
        List<Blog> blogList = this.blogMapper.selectList(blogQuery);

        List<BlogVO> blogVOList = CopyTools.copyList(blogList, BlogVO.class);

        blogVOList = convertLine2Tree4VO(blogVOList, "0");
        List<Blog> resultList = new ArrayList<>();
        Blog root = new Blog();
        root.setBlogId("0");
        if (BlogTreeShowTypeEnum.ALL.getType().equals(showType)) {
            root.setTitle("全部");
        } else if (BlogTreeShowTypeEnum.WITH_CATEGORY.getType().equals(showType)) {
            BlogCategory blogCategory = this.blogCategoryMapper.selectByCategoryId(categoryId);
            root.setTitle(blogCategory.getCategoryName());
        } else if (BlogTreeShowTypeEnum.ONLY_BLOG.getType().equals(showType)) {
            return blogVOList;
        }
        root.setCategoryId(categoryId);
        root.setChildren(blogList);
        resultList.add(root);
        return blogVOList;
    }


    public List<BlogVO> convertLine2Tree4VO(List<BlogVO> dataList, String pid) {
        List<BlogVO> children = null;
        for (BlogVO m : dataList) {
            if (m.getBlogId() != null && m.getpBlogId() != null && m.getpBlogId().equals(pid)) {
                m.setChildren(convertLine2Tree4VO(dataList, m.getBlogId()));
                if (children == null) {
                    children = new ArrayList<>();
                }
                children.add(m);
            }
        }
        return children;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSpecialBlogSort(String blogId, String pBlogId, String blogIds) {
        Blog updateBlog = new Blog();
        updateBlog.setpBlogId(pBlogId);
        blogMapper.updateByBlogId(updateBlog, blogId);
        String[] blogIdArray = blogIds.split(",");
        Integer index = 0;
        for (String id : blogIdArray) {
            updateBlog = new Blog();
            updateBlog.setBlogId(id);
            updateBlog.setSort(index);
            blogMapper.updateByBlogId(updateBlog, id);
            index++;
        }
    }
}