import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
const path = require('path')
import { resolve } from 'path'
import styleImport, { VantResolve } from 'vite-plugin-style-import';

export default defineConfig({
  plugins: [
    vue(),
    styleImport({
      resolves: [VantResolve()],
    }),
  ],
  open: true,
  // 是否开启 https
  https: false,
  // 服务端渲染
  ssr: false,
  /**
   * 在生产中服务时的基本公共路径。
   * @default '/'
   */
   base: './',
   /**
    * 与“根”相关的目录，构建输出将放在其中。如果目录存在，它将在构建之前被删除。
    * @default 'dist'
    */
   outDir: 'dist',
   resolve: {
     alias: {
       '@': resolve(__dirname, '.', 'src'),
     },
   },
   // 反向代理
   server: {
     port: 8082,
     // 是否自动在浏览器打开
     open: true,
     // 是否开启 https
     https: false,
     proxy: {
        '/zwz': {
            target: 'http://127.0.0.1:8081',
            ws: true
        },
      },
      disableHostCheck: true
   },
})
