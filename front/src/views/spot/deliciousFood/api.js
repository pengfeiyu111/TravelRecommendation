import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const getDeliciousFoodOne = (params) => {
    return getRequest('/deliciousFood/getOne', params)
}
export const getDeliciousFoodList = (params) => {
    return getRequest('/deliciousFood/getByPage', params)
}
export const getDeliciousFoodCount = (params) => {
    return getRequest('/deliciousFood/count', params)
}
export const addDeliciousFood = (params) => {
    return postRequest('/deliciousFood/insert', params)
}
export const editDeliciousFood = (params) => {
    return postRequest('/deliciousFood/update', params)
}
export const addOrEditDeliciousFood = (params) => {
    return postRequest('/deliciousFood/insertOrUpdate', params)
}
export const deleteDeliciousFood = (params) => {
    return postRequest('/deliciousFood/delByIds', params)
}
export const getScenicSpotList = (params) => {
    return getRequest('/scenicSpot/getAll', params)
}