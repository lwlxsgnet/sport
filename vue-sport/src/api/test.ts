import { request } from '@/utils/request';

export const test = () => {
    return request.get({
        url: '/test',
    });
}