<script setup>
import { nextTick, onMounted, reactive, ref } from "vue";
import {
  getCategoryListAPI,
  addCategoeyAPI,
  delCategoeyAPI,
  sortCategoryAPI,
} from "@/api/category.js";
import { Plus } from "@element-plus/icons-vue";
import { genFileId } from "element-plus";
import { useUserInfoStore } from "@/store/index.js";

const userInfoStore = useUserInfoStore();
// 获取用户信息
const userInfo = ref(userInfoStore.userInfo);

const categoryList = ref([]);

// 获取博客分类
const getCategoryList = async () => {
  const res = await getCategoryListAPI();
  categoryList.value = res;
  console.log(res);
};

// 新增分类对话框显示
const addCategoryDialogVisible = ref(false);

// 打开新增分类对话框
const addCategory = () => {
  addCategoryDialogVisible.value = true;
};

// 新增分类对象
const addCategoryObj = ref({
  categoryId: "",
  categoryName: "",
  cover: "",
  categoryDesc: "",
});

// 新增分类表单校验对象
const rules = reactive({
  categoryName: [{ required: true, message: "名称不能为空！" }],
  categoryDesc: [{ required: true, message: "简介不能为空！" }],
});

// 上传文件
// upload对象
const upload = ref();
// 获取新增分类表单对象
const addFormRef = ref();
// 默认上传文件列表
const fileList = ref([]);

// 上传回调
const handleAvatarSuccess = (a, b) => {
  if (a.code === 200) {
    addCategoryObj.value.cover = a.data.fileName;
  }
};

// 确保只有一个文件被上传
const handleFileChange = (file, fileList) => {
  if (fileList.length > 1) {
    fileList.shift();
  }
};

// 确认新增/修改分类函数
const confirmAdd = () => {
  //验证表单数据合法性
  addFormRef.value.validate(async (valid, msg) => {
    console.log(addCategoryObj);
    console.log(msg);
    if (valid) {
      await addCategoeyAPI(addCategoryObj.value);
      // 如果不存在categoryId，则为新增
      const message = addCategoryObj.value.categoryId
        ? "修改成功！"
        : "新增成功！";
      ElMessage({
        type: "success",
        message,
      });
    }
    //隐藏弹出框
    addCategoryDialogVisible.value = false;
    // 重新获取列表数据
    getCategoryList();
  });
};

// 取消新增
const cancelAdd = () => {
  addCategoryDialogVisible.value = false;
};

// 修改分类
const updateCategory = (data) => {
  // 展示对话框
  addCategoryDialogVisible.value = true;
  // 给表单对象赋值
  nextTick(() => {
    addCategoryObj.value = { ...data };
  });
  // 添加默认上传封面
  if (data.cover) {
    fileList.value.push({
      name: "cover",
      url: "/api/file/getImage/" + data.cover,
    });
  }
};

// 关闭对话框回调
const closeDialog = () => {
  // 重置表单
  addFormRef.value.resetFields();
  // 重置默认文件列表
  fileList.value = [];
  addCategoryObj.value = {
    categoryName: "",
    categoryDesc: "",
  };
};

// 删除分类
const delCategory = async (data) => {
  const res = await delCategoeyAPI(data);
  ElMessage({
    type: "success",
    message: "删除成功",
  });
  // 重新获取列表数据
  getCategoryList();
};

onMounted(() => {
  getCategoryList();
});

// 上移
const moveUp = async (index) => {
  console.log(index);
  if (index) {
    const tempList = [...categoryList.value];
    let temp = { ...tempList[index] };
    tempList[index] = { ...tempList[index - 1] };
    tempList[index - 1] = { ...temp };
    console.log("交换后", categoryList);
    // 发起请求
    await sortCategoryAPI(JSON.stringify(tempList));
    //刷新数据
    getCategoryList();
  } else {
    ElMessage({
      type: "warning",
      message: "已经到顶啦！",
    });
  }
};

// 下移
const moveDown = async (index) => {
  console.log(index);
  if (index < categoryList.value.length - 1) {
    const tempList = [...categoryList.value];
    let temp = { ...tempList[index] };
    tempList[index] = { ...tempList[index + 1] };
    tempList[index + 1] = { ...temp };
    console.log("交换后", categoryList);
    // 发起请求
    await sortCategoryAPI(JSON.stringify(tempList));
    //刷新数据
    getCategoryList();
  } else {
    ElMessage({
      type: "warning",
      message: "已经到底啦！",
    });
  }
};
</script>

<template>
  <div>
    <div class="header">
      <el-button
        type="danger"
        @click="addCategory"
        v-if="userInfo.roleType == 1"
        >新增分类</el-button
      >
    </div>
    <div class="body">
      <el-table
        :data="categoryList"
        style="width: 100%"
        height="calc(100vh - 152px)"
        class="table"
      >
        <el-table-column label="封面" width="100">
          <template #default="scope">
            <el-image
              style="width: 60px; height: 60px"
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
        <el-table-column prop="categoryName" label="名称" width="200" />
        <el-table-column prop="categoryDesc" label="简介" width="600" />
        <el-table-column prop="blogCount" label="博客数" width="140" />
        <el-table-column label="操作" width="200" class="operations">
          <template #default="scope">
            <span v-if="userInfo.roleType == 1">
              <el-link
                type="primary"
                :underline="false"
                @click="updateCategory(scope.row)"
                >修改</el-link
              >
              <el-popconfirm
                title="确认删除吗？"
                confirm-button-text="确认"
                cancel-button-text="取消"
                @confirm="delCategory(scope.row.categoryId)"
              >
                <template #reference>
                  <el-link type="primary" :underline="false">删除</el-link>
                </template>
              </el-popconfirm>

              <el-link
                type="primary"
                :underline="false"
                @click="moveUp(scope.$index)"
                :disabled="scope.$index === 0"
                >上移</el-link
              >
              <el-link
                type="primary"
                :underline="false"
                @click="moveDown(scope.$index)"
                :disabled="scope.$index === categoryList.length - 1"
                >下移</el-link
              >
            </span>
            <span v-else>---</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 新增分类对话框 -->
    <el-dialog
      v-model="addCategoryDialogVisible"
      title="新增分类"
      width="30%"
      @close="closeDialog"
    >
      <el-form
        :model="addCategoryObj"
        label-width="90px"
        :rules="rules"
        ref="addFormRef"
      >
        <el-form-item label="名称" prop="categoryName">
          <el-input
            v-model="addCategoryObj.categoryName"
            placeholder="请输入名称"
          ></el-input>
        </el-form-item>
        <el-form-item label="封面" prop="cover">
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
            v-model="addCategoryObj.categoryDesc"
            type="textarea"
            rows="4"
          ></el-input>
        </el-form-item>
        <el-form-item prop="categoryId"> </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancelAdd">取消</el-button>
          <el-button type="primary" @click="confirmAdd"> 确定 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.table {
  font-size: 12px;
  .el-link {
    margin-left: 5px;
    font-size: 12px;
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
