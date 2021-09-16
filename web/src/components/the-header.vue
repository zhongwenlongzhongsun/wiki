<template>
  <a-layout-header class="header">
    <div class="logo"/>
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >

      <a-menu-item key="/">
        <router-link to="/"><HomeOutlined />首页</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/ebook">
        <router-link to="/admin/ebook"><BookOutlined />电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about"><AliwangwangOutlined />关于我们</router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<script lang="ts">
import {defineComponent} from 'vue';
import {AliwangwangOutlined, BookOutlined, HomeOutlined} from '@ant-design/icons-vue';

export default defineComponent({
  name: 'the-header',
  components: {
    AliwangwangOutlined,
    HomeOutlined,
    BookOutlined
  }
});
</script>