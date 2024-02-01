import { getRequest, postRequest, putRequest, postBodyRequest, getNoAuthRequest, postNoAuthRequest } from '@/libs/axios';

export const getTripOne = (params) => {
    return getRequest('/trip/getOne', params)
}
export const getTripList = (params) => {
    return getRequest('/trip/getByPage', params)
}
export const getTripCount = (params) => {
    return getRequest('/trip/count', params)
}
export const addTrip = (params) => {
    return postRequest('/trip/insert', params)
}
export const editTrip = (params) => {
    return postRequest('/trip/update', params)
}
export const addOrEditTrip = (params) => {
    return postRequest('/trip/insertOrUpdate', params)
}
export const deleteTrip = (params) => {
    return postRequest('/trip/delByIds', params)
}
export const getScenicSpotList = (params) => {
    return getRequest('/scenicSpot/getAll', params)
}