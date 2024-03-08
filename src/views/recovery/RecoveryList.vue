<script setup>
import { nextTick, onMounted, reactive, ref, watch } from "vue";
import { useRouter } from "vue-router";
import {
  loadRecoveryListAPI,
  reductionBlogAPI,
  delBlogAPI,
} from "@/api/recovery.js";
import { genFileId } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import hljs from "highlight.js";
import "highlight.js/styles/atom-one-light.css";
import { WarningFilled } from "@element-plus/icons-vue";
import { useUserInfoStore } from "@/store/index.js";

// 博客列表数据
const tableData = ref([]);

// 总博客条数
const totalBlogCount = ref(0);
// 分页信息
const page = ref({
  pageNo: 1,
  pageSize: 10,
});

const userInfoStore = useUserInfoStore();
// 获取用户信息
const userInfo = ref(userInfoStore.userInfo);

// 博客筛选
const blogFilter = reactive({
  titleFuzzy: "",
});

// 获取博客列表
const getBlogList = async () => {
  const res = await loadRecoveryListAPI({ ...page.value, ...blogFilter });
  tableData.value = res.list;
  totalBlogCount.value = res.totalCount;
};

onMounted(() => {
  getBlogList();
});

// 渲染获取博客类型
const getBlogType = (type) => {
  if (type === 0) return "原创";
  else if (type === 1) return "转载";
  else return "未知";
};

// 博客筛选搜索
const searchBlog = () => {
  getBlogList();
};

// 还原博客
const handleRecover = async (id) => {
  await reductionBlogAPI(id);
  ElMessage({
    type: "success",
    message: "已还原",
  });
  getBlogList();
};

const handleDelete = async (id) => {
  await delBlogAPI(id);
  ElMessage({
    type: "success",
    message: "删除成功",
  });
  getBlogList();
};
</script>

<template>
  <div>
    <el-form :model="blogFilter" :inline="true">
      <el-form-item label="标题">
        <el-input
          v-model="blogFilter.titleFuzzy"
          placeholder="请输入标题"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchBlog">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="tableData" style="width: 100%" class="table">
      <el-table-column label="封面" width="100">
        <template #default="scope">
          <el-image
            style="width: 60px; height: 60px"
            :src="'/api/file/getImage/' + scope.row.cover"
            fit="cover"
          >
            <template #placeholder>
              <div class="image-slot">Loading<span class="dot">...</span></div>
            </template>
            <template #error>
              <div class="image-slot">无封面</div>
            </template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column label="文章信息" width="300">
        <template #default="scope">
          <div class="title">标题：{{ scope.row.title }}</div>
          <div class="categoryName">
            文章类型：{{ scope.row.blogType ? "专题 | 专题：" : "博客 | 分类："
            }}{{ scope.row.categoryName }}
          </div>
          <div class="nickname">作者：{{ scope.row.nickName }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="editorTypeName" label="编辑器" width="100" />
      <el-table-column label="类型" width="120">
        <template #default="scope">
          类型：{{ getBlogType(scope.row.type) }}
        </template>
      </el-table-column>
      <el-table-column prop="allowCommentTypeName" label="评论" width="100" />
      <el-table-column label="状态" width="100">
        <template #default="scope">
          <span
            :class="{
              status_publish: scope.row.status,
              status_draft: !scope.row.status,
            }"
          >
            {{ scope.row.statusName }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="时间" width="260">
        <template #default="scope">
          <div class="createTime">创建时间：{{ scope.row.createTime }}</div>
          <div class="lastUpdateTime">
            更新时间：{{ scope.row.lastUpdateTime }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <span v-if="scope.row.userId === userInfo.userId">
            <el-popconfirm
              title="确认还原此博客吗？"
              confirm-button-text="确认"
              cancel-button-text="取消"
              @confirm="handleRecover(scope.row.blogId)"
              ><template #reference>
                <el-link type="primary" :underline="false">还原 </el-link>
              </template>
            </el-popconfirm>
            <el-popconfirm
              title="确认永久删除此博客吗？"
              confirm-button-text="确认"
              cancel-button-text="取消"
              :icon="WarningFilled"
              icon-color="#F56C6C"
              @confirm="handleDelete(scope.row.blogId)"
              ><template #reference>
                <el-link type="primary" :underline="false">删除 </el-link>
              </template>
            </el-popconfirm>
          </span>
          <span v-else>---</span>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-model:current-page="page.pageNo"
      v-model:page-size="page.pageSize"
      background
      layout="sizes,prev, pager, next"
      :page-sizes="[5, 10, 15, 20]"
      :total="totalBlogCount"
      class="mt-4"
      @size-change="getBlogList"
      @current-change="getBlogList"
      :pager-count="6"
    />
  </div>
</template>

<style lang="scss" scoped>
.table {
  font-size: 12px;
  height: calc(100vh - 217px);
  margin-bottom: 15px;
  .status_publish {
    color: green;
  }
  .status_draft {
    color: red;
  }
  .el-link {
    font-size: 12px;
    margin: 0px 5px;
  }
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: var(--el-fill-color-light);
  color: var(--el-text-color-secondary);
  font-size: 14px;
}
</style>
