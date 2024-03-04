<script setup>
import { reactive, ref, watch } from "vue";
import { useUserInfoStore } from "@/store/index.js";
import { ArrowDown } from "@element-plus/icons-vue";
import { useRouter } from "vue-router";
import { logoutAPI } from "@/api/user.js";
import { ElMessage } from "element-plus";
import { createHtmlAPI, checkProgressAPI } from "@/api/blog.js";

const userInfoStore = useUserInfoStore();
const router = useRouter();
const menuList = reactive([
  {
    title: "博客",
    icon: "icon-blog",
    index: "1",
    children: [
      {
        title: "博客管理",
        index: "1-1",
        path: "/blog/list",
      },
      {
        title: "分类管理",
        index: "1-2",
        path: "/blog/category",
      },
    ],
  },
  {
    title: "专题",
    icon: "icon-zhuanti",
    index: "2",
    children: [
      {
        title: "专题管理",
        index: "2-1",
        path: "/special/list",
      },
    ],
  },
  {
    title: "设置",
    icon: "icon-shezhi",
    index: "3",
    children: [
      {
        title: "个人信息设置",
        index: "3-1",
        path: "/setting/my",
      },
      {
        title: "博客成员",
        index: "3-2",
        path: "/setting/user",
      },
      {
        title: "系统设置",
        index: "3-3",
        path: "/setting/sysSetting",
      },
    ],
  },
  {
    title: "回收站",
    index: "4",
    icon: "icon-huishouzhan",
    children: [
      {
        title: "回收站",
        index: "4-1",
        path: "/recovery/list",
      },
    ],
  },
]);

if (userInfoStore.userInfo.roleType != 1) {
  menuList[2].children.pop();
}

//发布博客
const publishBlog = () => {};

// 头像地址
let avatarSrc = ref("api/file/getImage/" + userInfoStore.userInfo.avatar);

// 如果头像出错，改为默认头像
const avatarErrorHandler = () => {
  avatarSrc.value =
    "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png";
};

// 点击头部博客名跳转主页
const toHome = () => {
  router.push("/blog/list");
};

// 退出登录
const logOut = async () => {
  await logoutAPI();
  ElMessage({
    type: "success",
    message: "已退出",
  });
  router.push("/login");
};

const progressObj = reactive({
  result: 1, // 1表示没有发生错误
  progress: 0, //表示进度
  errorMsg: "",
});

const showDialog = ref(false);
let timer = null;
// 发布
const handlePublish = async () => {
  await createHtmlAPI();
  // 展示进度组件
  showDialog.value = true;
  //发送进度查询请求
  timer = setInterval(async () => {
    const res = await checkProgressAPI();
    Object.assign(progressObj, res);
    console.log(res);
  }, 500);
};

const status = ref("");

watch(progressObj, () => {
  if (progressObj.result == 0) {
    status.value = "exception";
  }
  if (progressObj.progress == 100 && progressObj.result == 1) {
    status.value = "success";
    // 停止定时器
    clearInterval(timer);
  }
  console.log(11);
});

// 关闭对话框
const closeDialog = () => {
  showDialog.value = false;
  // 还原数据
  progressObj.errorMsg = "";
  progressObj.progress = 0;
  progressObj.result = 1;
};
</script>

<template>
  <div class="common-layout">
    <el-container class="container">
      <el-header class="header">
        <img src="@/assets/title.png" class="left" @click="toHome" />
        <div class="right">
          <span
            >欢迎回来，
            <el-dropdown>
              <span class="el-dropdown-link" style="outline: none">
                {{ userInfoStore.userInfo.nickName }}
                <el-icon class="el-icon--right">
                  <arrow-down />
                </el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="logOut">退出</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </span>
        </div>
        <el-avatar
          class="avatar"
          @error="avatarErrorHandler"
          :src="'/api/file/getImage/' + userInfoStore.userInfo.avatar"
        />
      </el-header>
      <el-container>
        <el-aside class="aside_bar">
          <el-button
            type="primary"
            size="default"
            @click="handlePublish"
            class="publish_btn"
            color="#459e0e"
            >发布</el-button
          >

          <el-menu
            class="el-menu-vertical-demo"
            background-color="#f0eff0"
            active-text-color="#ef7878"
            :default-active="router.currentRoute.value.fullPath"
            router
          >
            <el-sub-menu
              v-for="item in menuList"
              :key="item.index"
              :index="item.index"
            >
              <template #title>
                <span
                  class="iconfont"
                  :class="item.icon"
                  style="margin: 10px"
                ></span>
                <span>{{ item.title }}</span>
              </template>
              <el-menu-item
                v-for="item in item.children"
                :key="item.index"
                :index="item.path"
                >{{ item.title }}</el-menu-item
              >
            </el-sub-menu>
          </el-menu>
        </el-aside>
        <el-main class="main">
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
  <el-dialog
    v-model="showDialog"
    width="400"
    style="position: reactive; height: 250px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :show-close="false"
  >
    <div class="progress">
      <el-progress
        type="circle"
        :percentage="progressObj.progress"
        :status="status"
      />
    </div>
    <div class="btn">
      <el-button type="primary" v-if="status === 'success'" @click="closeDialog"
        >发布成功</el-button
      >
      <el-button type="danger" v-else-if="status === 'exception'"
        >发布失败</el-button
      >
      <el-text class="mx-1" type="warning" v-else>发布中...</el-text>
    </div>
  </el-dialog>
</template>

<style lang="scss" scoped>
.container {
  height: 100vh;
  background-color: #f0eff0;
  .header {
    position: relative;
    background-color: #fff;
    margin-bottom: 20px;
    border-bottom: 1px solid #afadad;
    .left {
      position: absolute;
      top: 50%;
      left: 10px;
      transform: translate(0, -50%);
      cursor: pointer;
    }
    .right {
      position: absolute;
      top: 50%;
      right: 80px;
      transform: translate(0, -50%);
      font-size: 14px;
      .el-dropdown-link {
        cursor: pointer;
        font-size: 14px;
        color: var(--el-color-primary);
        .el-icon {
          color: rgb(118, 117, 117);
        }
      }
    }
    .avatar {
      position: absolute;
      top: 50%;
      right: 30px;
      transform: translate(0, -50%);
    }
  }
  .aside_bar {
    background-color: #f0eff0;
    height: 100%;
    width: 200px;
    text-align: center;
    .publish_btn {
      width: 90%;
      height: 35px;
      margin-bottom: 10px;
    }
  }
  .main {
    background-color: #fff;
  }
}

.progress {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
.btn {
  position: absolute;
  left: 50%;
  top: 85%;
  transform: translate(-50%, -50%);
}
</style>
