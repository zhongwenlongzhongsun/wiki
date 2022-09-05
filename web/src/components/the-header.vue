<template>
  <a-layout-header class="header">
    <div class="logo"/>

      <a-popconfirm
          title="确认退出登录?"
          ok-text="是"
          cancel-text="否"
          @confirm="logout()"
      >
        <a class="login-menu" v-show="user.id">
          <span>退出登录</span>
        </a>
      </a-popconfirm>
      <a class="login-menu"  v-show="user.id">
        <span>欢迎：{{ user.name }}</span>
      </a>
      <a class="login-menu" @click="showLoginModal" v-show="!user.id">
        <span>登录</span>
      </a>

    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/">
        <router-link to="/"><HomeOutlined />首页</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user">
        <router-link to="/admin/user" :style="user.id? {} : {display:'none'}"><BookOutlined />用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/ebook">
        <router-link to="/admin/ebook" :style="user.id? {} : {display:'none'}"><BookOutlined />电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category" >
        <router-link to="/admin/category" :style="user.id? {} : {display:'none'}"><BookOutlined />分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about"><AliwangwangOutlined />关于我们</router-link>
      </a-menu-item>

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
import {computed, defineComponent, ref} from 'vue';
import {AliwangwangOutlined, BookOutlined, HomeOutlined} from '@ant-design/icons-vue';
import axios from "axios";
import {message} from "ant-design-vue";
import store from "@/store";
import router from "@/router";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
  setup() {

    //登录后保存
    const user = computed(() => store.state.user);
    // const user = ref();
    // user.value = {};

    //登录
    const loginUser = ref({
      loginName: "test",
      password: "123"
    });

    const loginModalLoading = ref(false);
    const loginModalVisible = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    //登录
    const login = () => {
      console.log("开始登录");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);//加密
      axios.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;  // data = commonResp
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功！")
          // user.value = data.content;
          store.commit("setUser", data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    // 退出登录
    const logout = () => {
      console.log("退出登录开始");

      axios.get('/user/logout/' + user.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("退出登录成功！");
          store.commit("setUser", {});
          router.push("/");//退出登录重定向到首页
        } else {
          message.error(data.message);
        }
      });
    };

    return {
      loginModalLoading,
      loginModalVisible,
      showLoginModal,
      login,
      loginUser,
      user,
      logout
    };
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
   float: right !important;
   color: white;
    padding-left: 20px;
 }
</style>