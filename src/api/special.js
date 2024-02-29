import httpRequest from "../utils/request";

/**
 * 获取专题列表API
 * @returns Array 专题列表
 */
export const getSpecialListAPI = () => {
  return httpRequest({
    url: "/category/loadCategory4Special",
  });
};

/**
 *  新增/修改专题接口
 * @param {Object} data 新增/修改专题对象
 * @returns
 */
export const saveSpecialAPI = (data) => {
  return httpRequest({
    url: "/category/saveCategory4Special",
    method: "POST",
    data,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 * 删除专题接口
 * @param {*} id 要删除的专题id
 * @returns
 */
export const delSpecialAPI = (id) => {
  return httpRequest({
    url: "/category/delCategory4Special",
    method: "POST",
    data: {
      categoryId: id,
    },
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 * 获取专题详情列表
 * @param {Number} id 专题id
 * @param {Number} type 类型
 * @returns
 */
export const getSpecialInfoAPI = (id, type) => {
  return httpRequest({
    url: "/blog/getSpecialInfo",
    method: "POST",
    data: {
      categoryId: id,
      showType: type,
    },
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 * 保存专题文章
 * @param {Object} data 专题文章对象
 * @returns
 */
export const saveSpecialEssayAPI = (data) => {
  return httpRequest({
    url: "/blog/saveBlog4Special",
    method: "POST",
    data,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 * 修改专题文章排序
 * @param {object} data
 * @returns
 */
export const updateSpecialBlogSortAPI = (data) => {
  return httpRequest({
    url: "/blog/updateSpecialBlogSort",
    method: "POST",
    data,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 * ！！！接口已失效
 * 自动保存专题博客接口
 * @param {Object} data 自动保存的博客对象
 * @returns string 博客id
 */
export const autoSaveSpecialBlogAPI = (data) => {
  return httpRequest({
    url: "/blog/autoSaveBlog4Special",
    method: "POST",
    data,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};
