import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import Home from '../views/home.vue'
import About from '../views/about.vue'
import AdminUser from '../views/admin/admin-user.vue'
import AdminEbook from '../views/admin/admin-ebook.vue'
import AdminCategory from '../views/admin/admin-category.vue'
import AdminDoc from '../views/admin/admin-doc.vue'
import Doc from '../views/doc.vue'
import {Tool} from "@/util/tool";
import store from "@/store";


const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/doc',
    name: 'Doc',
    component: Doc
  },
  {
    path: '/about',
    name: 'About',
    component: About
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    // component: () => import(/* webpackChunkName: "about" */ '../views/about.vue')
  },
  {
    path: '/admin/user',
    name: 'AdminUser',
    component: AdminUser,
    meta:{ //自定义元数据
      loginRequire: true //需要登录
    }
  },
  {
    path: '/admin/ebook',
    name: 'AdminEbook',
    component: AdminEbook,
    meta:{
      loginRequire: true
    }
  },
  {
    path: '/admin/category',
    name: 'AdminCategory',
    component: AdminCategory,
    meta:{
      loginRequire: true
    }
  },
  {
    path: '/admin/doc',
    name: 'AdminDoc',
    component: AdminDoc,
    meta:{
      loginRequire: true
    }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

//路由登录拦截
//to:即将要进入的目标路由对象；from:当前导航即将要离开的路由对象；next ：调用该方法后，才能进入下一个afterEach函数
//afterEach不接收next
router.beforeEach((to,from,next)=>{
  //是否对meta.LoginRequire属性做监控拦截
  if (to.matched.some(function(item){
    console.log(item, "是否需要登录校验：", item.meta.loginRequire);
    return item.meta.loginRequire
  })){
    const loginUser = store.state.user;
    if (Tool.isEmpty(loginUser)){
      console.log("用户未登录！");
      next('/');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router
