<script setup>
import { nextTick, onMounted, reactive, ref, watch } from "vue";
import { useRouter } from "vue-router";
import {
  getBlogListAPI,
  addBlogAPI,
  getBlogByIdAPI,
  recoveryBlogByIdAPI,
  autoSaveBlogAPI,
} from "@/api/blog.js";
import { getCategoryListAPI } from "@/api/category.js";
import { genFileId } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import hljs from "highlight.js";
import "highlight.js/styles/atom-one-light.css";
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

// 默认上传文件列表
const fileList = ref([]);

// 博客筛选
const blogFilter = reactive({
  titleFuzzy: "",
  status: "",
  categoryId: "",
});

// 获取博客列表
const getBlogList = async () => {
  const res = await getBlogListAPI({ ...page.value, ...blogFilter });
  tableData.value = res.list;
  totalBlogCount.value = res.totalCount;
};

//博客分类列表
const blogCateGoryList = ref([]);

// 获取博客分类列表
const getCategoryList = async () => {
  const res = await getCategoryListAPI();
  blogCateGoryList.value = res;
};

onMounted(() => {
  getCategoryList(null);
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

// 新增博客对象
let blogObj = reactive({
  title: "", //博客标题
  content: "", //html内容
  markdownContent: "", // markdown内容
  editorType: 1,
  categoryId: null, //博客分类id
  cover: null, //博客封面
  summary: null, //简介
  tag: [], //博客标签
  type: 0, //博客类型
  reprintUrl: null, //转载地址
  allowComment: 1, //是否允许评论
});

// 博客对象重置
const resetBlogObj = () => {
  blogObj = reactive({
    title: "", //博客标题
    content: "", //html内容
    markdownContent: "", // markdown内容
    editorType: 1,
    categoryId: null, //博客分类id
    cover: null, //博客封面
    summary: null, //简介
    tag: [], //博客标签
    type: 0, //博客类型
    reprintUrl: null, //转载地址
    allowComment: 1, //是否允许评论
  });
};

const showList = ref(true);
// 新增博客
const addBlog = () => {
  // 清空封面上传数据
  fileList.value = [];
  showList.value = false;
};

// 退出新增
const goBack = () => {
  showList.value = true;
  resetBlogObj();
  // 重新获取数据
  getBlogList();
};

//获取表单对象
const addFormRef = ref();

// 新增博客弹窗控制
const showAddDialog = ref(false);

// 确认新增
const confirmAdd = () => {
  addFormRef.value.validate((valid) => {
    if (valid) {
      if (!blogObj.content) {
        return ElMessageBox.alert("博客内容不能为空！", "提示");
      }
      showAddDialog.value = true;
    }
  });
};

// 表单验证
const rules = ref({
  title: [{ required: true, message: "博客标题不能为空！" }],
});

// 上传文件
// upload对象
const upload = ref();

const handleExceed = (files) => {
  upload.value.clearFiles();
  const file = files[0];
  file.uid = genFileId();
  upload.value.handleStart(file);
};

// 上传回调
const handleAvatarSuccess = (a, b) => {
  if (a.code === 200) {
    blogObj.cover = a.data.fileName;
  }
};

// 添加标签
const tag = ref("");
const showTagInput = ref(false);
const addTag = () => {
  // 关闭输入框
  showTagInput.value = false;
  if (tag.value && !blogObj.tag.find((item) => item === tag.value)) {
    blogObj.tag.push(tag.value);
  }
  // 清空输入值
  tag.value = "";
};

// 删除标签
const handleTagClose = (tag) => {
  blogObj.tag.splice(blogObj.tag.indexOf(tag), 1);
};

// 自定义指令，解决input框第一次之后不能自动聚焦的问题
const vFocus = {
  mounted: (el) => {
    el.focus();
  },
};

// 新增确认表单校验规则
const confirmRules = reactive({
  categoryId: [{ required: true, message: "请选择博客分类！" }],
  type: [{ required: true, message: "请选择博客类型！" }],
  reprintUrl: [{ required: true, message: "请输入转载地址！" }],
  allowComment: [{ required: true, message: "请选择是否允许评论！" }],
  summary: [{ required: true, message: "请输入博客摘要！" }],
});

// 确认新增博客弹窗表单对象
const confirmFormRef = ref();
const confirmAddBlog = () => {
  confirmFormRef.value.validate(async (valid) => {
    if (valid) {
      const res = await addBlogAPI(blogObj);
      //消息提示
      ElMessage({
        type: "success",
        message: "新增成功！",
      });
      // 重新获取列表
      getBlogList();
      // 重置表单
      addFormRef.value.resetFields();
      confirmFormRef.value.resetFields();
      // 清空封面上传数据
      fileList.value = [];
      // 清空博客内容数据
      blogObj.content = "";
      blogObj.markdownContent = "";
      blogObj.blogId = null;
      // 新增成功之后，关闭弹窗
      showAddDialog.value = false;
      // 返回列表
      showList.value = true;
    }
  });
};

// 修改
const handleEdit = async (id) => {
  const res = await getBlogByIdAPI(id);
  blogObj.blogId = id;
  blogObj.title = res.title;
  blogObj.content = res.content;
  blogObj.markdownContent = res.markdownContent;
  blogObj.editorType = res.editorType;
  blogObj.categoryId = res.categoryId;
  blogObj.cover = res.cover;
  blogObj.summary = res.summary;
  blogObj.tag = res.tag ? res.tag.split(",") : [];
  blogObj.type = res.type;
  blogObj.reprintUrl = res.reprintUrl;
  blogObj.allowComment = res.allowComment;
  if (res.cover) {
    fileList.value.push({
      name: "cover",
      url: "/api/file/getImage/" + res.cover,
    });
  }
  // 打开编辑页面
  showList.value = false;
};

// 保证只有一个文件上传
const handleFileChange = (file, fileList) => {
  if (fileList.length > 1) {
    fileList.shift();
  }
};

const handleDelete = async (id) => {
  const res = await recoveryBlogByIdAPI(id);
  // 重新获取列表
  getBlogList();
};

// 防抖定时器
let timer = null;
// 自动保存
watch(
  () => blogObj.markdownContent,
  (newValue, oldValue) => {
    if (timer) {
      //  重置定时器
      clearTimeout(timer);
    }
    if (!blogObj.title || !blogObj.markdownContent) return;
    timer = setTimeout(async () => {
      // 自动保存
      const res = await autoSaveBlogAPI({
        blogId: blogObj.blogId ? blogObj.blogId : null,
        editorType: 1,
        title: blogObj.title ? blogObj.title : null,
        content: blogObj.content,
        markdownContent: blogObj.markdownContent,
      });
      if (!blogObj.blogId) {
        // 没有id，加上
        blogObj.blogId = res;
      }
    }, 1000);
  }
);

// 展示详情页面
const showDetail = ref(false);
const blogDetail = ref("");

// 打开预览页面
const handlePreview = async (id) => {
  const res = await getBlogByIdAPI(id);
  blogDetail.value = res.content;
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
  <div v-if="showList && !showDetail">
    <el-form :model="blogFilter" :inline="true">
      <el-form-item label="标题">
        <el-input
          v-model="blogFilter.titleFuzzy"
          placeholder="请输入标题"
        ></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select
          v-model="blogFilter.status"
          placeholder="请选择状态"
          style="width: 140px"
        >
          <el-option label="草稿" value="0"></el-option>
          <el-option label="已发布" value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="分类">
        <el-select
          v-model="blogFilter.categoryId"
          placeholder="请选择分类"
          style="width: 140px"
        >
          <el-option
            v-for="item in blogCateGoryList"
            :key="item.categoryId"
            :label="item.categoryName"
            :value="item.categoryId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchBlog">搜索</el-button>
        <el-button type="danger" @click="addBlog">新增博客</el-button>
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
      <el-table-column label="文章信息" width="280">
        <template #default="scope">
          <div class="title">标题：{{ scope.row.title }}</div>
          <div class="categoryName">分类：{{ scope.row.categoryName }}</div>
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
      <el-table-column label="时间" width="230">
        <template #default="scope">
          <div class="createTime">创建时间：{{ scope.row.createTime }}</div>
          <div class="lastUpdateTime">
            更新时间：{{ scope.row.lastUpdateTime }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-link
            type="primary"
            :underline="false"
            @click="handleEdit(scope.row.blogId)"
            v-if="userInfo.userId === scope.row.userId"
            >修改
          </el-link>
          <el-popconfirm
            title="确认删除吗？"
            confirm-button-text="确认"
            cancel-button-text="取消"
            @confirm="handleDelete(scope.row.blogId)"
            v-if="userInfo.userId === scope.row.userId"
            ><template #reference>
              <el-link type="primary" :underline="false">删除 </el-link>
            </template>
          </el-popconfirm>

          <el-link
            type="primary"
            :underline="false"
            @click="handlePreview(scope.row.blogId)"
            >预览</el-link
          >
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
  <div class="editor" v-else-if="!showList && !showDetail">
    <el-page-header @back="goBack" class="editor_header">
      <template #content>
        <span class="text-large font-600 mr-3"> 新增博客 </span>
      </template>
    </el-page-header>
    <div class="editor_body">
      <el-form
        :model="blogObj"
        ref="addFormRef"
        :rules="rules"
        label-width="55px"
        :inline="true"
      >
        <el-form-item label="标题" prop="title">
          <el-input
            v-model="blogObj.title"
            placeholder="请输入博客标题"
          ></el-input>
        </el-form-item>
      </el-form>
      <markdown-editor
        v-model:markdownText="blogObj.markdownContent"
        v-model:htmlText="blogObj.content"
        :height="510"
      ></markdown-editor>
    </div>
    <div class="editor_footer">
      <el-button type="primary" @click="goBack">取消</el-button>
      <el-button type="danger" @click="confirmAdd">确认</el-button>
    </div>
  </div>
  <!-- 展示详情页面 -->
  <div class="detail_page" v-else-if="showDetail">
    <el-page-header @back="detailGoBack" class="editor_header">
      <template #content>
        <span class="text-large font-600 mr-3"> 预览 </span>
      </template>
      <div class="editor_body" v-html="blogDetail"></div>
    </el-page-header>
  </div>
  <!-- 新增博客确认弹窗 -->
  <el-dialog title="新增博客" v-model="showAddDialog" width="40%" align-center>
    <el-form
      :model="blogObj"
      ref="confirmFormRef"
      :rules="confirmRules"
      label-width="80px"
      :inline="false"
    >
      <el-form-item label="博客分类" prop="categoryId">
        <el-select
          v-model="blogObj.categoryId"
          placeholder="请选择分类"
          style="width: 140px"
        >
          <el-option
            v-for="item in blogCateGoryList"
            :key="item.categoryId"
            :label="item.categoryName"
            :value="item.categoryId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="封面" prop="cover">
        <el-upload
          ref="upload"
          accept=".png,.jpg,.PNG,.JPG,.jpeg,.JPEG,.gif,.GIF,.bmp,.BMP"
          action="/api/file/uploadImage"
          list-type="picture-card"
          :auto-upload="true"
          :on-success="handleAvatarSuccess"
          :file-list="fileList"
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
      <el-form-item label="博客类型" prop="type"
        ><el-radio-group v-model="blogObj.type" class="ml-4">
          <el-radio :label="0">原创</el-radio>
          <el-radio :label="1">转载</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="转载地址" v-if="blogObj.type" prop="reprintUrl">
        <el-input
          v-model="blogObj.reprintUrl"
          placeholder="请输入转载地址"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item label="评论" prop="allowComment">
        <el-radio-group v-model="blogObj.allowComment" class="ml-4">
          <el-radio :label="1">允许</el-radio>
          <el-radio :label="0">不允许</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="博客摘要" prop="summary">
        <el-input
          v-model="blogObj.summary"
          placeholder="请输入摘要"
          type="textarea"
          :autosize="{ minRows: 3, maxRows: 5 }"
        ></el-input>
      </el-form-item>
      <el-form-item label="博客标签" prop="tag">
        <el-text
          type="info"
          style="margin-right: 5px"
          v-if="!blogObj.tag.length"
          >添加标签更容易被搜索引擎收录</el-text
        >
        <span v-else>
          <el-tag
            v-for="tag in blogObj.tag"
            :key="tag"
            :disable-transitions="false"
            closable
            @close="handleTagClose(tag)"
            style="margin-right: 5px"
            >{{ tag }}</el-tag
          >
        </span>

        <el-button
          v-if="!showTagInput"
          class="button-new-tag ml-1"
          size="small"
          @click="showTagInput = true"
        >
          + 新增标签
        </el-button>
        <input
          v-model="tag"
          @blur="addTag"
          @keyup.enter="addTag"
          class="tag_input"
          v-focus
          v-else
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <span>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmAddBlog">确认</el-button>
      </span>
    </template>
  </el-dialog>
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
.editor {
  .editor_header {
    margin-bottom: 20px;
  }
  .editor_footer {
    text-align: center;
    margin-top: 15px;
  }
}

.tag_input {
  height: 20px;
  width: 80px;
}
.tag_input:focus {
  outline: 0;
  border: 1px solid #409eff;
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

.detail_page {
  height: calc(100vh - 120px);
  overflow: auto;
}
</style>
