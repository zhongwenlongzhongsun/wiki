<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-row :gutter="24"><!--使用 (16+8n)px 作为栅格间隔(n 是自然数)。-->
        <a-col :span="10">
          <p>
            <a-form layout="inline" :model="param">
<!--              <a-form-item>-->
<!--                <a-button type="primary" @click="handleQuery()">-->
<!--                  查询-->
<!--                </a-button>-->
<!--              </a-form-item>-->
              <a-form-item>
                <a-button type="primary" @click="add()">
                  新增
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <p>
          </p>
          <!-- defaultExpandAllRows 默认展开左边列表， v-if 只有等到level1展示完所有数据再加载，a-table-->
          <a-table
              v-if="level1.length > 0"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="level1"
              :loading="loading"
              :pagination="false"
              :defaultExpandAllRows="true"
              size="small"
          >
            <template #name="{ text , record }">
              {{record.sort}} {{text}} <!-- 渲染【顺序， 电子书名字】 -->
            </template>
            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>
                <a-popconfirm
                    title="删除后不可恢复，确认删除?"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button type="primary" size="small" danger>
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="14">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical">

            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>

            <a-form-item>
              <a-tree-select
                  v-model:value="doc['parent']"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="请选择父文档"
                  tree-default-expand-all
                  :replaceFields="{title: 'name', key: 'id', value: 'id'}"

              >
              </a-tree-select>
            </a-form-item>

            <a-form-item>
              <a-input v-model:value="doc['sort']" placeholder="顺序"/>
            </a-form-item>

            <a-form-item>
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeOutlined /> 内容预览
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

      <!--抽屉组件 -->
      <a-drawer width="900" placement="right" :closable="false" :visible="drawerVisible" @close="onDrawerClose">
        <div class="wangeditor" :innerHTML="previewHtml"></div>
      </a-drawer>

    </a-layout-content>
  </a-layout>

<!--  <a-modal-->
<!--      title="文档表单"-->
<!--      v-model:visible="modalVisible"-->
<!--      :confirm-loading="modalLoading"-->
<!--      @ok="handleSave"-->
<!--  >-->
<!--    -->
<!--  </a-modal>-->
</template>

<script lang="ts">
import {defineComponent, onMounted, ref, createVNode} from 'vue';
import axios from 'axios';
import {message, Modal} from 'ant-design-vue';
import {Tool} from "@/util/tool";
import {useRoute} from "vue-router";
import ExclamationCircleOutlined from "@ant-design/icons-vue/ExclamationCircleOutlined";
import E from 'wangeditor';

