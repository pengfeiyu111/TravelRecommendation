import { getRequest,postJsonRequest,getNoAuthRequest,postFlowRequest } from '@/libs/axios';

export const getMyInfoData = (params) => {
    return getRequest('/appletsOaLogin/info', params)
}
export const getAllPositionList = (params) => {
	return getNoAuthRequest('/scenicSpot/getAllOnWeb', params)
}
export const getAllPositionList2 = (params) => {
	return getNoAuthRequest('/scenicSpot/getRecommendListOnWeb', params)
}
export const loginOnWeb = (params) => {
	return getNoAuthRequest('/superUser/loginOnWeb', params)
}
export const reOnWeb = (params) => {
	return getNoAuthRequest('/superUser/reOnWeb', params)
}
export const addLook = (params) => {
    return getRequest('/carLook/addLook', params)
}
export const addOrder = (params) => {
    return getRequest('/carOrder/addOrder', params)
}
export const addSuperOrder = (params) => {
    return getRequest('/carOrder/addSuperOrder', params)
}
export const getAllNewList = (params) => {
	return getNoAuthRequest('/trip/getAllOnWeb', params)
}
export const getAllNewList2 = (params) => {
	return getNoAuthRequest('/deliciousFood/getAllOnWeb', params)
}
export const getAllMessageList = (params) => {
	return getNoAuthRequest('/messageBoard/getAllOnWeb', params)
}
export const getMyInfo = (params) => {
    return getRequest('/superUser/getMyInfo', params)
}
export const setMyInfo = (params) => {
    return getRequest('/superUser/setMyInfo', params)
}
export const getByAllOnUser = (params) => {
    return getRequest('/collection/getByAllOnUser', params)
}
export const getByAllOnUser2 = (params) => {
    return getRequest('/carLook/getByAllOnUser', params)
}
export const getOneCarOnWeb = (params) => {
    return getNoAuthRequest('/scenicSpot/getOneOnWeb', params)
}
export const addMessage = (params) => {
    return getRequest('/messageBoard/addMessageOnMyWeb', params)
}
export const deleteMessage = (params) => {
    return getRequest('/messageBoard/deleteMessageOnMyWeb', params)
}
export const deleteOrder = (params) => {
    return getRequest('/carOrder/delOne', params)
}
export const deleteLook = (params) => {
    return getRequest('/carLook/delOne', params)
}
export const getOneOrder = (params) => {
    return getRequest('/carOrder/getOne', params)
}
export const getOneLook = (params) => {
    return getRequest('/carLook/getOne', params)
}
export const getTopEvaluateOnWeb = (params) => {
    return getNoAuthRequest('/scenicSpot/getTopEvaluateOnWeb', params)
}
export const getTopCollectionOnWeb = (params) => {
    return getNoAuthRequest('/scenicSpot/getTopCollectionOnWeb', params)
}
export const getCollectionStatus = (params) => {
    return getRequest('/collection/getCollectionStatus', params)
}
export const addCollection = (params) => {
    return getRequest('/collection/addCollection', params)
}
export const cancelCollection = (params) => {
    return getRequest('/collection/cancelCollection', params)
}
