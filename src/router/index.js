import { createRouter, createWebHistory } from "vue-router";
import { useUserInfoStore } from "../store/index";
const routes = [
  {
    name: "home",
    path: "/",
    component: () => import("../views/Framework.vue"),
    redirect: "/login",
    children: [
      // 博客
      {
        name: "博客",
        path: "blog",
        redirect: "blog/list",
        children: [
          {
            name: "博客管理",
            path: "list",
            component: () => import("@/views/blog/BlogList.vue"),
          },
          {
            name: "分类管理",
            path: "category",
            component: () => import("@/views/blog/BlogCategory.vue"),
          },
        ],
      },
      // 专题
      {
        name: "专题",
        path: "special",
        redirect: "special/list",

        children: [
          {
            name: "专题管理",
            path: "list",
            component: () => import("@/views/special/specialList.vue"),
          },
        ],
      },
      // 设置
      {
        name: "设置",
        path: "setting",
        redirect: "setting/my",
        children: [
          {
            name: "个人信息设置",
            path: "my",
            component: () => import("@/views/setting/MySetting.vue"),
          },
          {
            name: "博客成员",
            path: "user",
            component: () => import("@/views/setting/UserSetting.vue"),
          },
          {
            name: "系统设置",
            path: "sysSetting",
            component: () => import("@/views/setting/SysSetting.vue"),
          },
        ],
      },
      {
        name: "回收站",
        path: "recovery",
        redirect: "recovery/list",
        children: [
          {
            name: "回收站",
            path: "list",
            component: () => import("@/views/recovery/RecoveryList.vue"),
          },
        ],
      },
    ],
  },
  {
    name: "login",
    path: "/login",
    component: () => import("@/views/Login.vue"),
  },
  {
    name: "404",
    path: "/:catchAll(.*)",
    component: () => import("@/views/404.vue"),
  },
];

const router = createRouter({
  routes,
  history: createWebHistory(),
});

router.beforeEach((to, from, next) => {
  const userInfoStore = useUserInfoStore();
  if (to.path === "/login" || to.path === "/" || to.path === "404") {
    next();
  } else {
    if (userInfoStore.userInfo.userId) {
      // 已登录，放行
      next();
    } else {
      ElMessage({
        type: "warning",
        message: "请先登录",
      });
      // 未登录，跳转登录页
      next({ path: "/login" });
    }
  }
});

export default router;
