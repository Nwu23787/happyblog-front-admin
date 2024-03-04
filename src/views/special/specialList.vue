<script setup>
import { nextTick, onMounted, reactive, ref, watch } from "vue";
import {
  getSpecialListAPI,
  saveSpecialAPI,
  delSpecialAPI,
  getSpecialInfoAPI,
  saveSpecialEssayAPI,
  updateSpecialBlogSortAPI,
} from "@/api/special.js";
import { Plus } from "@element-plus/icons-vue";
import { getBlogByIdAPI, recoveryBlogByIdAPI } from "@/api/blog.js";
import hljs from "highlight.js";
import "highlight.js/styles/atom-one-light.css";
import { useUserInfoStore } from "@/store/index.js";

// 专题列表
const specialList = ref([]);

// 分页对象
const page = reactive({});

// 获取专题列表
const getSpecialList = async () => {
  const res = await getSpecialListAPI();
  specialList.value = res.list;
  console.log(res);
};

const userInfoStore = useUserInfoStore();
// 获取用户信息
const userInfo = ref(userInfoStore.userInfo);

// 新增专题弹窗
const showAddDialog = ref(false);

// 新增专题对像
const addObj = reactive({
  categoryName: "",
  cover: "",
  categoryDesc: "",
});

// 新增修改专题封面
const addSpecialFileList = ref([]);

// 控制只能上传一张封面
const handleFileChange = (file, fileList) => {
  if (fileList.length > 1) {
    fileList.shift();
  }
};

// 上传成功回调
const handleAvatarSuccess = (a, b) => {
  if (a.code === 200) {
    addObj.cover = a.data.fileName;
  }
};

// 获取新增专题表单对象
const addFormRef = ref();

// 新增专题表单校验规则
const addRules = ref({
  categoryName: [{ required: true, message: "专题名称不能为空！" }],
  categoryDesc: [{ required: true, message: "专题简介不能为空！" }],
});

// 确认新增
const addSpecialConfirm = () => {
  addFormRef.value.validate(async (valid) => {
    if (valid) {
      await saveSpecialAPI(addObj);
      showAddDialog.value = false;
      // 刷新列表
      getSpecialList();
      // 提示
      ElMessage({
        type: "success",
        message: addObj.categoryId ? "修改成功！" : "新增成功！",
      });
    }
  });
};

// 修改
const updateSpecialBtn = (data) => {
  showAddDialog.value = true;
  nextTick(() => {
    addObj.categoryDesc = data.categoryDesc;
    addObj.categoryName = data.categoryName;
    addObj.cover = data.cover;
    addObj.categoryId = data.categoryId;
    // 添加默认封面文件
    if (addObj.cover) {
      console.log(1234);
      addSpecialFileList.value.push({
        name: "cover",
        url: "/api/file/getImage/" + addObj.cover,
      });
    }
  });

  console.log(addObj);
};

// 关闭专题弹窗回调
const beforeCloseDialog = () => {
  // 重置表单
  addFormRef.value.resetFields();
  delete addObj.categoryId;
  console.log("重置", addObj);
  // 清空默认上传文件
  addSpecialFileList.value = [];
};

// 删除确认框
const openDeleteConfirm = async (id) => {
  try {
    await ElMessageBox.confirm("确认删除吗？", "提示", {
      confirmButtonText: "确认",
      cancelButtonText: "取消",
      type: "warning",
    });
    // 删除
    await delSpecialAPI(id);
    // 重新获取数据
    getSpecialList();
    ElMessage({
      type: "success",
      message: "删除成功！",
    });
  } catch (error) {}
};

const catagoryData = ref([]);
const defaultCategoryId = ref(null);

// 获取树形列表信息
const getTreeInfo = async () => {
  const res = await getSpecialInfoAPI(defaultCategoryId.value, 1);
  catagoryData.value = res;
};

onMounted(async () => {
  await getSpecialList();
  defaultCategoryId.value = specialList.value[0].categoryId;
  getTreeInfo();
});

// 点击专题，展示响应的专题文章树形列表
const showCategoryTree = async (a) => {
  defaultCategoryId.value = a.categoryId;
  await getTreeInfo();
};

// 树形控件配置项
const defaultProps = ref({
  children: "children",
  label: "title",
});

const showEditor = ref(false);

const specialObj = reactive({
  title: "",
  categoryId: "",
  content: "",
  markdownContent: "",
  editorType: 1,
  pBlogId: null,
});

// 清空新增文章对象
const initSpecialObj = () => {
  specialObj.title = "";
  specialObj.categoryId = "";
  specialObj.content = "";
  specialObj.markdownContent = "";
  specialObj.editorType = 1;
  specialObj.pBlogId = null;
  delete specialObj.blogId;
};

// 新增文章
const append = (data) => {
  console.log("ada", data);
  showEditor.value = true;
  specialObj.categoryId = data.categoryId;
  specialObj.pBlogId = data.blogId;
};

// 新增文章表单验证规则
const addBlogRules = ref({
  title: [{ required: true, message: "标题不能为空！" }],
});

