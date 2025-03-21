import { request } from '@/utils/request';
import { PageParams } from '@/api/model/pageParams';

/**
 * 用户分页查询
 * @param params 
 * @returns 
 */
const page = async (params: PageParams) => {
    return request.post({
        url: '/user/page',
        data: params
    });
}

/**
 * 用户新增
 * @param data 
 * @returns 
 */
const insert = async (data: any) => {
    return request.post({
        url: '/user/insert',
        data
    });
}

/**
 * 更新用户
 * @param data 
 * @returns 
 */
const update = async (data: any) => {
    return request.post({
        url: '/user/update',
        data
    });
}

/**
 * 根据id删除
 * @param id 
 * @returns 
 */
const deleteById = async (id: String) => {
    return request.get({
        url: `/user/delete/${id}`,
    });
}

export { page, insert, update, deleteById };