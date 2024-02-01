import {createRouter, createWebHashHistory} from "vue-router"

const routes = [
	{
		path: '/position',
		name: 'position',
		component: () => import('../views/position.vue')
	},
	{
		path: '/position2',
		name: 'position2',
		component: () => import('../views/position2.vue')
	},
	{
		path: '/positionItem',
		name: 'positionItem',
		component: () => import('../views/positionItem.vue')
	},
	{
		path: '/positionBuy',
		name: 'positionBuy',
		component: () => import('../views/positionBuy.vue')
	},
	{
		path: '/news',
		name: 'news',
		component: () => import('../views/news.vue')
	},
	{
		path: '/message',
		name: 'message',
		component: () => import('../views/message.vue')
	},
	{
		path: '/myOwn',
		name: 'myOwn',
		component: () => import('../views/myOwn.vue')
	},
	{
		path: '/myOrder',
		name: 'myOrder',
		component: () => import('../views/myOrder.vue')
	},
	{
		path: '/myOrderItem',
		name: 'myOrderItem',
		component: () => import('../views/myOrderItem.vue')
	},
	{
		path: '/myLookItem',
		name: 'myLookItem',
		component: () => import('../views/myLookItem.vue')
	},
	{
		path: '/',
		name: 'Home',
		component: () => import('../views/home.vue')
	},
]

const router = createRouter({
	// 4. 采用hash 模式
	history: createWebHashHistory(),
	// 采用 history 模式
	// history: createWebHistory(),
	routes, // short for `routes: routes`
});

export default router