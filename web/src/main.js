import { createApp } from 'vue';
import App from './App.vue';
import myRrouter from './router/index'
import store from './store'
import { getRequest, postRequest } from '@/libs/axios'
import { setStore, getStore, removeStore } from '@/libs/storage'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import Video from 'video.js'
import 'video.js/dist/video-js.css'

const app = createApp(App)
app.config.productionTip = false
app.use(myRrouter);
app.use(store);
app.use(ElementPlus);

app.config.globalProperties.getRequest = getRequest;
app.config.globalProperties.postRequest = postRequest;
app.config.globalProperties.setStore = setStore;
app.config.globalProperties.getStore = getStore;
app.config.globalProperties.removeStore = removeStore;
app.config.globalProperties.$video = Video
app.mount('#app')
