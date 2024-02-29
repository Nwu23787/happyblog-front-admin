<script setup>
import TextEditor from "@/components/TextEditor.vue";
import { ref } from "vue";
const text = ref("1");
</script>

<template>
  <div>
    <TextEditor v-model:valueHtml="text"></TextEditor>
  </div>
  {{ text }}
</template>

<style scoped></style>
