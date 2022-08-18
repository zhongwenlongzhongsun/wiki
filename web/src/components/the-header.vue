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
      <a-menu-item key="/admin/user">
        <router-link to="/admin/user"><BookOutlined />用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/ebook">
        <router-link to="/admin/ebook"><BookOutlined />电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category">
        <router-link to="/admin/category"><BookOutlined />分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about"><AliwangwangOutlined />关于我们</router-link>
      </a-menu-item>
      <a class="login-menu" @click="showLoginModal">
        <span>登录</span>
      </a>
    </a-menu>

    <a-modal
        title="登录"
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        @ok="login"
    >
      <a-form :model="loginUser" :label-col="{ span: 6}" :wrapper-col="{span: 18}">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.loginName"/>
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import {defineComponent, ref} from 'vue';
import {AliwangwangOutlined, BookOutlined, HomeOutlined} from '@ant-design/icons-vue';

export default defineComponent({
  name: 'the-header',
  setup() {
    const loginUser = ref({
      loginName: "test",
      password: "test"
    });
    const loginModalLoading = ref(false);
    const loginModalVisible = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    //登录
    const login = () => {
      console.log("开始登录");
    };

    return {
      loginModalLoading,
      loginModalVisible,
      showLoginModal,
      login,
      loginUser
    }
  },
  components: {
    AliwangwangOutlined,
    HomeOutlined,
    BookOutlined
  }
});
</script>

<style>
 .login-menu {
   float: right;
   color: white;
 }
</style>