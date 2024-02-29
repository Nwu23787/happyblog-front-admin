import httpRequest from "../utils/request";

/**
 *获取博客列表接口
 * @param {*} params 列表参数
 * @returns 博客列表
 */
export const getBlogListAPI = (params) => {
  return httpRequest({
    url: "/blog/loadBlog",
    method: "GET",
    params,
  });
};

/**
 * 上传博客内容图片接口
 * @param {*} file 文件
 * @param {0/1} type 类型，封面0，博客图片1
 * @returns
 */
export const uploadImgAPI = (file, type) => {
  return httpRequest({
    url: "/file/uploadImage",
    method: "POST",
    data: {
      file,
      type,
    },
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
};

/**
 * 新增或修改博客接口
 * @param {Object} data 新增博客对象
 * @returns null
 */
export const addBlogAPI = (data) => {
  return httpRequest({
    url: "/blog/saveBlog",
    method: "POST",
    data,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 * 根据博客id获取博客详情
 * @param {String} id 博客id
 * @returns Object
 */
export const getBlogByIdAPI = (id) => {
  return httpRequest({
    url: "/blog/getBlogById",
    params: {
      blogId: id,
    },
  });
};

/**
 * 根据博客id将博客移动到回收站
 * @param {String} id 博客id
 * @returns null
 */
export const recoveryBlogByIdAPI = (id) => {
  return httpRequest({
    url: "/blog/recoveryBlog",
    params: {
      blogId: id,
    },
  });
};

/**
 * 自动保存博客接口
 * @param {Object} data 自动保存的博客对象
 * @returns string 博客id
 */
export const autoSaveBlogAPI = (data) => {
  return httpRequest({
    url: "/blog/autoSaveBlog",
    method: "POST",
    data,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 * 发布接口
 * @returns
 */
export const createHtmlAPI = (data) => {
  return httpRequest({
    url: "/createHtml",
  });
};

/**
 * 发布进度查询接口
 * @returns
 */
export const checkProgressAPI = () => {
  return httpRequest({
    url: "/checkProgress",
  });
};
