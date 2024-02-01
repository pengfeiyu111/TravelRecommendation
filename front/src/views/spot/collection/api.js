import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const getCollectionOne = (params) => {
    return getRequest('/collection/getOne', params)
}
export const getCollectionList = (params) => {
    return getRequest('/collection/getByPage', params)
}
export const getCollectionCount = (params) => {
    return getRequest('/collection/count', params)
}
export const addCollection = (params) => {
    return postRequest('/collection/insert', params)
}
export const editCollection = (params) => {
    return postRequest('/collection/update', params)
}
export const addOrEditCollection = (params) => {
    return postRequest('/collection/insertOrUpdate', params)
}
export const deleteCollection = (params) => {
    return postRequest('/collection/delByIds', params)
}