export default defineComponent({
  name: 'AdminDoc',

  setup() {

    const route = useRoute();
    console.log("路由: ", route);
    console.log("route.path: ", route.path);
    console.log("route.query: ", route.query);
    console.log("route.param: ", route.params);
    console.log("route.fullPath: ", route.fullPath); //包括path+query参数
    console.log("route.name: ", route.name);
    console.log("route.meta: ", route.meta);
    const param = ref();
    param.value = {};
    const docs = ref();
    const loading = ref(false);
    //因为树选择组件的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
    const treeSelectData = ref();
    treeSelectData.value = [];

    const columns = [
      {
        title: '名称',
        dataIndex: 'name',
        slots: {customRender: 'name'}
      },
      // {
      //   title: '父文档',
      //   key: 'parent',
      //   dataIndex: 'parent'
      // },
      // {
      //   title: '顺序',
      //   dataIndex: 'sort'
      // },
      {
        title: 'Action',
        key: 'action',
        slots: { customRender: 'action' }
      }
    ];

    /**
     * 一级文档树，children属性就是二级文档
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const level1 = ref(); // 一级文档树，children属性就是二级文档
    level1.value = []; //初始化level1，因为【v-if="level1.length > 0"】里初始level为空，会报错

    /**
     * 数据查询
     **/
    const handleQuery = () => {
      loading.value = true;
      // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
      level1.value = [];
      //从路径里查出ebookId
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          console.log("原始数组：", docs.value);

          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          console.log("树形结构：", level1);

          //父文档下拉框初始化，相当于点击新增
          treeSelectData.value = Tool.copy(level1.value);
          //为选择树添加一个 无  //unshift: 在数组前面添加元素
          treeSelectData.value.unshift({id: 0, name:'无'});
        } else {
          message.error(data.message);
        }
      });
    };

    // -------- 表单 ---------

    const doc = ref();
    doc.value = {
      ebookId: route.query.ebookId
    };
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleSave = () => {
      modalLoading.value = true;
      editor.txt.html();
      doc.value.content = editor.txt.html();
      axios.post("/doc/save", doc.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          // modalVisible.value = false;
          message.success("保存成功")
          // 重新加载列表
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 将某节点及其子孙节点全部置为disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("disabled", node);
          // 将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)      //在children数组里找到对应的id，递归调用
            }
          }
        } else {
          // 如果当前节点不是目标节点，则再找到其子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    // const ids: Array<string> = [];
    const deleteIds: Array<string> = [];
    const deleteNames: Array<string> = [];
    /**
     * 查找整个树枝
     */
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("delete", node);
          // 将目标id放入结果集ids
          // ids.push(node.id); //id也行
          deleteIds.push(id);
          deleteNames.push(node.name);

          // 遍历所有子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id)      //在children数组里找到对应的id，递归调用
            }
          }
        } else {
          // 如果当前节点不是目标节点，则再找到其子节点
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };

    /**
     * 内容查询
     **/
    const handleQueryContent = () => {
      axios.get("/doc/find-content/" + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          editor.txt.html(data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      //清空富文本框
      editor.txt.html("");
      modalVisible.value = true;
      doc.value = Tool.copy(record); //这里才开始有值
      handleQueryContent();


      // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
      treeSelectData.value = Tool.copy(level1.value);
      setDisable(treeSelectData.value, record.id);

      // 为选择树添加一个"无"   //unshift: 在数组前面添加元素
      treeSelectData.value.unshift({id: 0, name: '无'});
      // setTimeout(function () {
      //   if (editor == null) {
      //     createEditor();
      //   }else {
      //     editor.destroy();//这里做了一次判断，判断编辑器是否被创建，如果创建了就先销毁。
      //     createEditor();
      //   }
      // },100);
    };

    // 创建编辑器
    let editor:any;

    const createEditor = () => {
      editor = new E('#content');
      editor.config.zIndex = 0; //防止富文本显示在顶层
      editor.create();
    }

    /**
     * 新增
     */
    const add = () => {
      //清空富文本框
      editor.txt.html("");
      modalVisible.value = true;
      doc.value = {
        ebookId: route.query.ebookId
      };

      treeSelectData.value = Tool.copy(level1.value);

      // 为选择树添加一个"无"   //unshift: 在数组前面添加元素
      treeSelectData.value.unshift({id: 0, name: '无'});
      // setTimeout(function () {
      //   if (editor == null) {
      //     createEditor();
      //   }else {
      //     editor.destroy();//这里做了一次判断，判断编辑器是否被创建，如果创建了就先销毁。
      //     createEditor();
      //   }
      // })
    };

    const handleDelete = (id: number) => {
      // 清空数组，否则多次删除时，数组会一直增加
      deleteIds.length = 0;
      deleteNames.length = 0;
      // console.log(level1.value, id);
      getDeleteIds(level1.value, id);//整棵树的数据， 查找这个目标id的整个树枝
      // // console.log(ids);
      // //将ids变成字符串
      // axios.delete("/doc/delete/" + ids.join(",")).then((response) => {
      //   const data = response.data; // data = commonResp
      //   if (data.success) {
      //     // 重新加载列表
      //     handleQuery();
      //   } else {
      //     message.error(data.message);
      //   }
        Modal.confirm({
          title: '重要提醒',
          icon: createVNode(ExclamationCircleOutlined),
          content: '将删除：【' + deleteNames.join(",") + "】删除后不可恢复，确认删除？",
          onOk() {
            // console.log(ids)
            axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
            const data = response.data; // data = commonResp
                if (data.success)
                // 重新加载列表
                handleQuery();
            });
        },
      });
    };

    // 富文本预览
    const drawerVisible = ref(false);
    const previewHtml = ref();

    const handlePreviewContent = () => {
      const html = editor.txt.html();
      previewHtml.value = html;
      drawerVisible.value = true;
    }

    const onDrawerClose = () => {
      drawerVisible.value = false;
    }


    onMounted(() => {
      createEditor();
      handleQuery();
    });

    return {
      param,
      // docs,
      level1,
      columns,
      loading,
      handleQuery,
      handleQueryContent,

      edit,
      add,

      doc,
      modalLoading,
      modalVisible,
      handleSave,
      treeSelectData,
      previewHtml,
      drawerVisible,
      handlePreviewContent,
      onDrawerClose,
      handleDelete,
      setDisable,
      getDeleteIds
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
