import httpRequest from "../utils/request";

/**
 * 提交用户信息接口
 * @param {Object} 用户信息对象
 * @returns
 */
export const saveMyInfoAPI = (data) => {
  return httpRequest({
    url: "/saveMyInfo",
    method: "POST",
    data,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 * 获取用户信息接口
 * @returns 用户信息
 */
export const getUserInfoAPI = () => {
  return httpRequest({
    url: "/getUserInfo",
    method: "get",
  });
};

/**
 * 修改自己的密码接口
 * @param {string} password 新密码
 * @returns
 */
export const updateMyPasswordAPI = (password) => {
  return httpRequest({
    url: "/updateMyPassword",
    method: "POST",
    data: {
      password,
    },
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 * 获取用户列表
 * @param {*} params
 * @returns 用户列表数组
 */
export const getUserListAPI = (params) => {
  return httpRequest({
    url: "/setting/loadUser",
    method: "GET",
    params,
  });
};

/**
 * 新增用户接口
 * @param {Object} data 新增用户对象
 * @returns null
 */
export const saveTeamUserAPI = (data) => {
  return httpRequest({
    url: "/setting/saveTeamUser",
    method: "POST",
    data,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 *
 * @param {String} userId 用户ID
 * @param {String} status 修改后的状态
 * @returns null
 */
export const updateStatusAPI = (userId, status) => {
  return httpRequest({
    url: "/setting/updateStatus",
    method: "POST",
    data: {
      userId,
      status,
    },
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 * 修改用户密码接口
 * @param {String} userId 用户ID
 * @param {String} password 新密码
 * @returns null
 */
export const updatePasswordAPI = (userId, password) => {
  return httpRequest({
    url: "/setting/updatePassword",
    method: "POST",
    data: {
      userId,
      password,
    },
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 * 删除用户接口
 * @param {String} userId 删除的用户ID
 * @returns null
 */
export const delUserAPI = (userId) => {
  return httpRequest({
    url: "/setting/delUser",
    method: "GET",
    params: {
      userId,
    },
  });
};

/**
 * 保存系统设置接口
 * @param {Object} data 表达对象
 * @returns null
 */
export const saveSysSettingAPI = (data) => {
  return httpRequest({
    url: "/sysSetting/saveSysSetting",
    method: "POST",
    data,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
    },
  });
};

/**
 * 获取系统设置接口
 * @returns data
 */
export const getSysSettingAPI = () => {
  return httpRequest({
    url: "/sysSetting/getSysSetting",
    method: "GET",
  });
};

/**
 * 下载页面接口
 * @returns 下载路径
 */
export const createZipAPI = () => {
  return httpRequest({
    url: "/sysSetting/createZip",
    method: "GET",
  });
};
