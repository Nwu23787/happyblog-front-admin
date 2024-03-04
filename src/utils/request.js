import axios from "axios";
import { ElMessage } from "element-plus";
import router from "../router";

const httpRequest = axios.create({
  baseURL: "/api",
  timeout: 5000,
});

//请求拦截器
httpRequest.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    ElMessage({
      message: "请求发送失败",
      type: "error",
    });
    return Promise.reject("请求发送失败");
  }
);

//响应拦截器
httpRequest.interceptors.response.use(
  (response) => {
    const { data, code, info } = response.data;
    //去除自动包裹的data
    if (code == 200) {
      return data;
    } else if (code == 901) {
      console.log(1234);
      ElMessage({
        type: "error",
        message: "身份验证失败，请重新登陆",
      });
      router.push("/login");
      return data;
    } else {
      ElMessage({
        message: info,
        type: "error",
      });
      return Promise.reject(new Error(info));
    }
  },
  (error) => {
    ElMessage({
      message: error.message,
      type: "error",
    });
    return Promise.reject(error);
  }
);

export default httpRequest;
