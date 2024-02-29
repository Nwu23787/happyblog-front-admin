import { defineStore } from "pinia";
import { loginAPI } from "@/api/user.js";
import { ref } from "vue";

export const useUserInfoStore = defineStore(
  "userInfo",
  () => {
    //用户信息
    const userInfo = ref({});
    //获取用户信息
    const getUserInfo = async (obj) => {
      const res = await loginAPI(obj);
      userInfo.value = res;
    };
    // 修改用户名
    const updateUsername = async (nickName) => {
      userInfo.value.nickName = nickName;
    };

    return { userInfo, getUserInfo, updateUsername };
  },
  {
    persist: true,
  }
);
