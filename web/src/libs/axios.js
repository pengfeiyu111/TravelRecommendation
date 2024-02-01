import axios from 'axios';
import { getStore } from './storage';
let base = '/zwz';
axios.defaults.timeout = 250000;

export const getRequest = (url, params) => {
    let accessToken = getStore('token');
    return axios({
        method: 'get',
        url: `${base}${url}`,
        params: params,
        headers: {
            'accessToken': accessToken
        }
    });
};
export const postRequest = (url, params) => {
    let accessToken = getStore("token");
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: params,
        transformRequest: [function (data) {
            let ret = '';
            for (let it in data) {
                ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&';
            }
            ret = ret.substring(0, ret.length - 1);
            return ret;
        }],
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'accessToken': accessToken
        }
    });
};
export const postJsonRequest = (url, params) => {
    let accessToken = getStore('token');
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: params,
        headers: {
            'accessToken': accessToken,
            'Content-Type': 'application/json',
        }
    });
};
export const postFlowRequest = (url, params) => {
    let userToken = getStore('userToken');
    return axios({
        method: 'post',
        url: `${url}`,
        data: params,
        headers: {
            'eco-auth-token': userToken,
            'Content-Type': 'application/json',
        }
    });
};
export const getNoAuthRequest = (url, params) => {
    return axios({
        method: 'get',
        url: `${base}${url}`,
        params: params
    });
};

export const postNoAuthRequest = (url, params) => {
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: params,
        transformRequest: [function (data) {
            let ret = '';
            for (let it in data) {
                ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&';
            }
            ret = ret.substring(0, ret.length - 1);
            return ret;
        }],
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    });
};