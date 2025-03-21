import { defineStore } from 'pinia';

import { usePermissionStore } from '@/store';
import type { UserInfo } from '@/types/interface';
import { loginPwd } from '@/api/login';

const InitUserInfo: UserInfo = {
  name: '', // 用户名，用于展示在页面右上角头像处
  roles: [], // 前端权限模型使用 如果使用请配置modules/permission-fe.ts使用
};

export const useUserStore = defineStore('user', {
  state: () => ({
    token: 'main_token', // 默认token不走权限
    tokenHead: 'Authorization', //请求头
    userInfo: { ...InitUserInfo },
  }),
  getters: {
    roles: (state) => {
      return state.userInfo?.roles;
    },
  },
  actions: {
    async login(userInfo: Record<string, unknown>) {
      const { token, tokenHead } = await loginPwd(userInfo);
      console.log(token, tokenHead);
      this.token = token;
      this.tokenHead = tokenHead;
      // const mockLogin = async (userInfo: Record<string, unknown>) => {
      //   // 登录请求流程
      //   console.log(`用户信息:`, userInfo);
      //   // const { account, password } = userInfo;
      //   // if (account !== 'td') {
      //   //   return {
      //   //     code: 401,
      //   //     message: '账号不存在',
      //   //   };
      //   // }
      //   // if (['main_', 'dev_'].indexOf(password) === -1) {
      //   //   return {
      //   //     code: 401,
      //   //     message: '密码错误',
      //   //   };
      //   // }
      //   // const token = {
      //   //   main_: 'main_token',
      //   //   dev_: 'dev_token',
      //   // }[password];
      //   return {
      //     code: 200,
      //     message: '登录成功',
      //     data: 'main_token',
      //   };
      // };

      // const res = await mockLogin(userInfo);
      // if (res.code === 200) {
      //   this.token = res.data;
      // } else {
      //   throw res;
      // }
    },
    async getUserInfo() {
      const mockRemoteUserInfo = async (token: string) => {
        //console.log('Token for user info:', token);
        if (token === 'main_token') {
          return {
            name: 'Tencent',
            roles: ['all'], // 前端权限模型使用 如果使用请配置modules/permission-fe.ts使用
          };
        }
        return {
          name: 'lwlxsg',
          roles: ['UserIndex', 'DashboardBase', 'login'], // 前端权限模型使用 如果使用请配置modules/permission-fe.ts使用
        };
      };
      const res = await mockRemoteUserInfo(this.token);

      this.userInfo = res;
    },
    async logout() {
      this.token = '';
      this.userInfo = { ...InitUserInfo };
    },
  },
  persist: {
    afterRestore: () => {
      const permissionStore = usePermissionStore();
      permissionStore.initRoutes();
    },
    key: 'user',
    paths: ['token'],
  },
});
