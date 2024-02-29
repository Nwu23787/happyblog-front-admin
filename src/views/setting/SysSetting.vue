<script setup>
import { onMounted, reactive, ref } from "vue";
import {
  saveSysSettingAPI,
  getSysSettingAPI,
  createZipAPI,
} from "@/api/setting.js";

const form = ref({
  openCommentType: 1,
  changyanAppId: "",
  changyanAppKey: "",
  gitPagesType: 1,
  showIcp: 1,
  icpDomain: "",
  icpNo: "",
  policeProvince: "",
  policeNo: "",
});

const proviceList = [
  "京",
  "津",
  "冀",
  "晋",
  "蒙",
  "辽",
  "吉",
  "黑",
  "沪",
  "苏",
  "浙",
  "皖",
  "闽",
  "赣",
  "鲁",
  "豫",
  "鄂",
  "湘",
  "粤",
  "桂",
  "琼",
  "渝",
  "川",
  "黔",
  "滇",
  "藏",
  "陕",
  "甘",
  "宁",
  "青",
  "新",
  "台",
  "港",
  "澳",
];

// 表单规则
const rules = reactive({
  openCommentType: [{ required: true, message: "请选择是否开启评论/留言板" }],
  changyanAppId: [{ required: true, message: "请输入畅言appId" }],
  changyanAppKey: [{ required: true, message: "请选择畅言appKey" }],
  gitPagesType: [{ required: true, message: "请选择是否开启Git Pages" }],
  showIcp: [{ required: true, message: "请选择是否开启公安备案和ICP备案信息" }],
  icpDomain: [{ required: true, message: "请输入ICP备案域名" }],
  icpNo: [{ required: true, message: "请输入ICP备案号" }],
  policeProvince: [{ required: true, message: "请选择公安备案省份" }],
  policeNo: [{ required: true, message: "请输入公安备案号" }],
});
// 获取系统设置
const getSysSetting = async () => {
  const res = await getSysSettingAPI();
  console.log(res);
  form.value = res;
};

onMounted(() => {
  getSysSetting();
});

const formRef = ref();
// 保存表单
const onSubmit = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      await saveSysSettingAPI(form.value);
      ElMessage({
        type: "success",
        message: "保存成功",
      });
      // 刷新页面数据
      getSysSetting();
    }
  });
};

// 下载页面
const downLoadPage = async () => {
  const res = await createZipAPI();
  const t = res + "/" + encodeURI("网页.zip");
  document.location.href = "/api/file/download/" + t;
};
</script>

<template>
  <div class="body">
    <el-form
      :model="form"
      ref="formRef"
      :rules="rules"
      label-width="200px"
      :inline="false"
    >
      <el-form-item label="是否开启评论/留言板" prop="openCommentType">
        <el-radio-group v-model="form.openCommentType" class="ml-4">
          <el-radio :label="1" size="large">是</el-radio>
          <el-radio :label="0" size="large">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="畅言appId" prop="changyanAppId">
        <el-input v-model="form.changyanAppId"></el-input>
      </el-form-item>
      <el-form-item label="畅言appKey" prop="changyanAppKey">
        <el-input v-model="form.changyanAppKey"></el-input>
      </el-form-item>
      <el-form-item label="是否开启 Git Pages" prop="gitPagesType">
        <el-radio-group v-model="form.gitPagesType" class="ml-4">
          <el-radio :label="1" size="large">是</el-radio>
          <el-radio :label="0" size="large">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="开启公安备案和ICP备案信息" prop="showIcp">
        <el-radio-group v-model="form.showIcp" class="ml-4">
          <el-radio :label="1" size="large">是</el-radio>
          <el-radio :label="0" size="large">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <div v-if="form.showIcp">
        <el-form-item label="ICP备案域名" prop="icpDomain">
          <el-input v-model="form.icpDomain"></el-input>
        </el-form-item>
        <el-form-item label="ICP备案号" prop="icpNo">
          <el-input v-model="form.icpNo"></el-input>
        </el-form-item>
        <el-form-item label="公安备案省份" prop="policeProvince">
          <el-select
            v-model="form.policeProvince"
            placeholder="请选择"
            size="large"
            style="width: 200px"
          >
            <el-option
              v-for="item in proviceList"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="公安备案号" prop="policeNo">
          <el-input v-model="form.policeNo"></el-input>
        </el-form-item>
      </div>
      <el-form-item label="下载静态页面">
        <el-link type="primary" :underline="false" @click="downLoadPage"
          >点击下载</el-link
        >
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped lang="scss">
.body {
  font-size: 12px;
  width: 50%;
}
</style>
