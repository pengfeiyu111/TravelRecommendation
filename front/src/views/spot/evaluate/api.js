import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const getEvaluateOne = (params) => {
    return getRequest('/evaluate/getOne', params)
}
export const getEvaluateList = (params) => {
    return getRequest('/evaluate/getByPage', params)
}
export const getEvaluateCount = (params) => {
    return getRequest('/evaluate/count', params)
}
export const addEvaluate = (params) => {
    return postRequest('/evaluate/insert', params)
}
export const editEvaluate = (params) => {
    return postRequest('/evaluate/update', params)
}
export const addOrEditEvaluate = (params) => {
    return postRequest('/evaluate/insertOrUpdate', params)
}
export const deleteEvaluate = (params) => {
    return postRequest('/evaluate/delByIds', params)
}