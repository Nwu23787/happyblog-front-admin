import httpRequest from "../utils/request";

/**
 * 获取博客分类列表
 * @returns 博客分类列表
 */
export const getCategoryListAPI = () => {
  return httpRequest({
    url: "/category/loadAllCategory4Blog",
    method: "GET",
  });
};

/**
 * 修改或新增分类接口
 * @param {object} data 修改或新增的分类对象
 * @returns null
 */
export const addCategoeyAPI = (data) => {
  return httpRequest({
    url: "/category/saveCategory4Blog",
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
    data,
  });
};
/**
 * 删除分类接口
 * @param {object} id 删除分类的id号
 * @returns null
 */
export const delCategoeyAPI = (id) => {
  return httpRequest({
    url: "/category/delCategory4Blog",
    method: "GET",
    params: {
      categoryId: id,
    },
  });
};

/**
 *
 * @param {*} data JSON格式的完整的分类数组
 * @returns null
 */
export const sortCategoryAPI = (data) => {
  return httpRequest({
    url: "/category/changeCategorySort4Blog",
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data,
  });
};
