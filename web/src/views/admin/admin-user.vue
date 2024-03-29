<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input v-model:value="param['loginName']" placeholder="登录名" >
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()">
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="users"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template v-slot:action="{ text, record}">
          <a-space size="small">
            <a-button type="primary" @click="ResetPassword(record)">
              重置密码
            </a-button>
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="删除后不可恢复，确认删除?"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="primary" danger>
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      title="用户表单"
      v-model:visible="modalVisible"
      :confirm-loading="modalLoading"
      @ok="handleModalOK"
  >
    <a-form :model="user" :label-col="{ span: 6}" :wrapper-col="{span: 18}">
      <a-form-item label="登录名">
        <a-input v-model:value="user['loginName']" :disabled="!!user.id"/>
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="user.name" />
      </a-form-item>
      <a-form-item label="密码" v-show="!user.id" >
        <a-input v-model:value="user['password']"/>
      </a-form-item>
    </a-form>
  </a-modal>

  <a-modal
      title="重置密码"
      v-model:visible="resetModalVisible"
      :confirm-loading="resetModalLoading"
      @ok="handleResetModalOK"
  >
    <a-form :model="user" :label-col="{ span: 6}" :wrapper-col="{span: 18}">
      <a-form-item label="新密码">
        <a-input v-model:value="user['password']"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>


<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import {Tool} from "@/util/tool";

declare let hexMd5: any;
declare let KEY: any;


export default defineComponent({
  name: 'AdminUser',
  //Vue3 新增的初始化方法
  setup: function () {
    const param = ref();
    param.value = {};
    const users = ref();//响应式数据
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    });
    const loading = ref(false);

    const columns = [
      {
        title: '登陆名',
        dataIndex: 'loginName'
      },
      {
        title: '名称',
        dataIndex: 'name'
      },
      {
        title: '密码',
        dataIndex: 'password'
      },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    /**
     * 数据查询
     */
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/user/list", {
        params: {
          page: params.page,
          size: params.size,
          loginName: param.value.loginName
        }
      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          users.value = data.content.list;

          //重置分页按钮
          pagination.value.current = params.page;
          pagination.value.total = data.content.total;
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 表格点击页码时触发
     */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数都有啥：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      });
    };

    // --表单--
    /**
     * 数组 [100,101] 对应前端开发 / Vue
     */
    const user = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOK = () => {
      modalLoading.value = true;
      user.value.password = hexMd5(user.value.password + KEY);//加密
      axios.post("/user/save", user.value).then((response) => {
        modalLoading.value = false;
        const data = response.data;  // data = commonResp
        if (data.success) {
          modalVisible.value = false;

          //重新加载列表
          handleQuery({
            page: pagination.value.current,      //保存完重新查询当前页
            size: pagination.value.pageSize,
          }); // 只在方法内部调用不用return
        } else {
          message.error(data.message);
        }
      });
    };

    //编辑
    const edit = (record: any) => {
      modalVisible.value = true;
      user.value = Tool.copy(record);
    }

    //新增
    const add = () => {
      modalVisible.value = true;
      user.value = {};
    }

    //删除(long类型对应的前端类型是number)
    const handleDelete = (id: number) => {
      axios.delete("/user/delete/" + id).then((response) => {
        const data = response.data;  // data = commonResp
        if (data.success) {
          //重新加载列表
          handleQuery({
            page: pagination.value.current,      //保存完重新查询当前页
            size: pagination.value.pageSize,
          }); // 只在方法内部调用不用return
        }
      });
    };

    // 重置密码

    const resetModalVisible = ref(false);
    const resetModalLoading = ref(false);
    const handleResetModalOK = () => {
      resetModalLoading.value = true;

      user.value.password = hexMd5(user.value.password + KEY);

      axios.post("/user/reset-password", user.value).then((response) => {
        resetModalLoading.value = false;
        const data = response.data;  // data = commonResp
        if (data.success) {
          resetModalVisible.value = false;

          //重新加载列表
          handleQuery({
            page: pagination.value.current,      //保存完重新查询当前页
            size: pagination.value.pageSize,
          }); // 只在方法内部调用不用return
        } else {
          message.error(data.message);
        }
      });
    };

    //重置密码
    const ResetPassword = (record: any) => {
      resetModalVisible.value = true;
      user.value = Tool.copy(record);
      user.value.password = null;
    }

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      });
    });


    return {
      param,
      users,
      pagination,
      columns,
      loading,


      handleTableChange,
      handleQuery,


      edit,
      add,

      user,
      modalVisible,
      modalLoading,
      handleModalOK,
      handleDelete,
      resetModalVisible,
      resetModalLoading,
      handleResetModalOK,
      ResetPassword


    }
  }
});
</script>
<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>