// 新增文章表单对象
const addBlogFormRef = ref();

// 回退
const goBack = () => {
  showEditor.value = false;
  // 清空编辑器
  initSpecialObj();
};

// 确认新增文章
const confirmAdd = () => {
  addBlogFormRef.value.validate(async (valid) => {
    if (valid) {
      await saveSpecialEssayAPI(specialObj);
      ElMessage({
        type: "success",
        message: specialObj.blogId ? "修改成功" : "添加成功",
      });
      // 关闭编辑器
      setTimeout(() => {
        goBack();
        // 重新获取数据
        getTreeInfo();
        getSpecialList();
      }, 1000);
    }
  });
};

// 修改专题博客
const updateSpecial = async (data) => {
  console.log(data);
  const res = await getBlogByIdAPI(data.blogId);
  specialObj.title = data.title;
  specialObj.categoryId = data.categoryId;
  specialObj.content = res.content;
  specialObj.markdownContent = res.markdownContent;
  specialObj.editorType = data.editorType;
  specialObj.pBlogId = data.pBlogId;
  specialObj.blogId = data.blogId;
  // 打开编辑器
  showEditor.value = true;
};

// 删除专题博客
const delSpecialBlog = async (data) => {
  console.log(data);
  if (data.children) {
    return ElMessage({
      type: "error",
      message: "请先删除下级文章",
    });
  }
  await ElMessageBox.confirm("确认删除吗？", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
  });
  await recoveryBlogByIdAPI(data.blogId);
  ElMessage({
    type: "success",
    message: "删除成功！",
  });
  // 重新获取数据
  getTreeInfo();
  getSpecialList();
};

// 拖拽排序
const nodeDrop = async (draggingNode, dropNode, dropType, ev) => {
  let parentNode = null;
  if (dropType === "inner") {
    // 拖到节点内
    parentNode = dropNode;
  } else {
    // 放在目标节点的前后，就是和目标节点同级
    parentNode = dropNode.parent;
  }
  console.log(parentNode);
  const blogId = draggingNode.data.blogId;
  const pBlogId = parentNode.data.blogId;
  const blogIds = [];
  parentNode.childNodes.forEach((item) => {
    blogIds.push(item.data.blogId);
  });
  const res = await updateSpecialBlogSortAPI({
    blogId,
    pBlogId,
    blogIds: blogIds.join(","),
  });
  if (!res) {
    return;
  }
  w;
  // 刷新列表数据
  getTreeInfo();
};

const showDetail = ref(false);
const blogDetail = ref("");
// 打开预览页面
const handlePreview = async (id) => {
  const res = await getBlogByIdAPI(id);
  blogDetail.value = res;
  console.log(res);
  showDetail.value = true;
  nextTick(() => {
    let blocks = document.querySelectorAll("pre code");
    blocks.forEach((block) => {
      hljs.highlightBlock(block);
    });
  });
};

// 从预览页面返回
const detailGoBack = () => {
  showDetail.value = false;
};
</script>

