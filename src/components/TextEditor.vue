<script setup>
import "@wangeditor/editor/dist/css/style.css"; // 引入 css
import {
  onBeforeUnmount,
  ref,
  shallowRef,
  onMounted,
  defineProps,
  defineEmits,
  watch,
} from "vue";
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";

const props = defineProps({
  valueHtml: {
    type: String,
    default: "",
  },
  height: {
    type: Number,
    default: 300,
  },
});

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef();

// 内容 HTML
const html = ref(props.valueHtml);

const toolbarConfig = {};
const editorConfig = {
  placeholder: "请输入内容...",
  MENU_CONF: {
    uploadImage: {
      maxFileSize: 3 * 1024 * 1024,
      server: "/api/file/uploadImage4WangEditor",
      customInsert(res, insertFn) {
        insertFn(res.data.url, "", "");
      },
    },
  },
};

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor == null) return;
  editor.destroy();
});

const handleCreated = (editor) => {
  editorRef.value = editor; // 记录 editor 实例，重要！
};

const mode = "default";

const emit = defineEmits(["update:valueHtml"]);

// 内容变化传值
const valueChange = (editor) => {
  emit("update:valueHtml", editor.getHtml());
};

watch(
  () => props.valueHtml,
  (newValue, oldValue) => {
    html.value = newValue;
  }
);

const heightStyle = `height: ${props.height}px; overflow-y: hidden`;
</script>

<template>
  <div style="border: 1px solid #ccc">
    <Toolbar
      style="border-bottom: 1px solid #ccc"
      :editor="editorRef"
      :defaultConfig="toolbarConfig"
      :mode="mode"
    />
    <Editor
      :style="heightStyle"
      v-model="html"
      :defaultConfig="editorConfig"
      :mode="mode"
      @onCreated="handleCreated"
      @onChange="valueChange"
    />
  </div>
</template>

<style scoped></style>
