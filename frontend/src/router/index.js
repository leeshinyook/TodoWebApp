import Vue from 'vue';
import Router from 'vue-router';
import TodoBoard from '@/components/TodoBoard';
Vue.use(Router);

export default new Router({
	mode: 'history',
	routes: [
		{
			path: '/',
			name: 'TodoBoard',
			component: TodoBoard
		}
	]
});
