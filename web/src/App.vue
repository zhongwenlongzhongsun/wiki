<template>
  <a-layout>
    <the-header></the-header>
      <router-view/>
    <div id="components-back-top-demo-custom" >
      <a-back-top :visibilityHeight="15">
        <div class="ant-back-top-inner">Up</div>
      </a-back-top>
    </div>
    <the-footer></the-footer>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent } from 'vue';
import TheHeader from '@/components/the-header.vue';
import TheFooter from '@/components/the-footer.vue';

export default defineComponent({
  name: 'app',
  components: {
    TheHeader,
    TheFooter,
  },
  // methods: {
  //   fn() {
  //     return document.getElementById('top')
  //   },
  // }
});
</script>

<style>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
#components-back-top-demo-custom .ant-back-top {
  bottom: 50px;
}
#components-back-top-demo-custom .ant-back-top-inner {
  height: 40px;
  width: 40px;
  line-height: 40px;
  border-radius: 4px;
  background-color: #1088e9;
  color: #fff;
  text-align: center;
  font-size: 20px;
}
</style>
