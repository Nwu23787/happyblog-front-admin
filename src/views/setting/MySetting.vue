<script setup>
import { onMounted, reactive, ref } from "vue";
import { Plus } from "@element-plus/icons-vue";
import TextEditor from "@/components/TextEditor.vue";
import {
  saveMyInfoAPI,
  getUserInfoAPI,
  updateMyPasswordAPI,
} from "@/api/setting.js";
import {} from "@/api/user.js";
import { useRouter } from "vue-router";
import { useUserInfoStore } from "@/store/index.js";

const userInfo = reactive({
  avatar: "", // 头像
  nickName: "", // 昵称
  phone: "", // 手机号
  profession: "", // 职业
  editorType: 1, // 编辑器类型
  introduction: "", //简介
});

// 上传文件
// upload对象
const upload = ref();
// 默认上传文件列表
const fileList = ref([]);

// 上传成功回调
const handleAvatarSuccess = (a, b) => {
  if (a.code === 200) {
    userInfo.avatar = a.data.fileName;
  }
};

// 确保只有一个文件被上传
const handleFileChange = (file, fileList) => {
  if (fileList.length > 1) {
    fileList.shift();
  }
};

// 校验规则
const rules = ref({
  nickName: [{ required: true, message: "昵称不能为空" }],
  phone: [{ required: true, message: "手机号不能为空" }],
});

const formRef = ref();
const userInfoStore = useUserInfoStore();

const submitUserInfo = () => {
  formRef.value.validate(async (vaild) => {
    if (vaild) {
      // 提交个人信息
      await saveMyInfoAPI(userInfo);
      ElMessage({
        message: "提交成功",
        type: "success",
      });
      // 刷新用户信息
      getUserInfo();
      userInfoStore.updateUsername(userInfo.nickName);
    }
  });
};

// 获取用户信息
const getUserInfo = async () => {
  const res = await getUserInfoAPI();
  userInfo.avatar = res.avatar;
  userInfo.introduction = res.introduction;
  userInfo.nickName = res.nickName;
  userInfo.phone = res.phone;
  userInfo.profession = res.profession;
  if (res.avatar) {
    fileList.value = [];
    fileList.value.push({
      name: "avatar",
      url: "/api/file/getImage/" + res.avatar,
    });
  }
};

onMounted(() => {
  getUserInfo();
});

// 修改密码弹窗
const showPasswordDialog = ref(false);
// 修改密码对象
const passwordForm = reactive({
  password: "",
  confirmPassword: "",
});

// 验证两次密码输入是否一致
const validatePass = (rule, value, callback) => {
  if (value !== passwordForm.password) {
    callback(new Error("两次输入的密码不一致！"));
  } else {
    callback();
  }
};

// 密码表单校验规则
const passwordFormRules = ref({
  password: [{ required: true, message: "请输入新密码！" }],
  confirmPassword: [
    { required: true, message: "请再次输入新密码！" },
    { validator: validatePass, trigger: "blur" },
  ],
});

const passwordFormRef = ref();
const router = useRouter();
const updatePassword = () => {
  passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      // 提交新密码
      await updateMyPasswordAPI(passwordForm.password);
      ElMessage({
        type: "success",
        message: "修改成功！",
      });

      setTimeout(() => {
        router.push("/login");
      }, 1000);
    }
  });
};
</script>

<template>
  <el-row>
    <el-col :span="8">
      <!-- 信息收集表单 -->
      <el-form
        :model="userInfo"
        label-width="80px"
        :rules="rules"
        ref="formRef"
      >
        <el-form-item label="头像">
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
        <el-form-item label="昵称" prop="nickName">
          <el-input
            v-model="userInfo.nickName"
            placeholder="请输入昵称"
          ></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input
            v-model="userInfo.phone"
            placeholder="请输入手机号"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-link
            type="primary"
            :underline="false"
            @click="showPasswordDialog = true"
            >修改密码</el-link
          >
        </el-form-item>
        <el-form-item label="职业">
          <el-input v-model="userInfo.profession" placeholder="请输入职业">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" size="default" @click="submitUserInfo"
            >保存</el-button
          >
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="16">
      <el-form :model="userInfo" label-width="60px">
        <el-form-item label="简介">
          <TextEditor v-model:valueHtml="userInfo.introduction"
        /></el-form-item>
      </el-form>
    </el-col>
  </el-row>

  <!-- 修改密码弹窗 -->
  <el-dialog title="修改密码" v-model="showPasswordDialog" width="30%">
    <el-form
      :model="passwordForm"
      ref="passwordFormRef"
      :rules="passwordFormRules"
      label-width="80px"
    >
      <el-form-item label="新密码" prop="password">
        <el-input
          v-model="passwordForm.password"
          placeholder="请输入新密码"
          type="password"
          show-password
        ></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input
          v-model="passwordForm.confirmPassword"
          placeholder="请再次输入新密码"
          type="password"
          show-password
        ></el-input>
      </el-form-item>
    </el-form>

    <template #footer>
      <span>
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button type="primary" @click="updatePassword">确认</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped lang="scss"></style>
