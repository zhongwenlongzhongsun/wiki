<template>
  <a-layout-footer style="text-align: center">
    Ant Design Vue ©2022 Created by ZhongSun
    <span v-show="user.id">，欢迎：{{user.name}}</span>
  </a-layout-footer>
</template>

<script lang="ts">
import {defineComponent, computed } from 'vue';
import store from "@/store";

export default defineComponent({
    name: 'the-footer',
    setup(){
      const user = computed(() => store.state.user);

      return {
        user
      }
    }
});
</script>