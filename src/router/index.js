import { createRouter, createWebHistory } from "vue-router";
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
];

const router = createRouter({
  routes,
  history: createWebHistory(),
});

export default router;
