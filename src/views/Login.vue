<script setup>
import { ref, reactive, onMounted } from "vue";
import { loginAPI } from "@/api/user.js";
import md5 from "js-md5";
import VueCookies from "vue-cookies";
import { useUserInfoStore } from "@/store/index.js";
import { useRouter } from "vue-router";

const router = useRouter();
const form = reactive({
  username: "",
  password: "",
  code: "",
  isRemember: true,
});

//检查cookie函数
const uname = VueCookies.get("username");
if (uname) {
  form.username = uname;
}
const checkcodeURL = ref("api/checkCode");
const loginFormRef = ref();

const rules = reactive({
  username: [{ required: true, message: "用户名不能为空！" }],
  password: [{ required: true, message: "密码不能为空！" }],
  code: [{ required: true, message: "验证码不能为空！" }],
});
//点击修改验证码
const changeCode = () => {
  checkcodeURL.value = "api/checkCode?" + new Date().getTime();
};

const onSubmit = async () => {
  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      const userInfoStore = useUserInfoStore();
      await userInfoStore.getUserInfo({
        account: form.username,
        password: md5(form.password),
        checkCode: form.code,
      });
      //登录成功，提示
      ElMessage({
        message: "登录成功",
        type: "success",
      });
      // 等待1秒跳转
      setTimeout(() => {
        router.push("/");
      }, 1000);
      // 如果设置了记住我，设置cookie
      if (form.isRemember) {
        // 保存两天过期
        VueCookies.set("username", form.username, "0");
      } else {
        //没有设置记住我，清除cookie
        if (VueCookies.get("username")) {
          VueCookies.remove("username");
        }
      }
    }
  });
};
</script>

<template>
  <div class="login_body">
    <div class="login_panel">
      <h2>用户登录</h2>
      <el-form :model="form" size="large" :rules="rules" ref="loginFormRef">
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            clearable
          >
            <template #prefix>
              <span class="iconfont icon-account"> </span>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            placeholder="请输入密码"
            type="password"
            show-password
          >
            <template #prefix>
              <span class="iconfont icon-password"> </span> </template
          ></el-input>
        </el-form-item>
        <el-form-item prop="code">
          <el-input
            v-model="form.code"
            placeholder="请输入验证码"
            style="width: 50%"
          ></el-input>
          <el-image :src="checkcodeURL" @click="changeCode" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="form.isRemember" label="记住我" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.login_body {
  position: relative;
  width: 100%;
  height: 100vh;
  background-size: cover;
  background-position: center;
  background-image: url("../assets/bgImage.jpg");
  .login_panel {
    position: absolute;
    text-align: center;
    width: 300px;
    height: 350px;
    padding: 0px 30px 30px 30px;
    background-color: rgb(236, 233, 233, 0.75);
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    box-shadow: 0 0 2px 2px #dededd;
    border-radius: 5px;
    .el-button {
      width: 100%;
    }
    .el-image {
      height: 100%;
      padding-left: 10px;
      cursor: pointer;
    }
  }
}
</style>
