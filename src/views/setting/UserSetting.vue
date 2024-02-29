<script setup>
import { nextTick, onMounted, reactive, ref, watch } from "vue";
import {
  getUserListAPI,
  saveTeamUserAPI,
  updateStatusAPI,
  updatePasswordAPI,
  delUserAPI,
} from "@/api/setting.js";
import { genFileId } from "element-plus";
import { Plus } from "@element-plus/icons-vue";
import hljs from "highlight.js";
import "highlight.js/styles/atom-one-light.css";
import TextEditor from "@/components/TextEditor.vue";

// 博客列表数据
const tableData = ref([]);

// 总博客条数
const totalBlogCount = ref(0);
// 分页信息
const page = ref({
  pageNo: 1,
  pageSize: 10,
});

// 成员筛选
const userFilter = reactive({
  phoneFuzzy: "",
  nickNameFuzzy: "",
});

// 获取用户列表
const getUserList = async () => {
  const res = await getUserListAPI({ ...page.value, ...userFilter });
  tableData.value = res.list;
  totalBlogCount.value = res.totalCount;
  console.log(res);
};

onMounted(() => {
  getUserList();
});

// 搜索成员
const searchUser = () => {
  getUserList();
};

const showAddDialog = ref(false);
// 添加或修改表单对象
let addObj = reactive({
  nickName: "",
  phone: "",
  password: "",
  confirmPassword: "",
  content: "",
  editorType: 1,
  profession: "",
  avatar: "",
  introduction: "",
});

// 上传文件
// upload对象
const upload = ref();
// 默认上传文件列表
const fileList = ref([]);

// 上传成功回调
const handleAvatarSuccess = (a, b) => {
  if (a.code === 200) {
    addObj.avatar = a.data.fileName;
  }
};

// 确保只有一个文件被上传
const handleFileChange = (file, fileList) => {
  if (fileList.length > 1) {
    fileList.shift();
  }
};

// 验证两次密码输入是否一致
const validatePass = (rule, value, callback) => {
  if (value !== addObj.password) {
    callback(new Error("两次输入的密码不一致！"));
  } else {
    callback();
  }
};
// 新增用户表单校验规则
const addRules = reactive({
  nickName: [{ required: true, message: "请填写昵称！" }],
  phone: [{ required: true, message: "请填写手机号！" }],
  password: [{ required: true, message: "请填写密码！" }],
  confirmPassword: [
    { required: true, message: "请确认密码！" },
    { validator: validatePass, trigger: "blur" },
  ],
});

// 修改用户表单校验规则
const updateRules = reactive({
  nickName: [{ required: true, message: "请填写昵称！" }],
  phone: [{ required: true, message: "请填写手机号！" }],
  confirmPassword: [{ validator: validatePass, trigger: "blur" }],
});

const addFormRef = ref();
//确认添加
const confirmAddUser = () => {
  addFormRef.value.validate(async (valid) => {
    if (valid) {
      // 添加用户
      await saveTeamUserAPI(addObj);
      // 重置表单对象
      addFormRef.value.resetFields();
      // 关闭对话框
      showAddDialog.value = false;
      ElMessage({
        type: "success",
        message: addObj.userId ? "修改成功！" : "添加成功！",
      });
      // 刷新数据
      getUserList();
    }
  });
};

// 修改用户
const handleEdit = (obj) => {
  addObj = reactive(obj);
  if (obj.avatar) {
    fileList.value.push({
      name: "avatar",
      url: "/api/file/getImage/" + obj.avatar,
    });
  }
  showAddDialog.value = true;
};

const closeDialog = () => {
  fileList.value = [];
  addFormRef.value.resetFields();
  addObj = reactive({
    nickName: "",
    phone: "",
    password: "",
    confirmPassword: "",
    content: "",
    editorType: 1,
    profession: "",
    avatar: "",
    introduction: "",
  });
};

// 打开新增页面
const openAddDialog = () => {
  addObj = reactive({
    nickName: "",
    phone: "",
    password: "",
    confirmPassword: "",
    content: "",
    editorType: 1,
    profession: "",
    avatar: "",
    introduction: "",
  });
  console.log(addFormRef);
  showAddDialog.value = true;
};

// 禁用用户
const forbidUser = async (obj) => {
  console.log(obj);
  await updateStatusAPI(obj.userId, obj.status ? 0 : 1);
  getUserList();
};

// 修改密码对象
const updateObj = reactive({
  userId: "",
  password: "",
  confirmPassword: "",
});

// 修改密码对话框控制
const showUpdatePassword = ref(false);

// 打开修改密码对话框
const handleUpdatePassword = (userId) => {
  updateObj.userId = userId;
  showUpdatePassword.value = true;
};

const validatePass2 = (rule, value, callback) => {
  if (value !== updateObj.password) {
    callback(new Error("两次输入的密码不一致！"));
  } else {
    callback();
  }
};

// 修改密码校验规则
const updatePasswordRules = reactive({
  password: [{ required: true, message: "请填写密码！" }],
  confirmPassword: [
    { required: true, message: "请确认密码！" },
    { validator: validatePass2, trigger: "blur" },
  ],
});

const updateFormRef = ref();

// 确定修改密码
const updatePassword = () => {
  updateFormRef.value.validate(async (valid) => {
    if (valid) {
      await updatePasswordAPI(updateObj.userId, updateObj.password);
      ElMessage({
        type: "success",
        message: "修改成功",
      });
      // 关闭对话框
      showUpdatePassword.value = false;
      //清空对象
      for (let i in updateObj) {
        updateObj[i] = "";
      }
    }
  });
};

// 删除用户
const deleteUser = async (userId) => {
  await delUserAPI(userId);
  ElMessage({
    type: "success",
    message: "删除成功",
  });
  // 刷新数据
  getUserList();
};
</script>

