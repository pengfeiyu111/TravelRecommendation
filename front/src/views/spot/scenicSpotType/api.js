import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const getScenicSpotTypeOne = (params) => {
    return getRequest('/scenicSpotType/getOne', params)
}
export const getScenicSpotTypeList = (params) => {
    return getRequest('/scenicSpotType/getByPage', params)
}
export const getScenicSpotTypeCount = (params) => {
    return getRequest('/scenicSpotType/count', params)
}
export const addScenicSpotType = (params) => {
    return postRequest('/scenicSpotType/insert', params)
}
export const editScenicSpotType = (params) => {
    return postRequest('/scenicSpotType/update', params)
}
export const addOrEditScenicSpotType = (params) => {
    return postRequest('/scenicSpotType/insertOrUpdate', params)
}
export const deleteScenicSpotType = (params) => {
    return postRequest('/scenicSpotType/delByIds', params)
}