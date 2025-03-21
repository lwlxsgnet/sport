import { request } from '@/utils/request'

/**
 * 账号密码登录
 * @param data
 * @returns
 */
const loginPwd = async (data: any) => {
    return request.post({
        url: '/login/username',
        data
    });
}

/**
 * logout
 */
const logout = async () => {
    return request.get({
        url: '/login/logout'
    });
}

export { loginPwd, logout }