<template>
  <div>
    <el-form :model="userFilter" :inline="true">
      <el-form-item label="昵称">
        <el-input
          v-model="userFilter.nickNameFuzzy"
          placeholder="支持模糊搜索"
        ></el-input>
      </el-form-item>
      <el-form-item label="手机号">
        <el-input
          v-model="userFilter.phoneFuzzy"
          placeholder="支持模糊搜索"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="danger" @click="searchUser">搜索</el-button>
      </el-form-item>
    </el-form>
    <el-button type="danger" size="default" @click="openAddDialog"
      >新增账号</el-button
    >

    <el-table :data="tableData" style="width: 100%" class="table">
      <el-table-column label="头像" width="100">
        <template #default="scope">
          <el-image
            style="width: 60px; height: 60px"
            :src="'/api/file/getImage/' + scope.row.avatar"
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
      <el-table-column label="用户信息" width="300">
        <template #default="scope">
          <div class="title">昵称：{{ scope.row.nickName }}</div>
          <div class="categoryName">手机号：{{ scope.row.phone }}</div>
          <div class="nickname">职业：{{ scope.row.profession }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="editorTypeName" label="编辑器" width="130" />
      <el-table-column label="角色" width="120" prop="roleTypeName">
      </el-table-column>
      <el-table-column label="状态" width="120">
        <template #default="scope">
          <span
            :class="{
              status_star: scope.row.statusName === '启用',
              status_forbid: scope.row.statusName !== '启用',
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
            最后登录时间：{{ scope.row.lastLoginTime }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-link
            type="primary"
            :underline="false"
            @click="handleEdit(scope.row)"
            >修改
          </el-link>
          <el-popconfirm
            :title="scope.row.status ? '确认禁用吗？' : '确认启用吗'"
            confirm-button-text="确认"
            cancel-button-text="取消"
            :hide-after="0"
            @confirm="forbidUser(scope.row)"
            ><template #reference>
              <el-link type="primary" :underline="false"
                >{{ scope.row.status ? "禁用" : "启用" }}
              </el-link>
            </template>
          </el-popconfirm>
          <el-link
            type="primary"
            :underline="false"
            @click="handleUpdatePassword(scope.row.userId)"
            >修改密码
          </el-link>
          <el-popconfirm
            title="确认删除吗？"
            @confirm="deleteUser(scope.row.userId)"
            confirm-button-text="确认"
            cancel-button-text="取消"
          >
            <template #reference>
              <el-link type="primary" :underline="false">删除 </el-link>
            </template>
          </el-popconfirm>
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
      @size-change="getUserList"
      @current-change="getUserList"
      :pager-count="6"
    />
  </div>

  <!-- 新增用户对话框 -->
  <el-dialog
    title="新增用户"
    v-model="showAddDialog"
    width="60%"
    top="2vh"
    @close="closeDialog"
  >
    <el-form
      :model="addObj"
      ref="addFormRef"
      :rules="addObj.userId ? updateRules : addRules"
      label-width="80px"
      :inline="true"
    >
      <el-form-item label="昵称" prop="nickName">
        <el-input v-model="addObj.nickName" placeholder="请输入昵称"></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="addObj.phone" placeholder="请输入手机号"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password" v-if="!addObj.userId">
        <el-input
          v-model="addObj.password"
          placeholder="请输入密码"
          type="password"
          showPassword
        ></el-input>
      </el-form-item>
      <el-form-item
        label="确认密码"
        prop="confirmPassword"
        v-if="!addObj.userId"
      >
        <el-input
          v-model="addObj.confirmPassword"
          placeholder="请再次输入密码"
          type="password"
          showPassword
        ></el-input>
      </el-form-item>
      <el-form-item label="职业" prop="profession">
        <el-input
          v-model="addObj.profession"
          placeholder="请再次输入职业"
        ></el-input>
      </el-form-item>
      <el-form-item label="头像" style="width: 100%" prop="avatar">
        <el-upload
          ref="upload"
          accept=".png,.jpg,.PNG,.JPG,.jpeg,.JPEG,.gif,.GIF,.bmp,.BMP"
          action="/api/file/uploadImage"
          list-type="picture-card"
          :on-change="handleFileChange"
          :auto-upload="true"
          :on-success="handleAvatarSuccess"
          :file-list="fileList"
        >
          <el-icon><Plus /></el-icon>
          <template #file="{ file }">
            <div>
              <img class="el-upload-list__item-thumbnail" :src="file.url" />
            </div>
          </template>
        </el-upload>
      </el-form-item>
      <el-form-item label="简介" prop="introduction">
        <TextEditor v-model:valueHtml="addObj.introduction" :height="150"
      /></el-form-item>
    </el-form>
    <template #footer>
      <span>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmAddUser">确认</el-button>
      </span>
    </template>
  </el-dialog>

  <!-- 修改密码对话框 -->
  <el-dialog title="修改密码" v-model="showUpdatePassword" width="30%">
    <el-form
      :model="updateObj"
      ref="updateFormRef"
      :rules="updatePasswordRules"
      label-width="80px"
      :inline="false"
      size="normal"
    >
      <el-form-item label="新密码" prop="password">
        <el-input
          v-model="updateObj.password"
          type="password"
          show-password
        ></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input
          v-model="updateObj.confirmPassword"
          type="password"
          show-password
        ></el-input>
      </el-form-item>
    </el-form>

    <template #footer>
      <span>
        <el-button @click="showUpdatePassword = false">取消</el-button>
        <el-button type="primary" @click="updatePassword">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style lang="scss" scoped>
.table {
  font-size: 12px;
  height: calc(100vh - 250px);
  margin-bottom: 15px;
  .status_star {
    color: green;
  }
  .status_forbid {
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
