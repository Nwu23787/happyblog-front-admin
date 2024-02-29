import httpRequest from "../utils/request";

/**
 * 登录获取用户信息接口
 * @param {*} data 表单参数
 * @return 用户信息
 */
export const loginAPI = (data) => {
  return httpRequest({
    url: "/login",
    method: "POST",
    data,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 * 登出
 * @returns null
 */
export const logoutAPI = () => {
  return httpRequest({
    url: "/logout",
    method: "GET",
  });
};
