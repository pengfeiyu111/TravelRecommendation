import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const getScenicSpotOne = (params) => {
    return getRequest('/scenicSpot/getOne', params)
}
export const getScenicSpotList = (params) => {
    return getRequest('/scenicSpot/getByPage', params)
}
export const getScenicSpotCount = (params) => {
    return getRequest('/scenicSpot/count', params)
}
export const addScenicSpot = (params) => {
    return postRequest('/scenicSpot/insert', params)
}
export const editScenicSpot = (params) => {
    return postRequest('/scenicSpot/update', params)
}
export const addOrEditScenicSpot = (params) => {
    return postRequest('/scenicSpot/insertOrUpdate', params)
}
export const deleteScenicSpot = (params) => {
    return postRequest('/scenicSpot/delByIds', params)
}
export const getScenicSpotTypeList = (params) => {
    return getRequest('/scenicSpotType/getAll', params)
}
export const addCollection = (params) => {
    return getRequest('/collection/addCollection', params)
}
export const cancelCollection = (params) => {
    return getRequest('/collection/cancelCollection', params)
}
export const addEvaluate = (params) => {
    return getRequest('/evaluate/addEvaluate', params)
}
export const getTopCollection = (params) => {
    return getRequest('/scenicSpot/getTopCollection', params)
}
export const getTopEvaluate = (params) => {
    return getRequest('/scenicSpot/getTopEvaluate', params)
}