<script setup>
import { ref, defineProps, defineEmits } from "vue";
import { uploadImgAPI } from "@/api/blog.js";

const props = defineProps({
  markdownText: {
    type: String,
    default: "",
  },
  htmlText: {
    type: String,
    default: "",
  },
  height: {
    type: Number,
    default: 400,
  },
});

const emit = defineEmits(["update:markdownText", "update:htmlText"]);
const text = ref(props.markdownText);

// 上传图片
const handleUploadImage = async (event, insertImage, files) => {
  // 拿到 files 之后上传到文件服务器，然后向编辑框中插入对应的内容
  console.log(event);
  console.log(insertImage);
  console.log(files[0]);
  // 将图片上传到服务器
  const res = await uploadImgAPI(files[0], 1);
  console.log(res);

  insertImage({
    url: "/api/file/getImage/" + res.fileName,
  });
};
// 文本改变
const change = (markdownText, htmlText) => {
  emit("update:markdownText", markdownText);
  emit("update:htmlText", htmlText);
};
</script>

<template>
  <div>
    <v-md-editor
      v-model="text"
      :height="props.height + 'px'"
      :disabled-menus="[]"
      :include-level="[1, 2, 3, 4, 5, 6]"
      @upload-image="handleUploadImage"
      @change="change"
    ></v-md-editor>
  </div>
</template>

<style scoped></style>
