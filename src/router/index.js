import { createRouter, createWebHistory } from "vue-router";
const routes = [
  {
    name: "home",
    path: "/",
    component: () => import("../views/index.vue"),
    children: [
      {
        path: "tiger",
        component: () => import("../views/tiger.vue"),
      },
    ],
  },
];

const router = createRouter({
  routes,
  history: createWebHistory(),
});

export default router;
