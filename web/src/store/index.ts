import {createStore} from 'vuex'

declare let SessionStorage: any;
const USER = "USER";

const store = createStore ({
  state: {
    //避免空对象，刷新时加载store，在缓存里获取
    user: SessionStorage.get(USER) || {}
  },
  mutations: { //commit()触发
    //同步 state全局变量，user外部变量
    setUser(state , user){
      state.user = user;
      //登录成功后，赋值到state中并加入到缓存中，刷新时再从user中取出来
      SessionStorage.set(USER, user);
    }
  },
  actions: {
  },
  modules: {
  }
})

export default store;