import httpRequest from "../utils/request";

/**
 * 获取回收站博客列表
 * @param {Object} data 搜索参数
 * @returns list
 */
export const loadRecoveryListAPI = (data) => {
  return httpRequest({
    url: "/blog/loadRecoveryList",
    method: "POST",
    data,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 * 删除回收站博客接口
 * @param {String} blogId 要删除的博客ID
 * @returns null
 */
export const delBlogAPI = (blogId) => {
  return httpRequest({
    url: "/blog/delBlog",
    params: {
      blogId,
    },
  });
};

/**
 * 恢复回收站博客接口
 * @param {String} blogId 要恢复的博客ID
 * @returns null
 */
export const reductionBlogAPI = (blogId) => {
  return httpRequest({
    url: "/blog/reductionBlog",
    params: {
      blogId,
    },
  });
};