<template>
  <div class="main" v-if="!showEditor && !showDetail">
    <div class="header">
      <el-button type="danger" @click="showAddDialog = true"
        >新增专题</el-button
      >
    </div>
    <el-row class="body" :gutter="20">
      <el-col :span="14">
        <el-card
          shadow="always"
          class="body_left"
          :body-style="{ height: '75%' }"
        >
          <template #header>
            <div class="header">
              <span>专题</span>
            </div>
          </template>
          <!-- card body -->
          <el-table
            :data="specialList"
            class="left_table"
            @row-click="showCategoryTree"
          >
            <el-table-column label="封面" width="100">
              <template #default="scope">
                <el-image
                  style="width: 50px; height: 50px"
                  :src="'/api/file/getImage/' + scope.row.cover"
                  fit="cover"
                >
                  <template #placeholder>
                    <div class="image-slot">
                      Loading<span class="dot">...</span>
                    </div>
                  </template>
                  <template #error>
                    <div class="image-slot">无封面</div>
                  </template>
                </el-image>
              </template>
            </el-table-column>
            <el-table-column label="名称" width="150">
              <template #default="scope">
                <div>{{ scope.row.categoryName }}</div>
                <div>作者：{{ scope.row.nickName }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="categoryDesc" label="简介" width="200">
            </el-table-column>
            <el-table-column prop="blogCount" label="博客数量" width="100">
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <span v-if="userInfo.userId === scope.row.userId">
                  <el-link
                    type="primary"
                    :underline="false"
                    @click="updateSpecialBtn(scope.row)"
                    >修改</el-link
                  >
                  <el-link
                    type="primary"
                    :underline="false"
                    @click="openDeleteConfirm(scope.row.categoryId)"
                    >删除</el-link
                  >
                </span>
                <span v-else>---</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card
          shadow="always"
          class="body_left"
          :body-style="{ height: '75%' }"
        >
          <template #header>
            <div>
              <span>专题文章</span>
            </div>
          </template>
          <!-- card body -->
          <el-alert
            title="拖动文章修改排序"
            type="info"
            show-icon
            :closable="false"
          />
          <el-tree
            :data="catagoryData"
            :props="defaultProps"
            empty-text="点击左侧专题展示详情"
            default-expand-all
            :expand-on-click-node="false"
            draggable
            @node-drop="nodeDrop"
          >
            <template #default="{ node, data }">
              <span class="custom-tree-node">
                <span>{{ node.label }}</span>
                <span v-if="node.level === 1">
                  <el-link
                    type="primary"
                    @click="append(data)"
                    :underline="false"
                    >新增文章</el-link
                  >
                </span>
                <span v-else>
                  <el-link
                    type="primary"
                    :underline="false"
                    @click="handlePreview(data.blogId)"
                    >预览</el-link
                  >
                  <el-link
                    type="primary"
                    :underline="false"
                    @click="updateSpecial(data)"
                    v-if="userInfo.userId === data.userId"
                    >修改</el-link
                  >
                  <el-link
                    type="primary"
                    :underline="false"
                    @click="delSpecialBlog(data)"
                    v-if="userInfo.userId === data.userId"
                    >删除</el-link
                  >
                  <el-link
                    type="primary"
                    :underline="false"
                    @click="append(data)"
                    >新增下级文章</el-link
                  >
                </span>
              </span>
            </template>
          </el-tree>
        </el-card>
      </el-col>
    </el-row>
  </div>
  <!-- 新增文章编辑页面 -->
  <div class="editor" v-else-if="showEditor && !showDetail">
    <el-page-header @back="goBack" class="editor_header">
      <template #content>
        <span class="text-large font-600 mr-3">
          {{ specialObj.blogId ? "修改文章" : "新增文章" }}
        </span>
      </template>
    </el-page-header>
    <div class="editor_body">
      <el-form
        :model="specialObj"
        ref="addBlogFormRef"
        :rules="addBlogRules"
        label-width="55px"
        :inline="true"
      >
        <el-form-item label="标题" prop="title">
          <el-input
            v-model="specialObj.title"
            placeholder="请输入文章标题"
          ></el-input>
        </el-form-item>
      </el-form>
      <markdown-editor
        v-model:markdownText="specialObj.markdownContent"
        v-model:htmlText="specialObj.content"
        :height="510"
      ></markdown-editor>
    </div>
    <div class="editor_footer">
      <el-button type="primary" @click="goBack">取消</el-button>
      <el-button type="danger" @click="confirmAdd">确认</el-button>
    </div>
  </div>
  <!-- 预览页面 -->
  <div class="detail_page" v-else-if="showDetail">
    <el-page-header @back="detailGoBack" class="editor_header">
      <template #content>
        <span class="text-large font-600 mr-3"> {{ blogDetail.title }} </span>
      </template>
      <div class="editor_body" v-html="blogDetail.content"></div>
    </el-page-header>
  </div>
  <!-- 新增专题弹窗 -->
  <el-dialog
    title="新增专题"
    v-model="showAddDialog"
    width="30%"
    @close="beforeCloseDialog"
  >
    <el-form
      :model="addObj"
      ref="addFormRef"
      :rules="addRules"
      label-width="80px"
      :inline="false"
    >
      <el-form-item label="名称" prop="categoryName">
        <el-input
          v-model="addObj.categoryName"
          placeholder="请输入专题名称"
        ></el-input>
      </el-form-item>
      <el-form-item label="封面" prop="cover">
        <el-upload
          ref="upload"
          accept=".png,.jpg,.PNG,.JPG,.jpeg,.JPEG,.gif,.GIF,.bmp,.BMP"
          action="/api/file/uploadImage"
          list-type="picture-card"
          :auto-upload="true"
          :on-success="handleAvatarSuccess"
          :file-list="addSpecialFileList"
          :on-change="handleFileChange"
        >
          <el-icon><Plus /></el-icon>
          <template #file="{ file }">
            <div>
              <img
                class="el-upload-list__item-thumbnail"
                :src="file.url"
                alt=""
              />
            </div>
          </template>
        </el-upload>
      </el-form-item>
      <el-form-item label="简介" prop="categoryDesc">
        <el-input
          v-model="addObj.categoryDesc"
          placeholder="请输入简介"
          type="textarea"
          :autosize="{ minRows: 3, maxRows: 5 }"
        ></el-input>
      </el-form-item>
    </el-form>

    <template #footer>
      <span>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="addSpecialConfirm">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped>
.body {
  height: calc(100vh - 165px);
  margin-top: 10px;
  .body_left {
    height: 100%;
  }
  .body_right {
    height: 100%;
  }
  .el-link {
    font-size: 12px;
    margin: 0px 5px;
  }
  .image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: var(--el-fill-color-light);
    color: var(--el-text-color-secondary);
    font-size: 10px;
  }
  .left_table {
    font-size: 12px;
    height: calc(100vh - 260px);
  }
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }
}

.editor {
  .editor_header {
    margin-bottom: 20px;
  }
  .editor_footer {
    text-align: center;
    margin-top: 15px;
  }
}

.detail_page {
  height: calc(100vh - 120px);
  overflow: auto;
}
</style>
