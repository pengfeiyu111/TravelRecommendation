<template>
<div id="home">
    <div id="header" class="header">
        <div class="logo">
            <img class="logoImg" src="../assets/langShiLogo.png" alt="" />
            <div class="logoText">海南旅游景点推荐系统</div>
        </div>
        <div class="rightBox">
            <div class="white" @click="toMainPage">首页</div>
            <div class="shu"></div>
            <div class="white current">景点</div>
            <div class="shu"></div>
            <div class="white" @click="toMain2Page">景点推荐</div>
            <div class="shu"></div>
            <div class="white" @click="toNewsPage">旅游攻略</div>
            <div class="shu"></div>
            <!-- <div class="white" v-show="token!==undefined" @click="toMyOrderPage">服务保障</div>
            <div class="shu"></div> -->
            <div class="white" @click="toMessagePage">景点排名</div>
            <div class="shu"></div>
            <div class="white">
                <el-button v-show="token===undefined" type="text" @click="openLoginBox">登录</el-button>
                <el-button v-show="token===undefined" type="text" @click="openReBox">注册</el-button>
                <el-button v-show="token!==undefined" type="text" @click="toMyOwnPage">个人中心</el-button>
            </div>
        </div>
    </div>
    <header>
        <div class="imgBox">
            <img class="headerImg" src="../assets/longLogo.png" alt="">
        </div>
    </header>
    <div class="body">
        <div class="main">
            <div class="mainBox">
                <div class="mainTitle">旅游景点合同签订页</div>
                <div class="box">
                    <span class="name">【{{item.brand}}】{{item.money}}万</span>
                    <div class="labelBox">
                        <el-button type="danger" @click="addSuperOrderFx(item)">签订合同</el-button>
                        <el-button type="success" @click="toBackPage">取消并返回</el-button>
                    </div>
                    <div class="labelBox">
                        <span>甲方：{{item.sellName}}</span>
                    </div>
                    <div class="labelBox">
                        <span>乙方：{{myData.nickname}}</span>
                    </div>
                    <div class="labelBox">
                        <span>卖家：{{item.sellName}}</span>
                    </div>
                    <div class="labelBox">
                        <span>上架日期：{{item.date}}</span>
                    </div>
                    <div class="labelBox">
                        <span>联系方式：{{item.sellMobile}}</span>
                    </div>
                    <div class="labelBox">
                        <span>已使用：{{item.year}} 年</span>
                    </div>
                    <div class="labelBox">
                        <span>出售价格：{{item.money}} 万</span>
                    </div>
                    <div class="labelBox">
                        <span>推荐理由：{{item.license}} </span>
                    </div>
                    <div class="labelBox">
                        <span>商品类型：{{item.idCard}} </span>
                    </div>
                    <div style="display:inline">
                        <img src="./sign.png" style="width:88%" />
                    </div>
                </div>
            </div>
        </div>
        <div class="footer">
            <div class="footerBox">
                <div class="footerTeal">
                    <div class="logoBox">
                        <img class="footerLogo" src="../assets/langShiLogo1.png" alt="" />
                        <div class="logoTitle">旅游景点</div>
                    </div>
                    <span class="smallTitle">XXXX</span>
                    <span class="smallTitle">Copyright © 2020 - 至今 ICP备案 浙ICP备1xxxxxxx号-1</span>
                </div>
                <div class="lie"></div>
                <div class="footerAway">
                    <span class="text1">联系我们</span>
                    <span class="text2">关于我们</span>
                    <span class="text2">意见反馈</span>
                </div>
                <div class="lie"></div>
                <div class="footerAway">
                    <span class="text1">相关网站</span>
                    <span class="text2">XXX</span>
                    <span class="text2">旅游景点官网</span>
                </div>
                <div class="lie"></div>
                <div class="footerWait">
                    <span class="text1">实时旅游景点信息，关注我们</span>
                    <div class="yuanyuan">
                        <div class="yuan"></div>
                        <div class="yuan"></div>
                        <div class="yuan"></div>
                        <div class="yuan"></div>
                        <div class="yuan"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<script>
import {
    getAllPositionList,
    loginOnWeb,
    addLook,
    addOrder,
    reOnWeb,
    getOneCarOnWeb,
    getMyInfo,
    addSuperOrder
} from "./api.js";
import {
    ElMessage,
    ElMessageBox
} from 'element-plus'
import Cookies from "js-cookie";
export default {
    data() {
        return {
            token: "",
            test: "hello world",
            class1: "none",
            positionList: [],
            selectPositionStr: "",
            loginUserName: "",
            loginPassword: "",
            item: {
                brand: "",
                money: "",
                sellName: "",
                date: "",
                sellMobile: "",
                label: "",
                year: "",
                canOrder: false
            },
            myData: {
                nickname: "加载中"
            }
        };
    },
    components: {},
    methods: {
        init() {
            this.getMyInfoFx();
            this.getOneCarOnWebFx();
            // this.getAllPositionListFx();
        },
        getMyInfoFx() {
            var that = this;
            getMyInfo().then(res => {
                if (res.data.success) {
                    that.myData = res.data.result;
                }
            })
        },
        getOneCarOnWebFx() {
            var that = this;
            getOneCarOnWeb({
                id: that.getStore("carId")
            }).then(res => {
                console.log(res);
                if (res.data.success) {
                    that.item = res.data.result;
                } else {
                    ElMessage({
                        type: 'error',
                        message: res.data.message,
                    })
                }
            })
        },
        addSuperOrderFx(e) {
            var that = this;
            ElMessageBox.prompt('请输入您要签订合同的备注', '签订合同', {
                    confirmButtonText: '确认签订',
                    cancelButtonText: '取消',
                })
                .then(({
                    value
                }) => {
                    addSuperOrder({
                        carId: e.id
                    }).then(res => {
                        if (res.data.success) {
                            ElMessage({
                                type: 'success',
                                message: '签订成功成功',
                            })
                            that.$router.go(-1);
                        } else {
                            ElMessage({
                                type: 'error',
                                message: res.data.message,
                            })
                        }
                    })
                })
                .catch(() => {
                    ElMessage({
                        type: 'info',
                        message: 'Input canceled',
                    })
                });
        },
        addOrderFx(e) {
            var that = this;
            ElMessageBox.prompt('支付定金后一周不付款将取消预定资格', '请输入您的预定的定金', {
                    confirmButtonText: '预约',
                    cancelButtonText: '取消',
                })
                .then(({
                    value
                }) => {
                    addOrder({
                        carId: e.id
                    }).then(res => {
                        if (res.data.success) {
                            ElMessage({
                                type: 'success',
                                message: '预定成功',
                            })
                        } else {
                            ElMessage({
                                type: 'error',
                                message: res.data.message,
                            })
                        }
                    })
                })
                .catch(() => {
                    ElMessage({
                        type: 'info',
                        message: 'Input canceled',
                    })
                });
        },
        openLoginBox() {
            var that = this;
            ElMessageBox.prompt('请输入您的账号', '用户登陆', {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                })
                .then(({
                    value
                }) => {
                    that.loginUserName = value;
                    ElMessageBox.prompt('请输入您的密码', '用户登陆', {
                            confirmButtonText: '登陆',
                            cancelButtonText: '取消',
                        })
                        .then(({
                            value
                        }) => {
                            that.loginPassword = value;
                            loginOnWeb({
                                userName: that.loginUserName,
                                password: that.loginPassword
                            }).then(res => {
                                console.log(res);
                                if (res.data.success) {
                                    Cookies.set("token", res.data.result);
                                    that.setStore("token", res.data.result);
                                    that.token = res.data.result;
                                    ElMessage({
                                        type: 'success',
                                        message: '登陆成功',
                                    })
                                } else {
                                    ElMessage({
                                        type: 'error',
                                        message: res.data.message,
                                    })
                                }
                            })

                        })
                        .catch(() => {
                            ElMessage({
                                type: 'info',
                                message: 'Input canceled',
                            })
                        });
                })
                .catch(() => {
                    ElMessage({
                        type: 'info',
                        message: 'Input canceled',
                    })
                });
        },

        openReBox() {
            var that = this;
            ElMessageBox.prompt('请输入您的注册账号', '用户注册', {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                })
                .then(({
                    value
                }) => {
                    that.loginUserName = value;
                    ElMessageBox.prompt('请输入您的密码', '用户注册', {
                            confirmButtonText: '确认',
                            cancelButtonText: '取消',
                        })
                        .then(({
                            value
                        }) => {
                            that.loginPassword = value;

                            ElMessageBox.prompt('请输入您的手机号', '用户注册', {
                                    confirmButtonText: '确认注册',
                                    cancelButtonText: '取消',
                                })
                                .then(({
                                    value
                                }) => {
                                    that.loginMobile = value;

                                    reOnWeb({
                                        userName: that.loginUserName,
                                        password: that.loginPassword,
                                        mobile: that.loginMobile
                                    }).then(res => {
                                        console.log(res);
                                        if (res.data.success) {
                                            Cookies.set("token", res.data.result);
                                            that.setStore("token", res.data.result);
                                            that.token = res.data.result;
                                            ElMessage({
                                                type: 'success',
                                                message: '注册成功',
                                            })
                                        } else {
                                            ElMessage({
                                                type: 'error',
                                                message: res.data.message,
                                            })
                                        }
                                    })

                                }).catch((e) => {
                                    console.log(e);
                                    ElMessage({
                                        type: 'info',
                                        message: '注册手机号输入错误',
                                    })
                                });
                        })
                        .catch((e) => {
                            console.log(e);
                            ElMessage({
                                type: 'info',
                                message: '注册密码输入错误',
                            })
                        });
                })
                .catch(() => {
                    ElMessage({
                        type: 'info',
                        message: '注册账号输入错误',
                    })
                });
        },
        selectPositionByTitleFx() {
            this.selectPositionStr = document.getElementById("zwzSearchPositionInput").value;
            this.getAllPositionListFx();
        },
        getAllPositionListFx() {
            var that = this;
            getAllPositionList({
                title: that.selectPositionStr
            }).then(res => {
                console.log(res);
                if (res.data.success) {
                    that.positionList = res.data.result;
                }
            })
        },
        toMainPage() {
            this.$router.push("/");
        },
        toMain2Page() {
            this.$router.push("/position2");
        },
        toBackPage() {
            this.$router.go(-1);
        },
        toMessagePage() {
            this.$router.push("/message");
        },
        toNewsPage() {
            this.$router.push("/news");
        },
        toMyOwnPage() {
            this.$router.push("/myOwn");
        },
        toCarListPage() {
            this.$router.push("/position");
        },
        toMyOrderPage() {
            this.$router.push("/myOrder");
        },
        classHeader: function () {
            //当前滑下的高度
            var Height = document.documentElement.scrollTop;
            //屏幕的可用高度
            var pinmu = window.screen.height;
            // console.log(Height, pinmu);
            if (Height > 420) {
                this.class1 = "fixed";
            } else {
                this.class1 = "none";
            }
        },
    },
    mounted() {
        this.token = Cookies.get("token");
        this.init();
        this.classHeader();
        window.addEventListener('scroll', this.classHeader)
    },
    onShow() {
        console.log("onShow");
    }
};
</script>

<style scoped>
#home {
    display: flex;
    flex-direction: column;
}

header {
    position: relative;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    box-sizing: border-box;
}

.imgBox {
    width: 100%;
    min-width: 1000px;
    overflow: hidden;
    display: flex;
    justify-content: center;
}

.headerImg {
    width: 100%;
    min-width: 1000px;
}

.search {
    width: 800px;
    height: 60px;
    position: absolute;
    bottom: -30px;
    display: flex;
}

.searchInput {
    width: 650px;
    height: 60px;
    padding: 0;
    border: 1px solid #fff;
    border-right: none;
    outline: none;
    text-indent: 50px;
    border-top-left-radius: 50px;
    border-bottom-left-radius: 50px;
    font-size: 16px;
    line-height: 50px;
    font-weight: 450;
    box-sizing: border-box;
    box-shadow: -5px 20px 30px #f0f0f0;
}

.searchInput::-webkit-input-placeholder {
    color: #C3C6CB;
}

.searchInput:hover {
    border: 1px solid #3370FF;
    border-right: none;
    transition: 0.6s;
}

.searchBt {
    width: 150px;
    height: 60px;
    padding: 0;
    border: 0;
    border: 1px solid #3370FF;
    border-left: none;
    border-top-right-radius: 50px;
    border-bottom-right-radius: 50px;
    background-color: #3370FF;
    color: #fff;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 18px;
    font-weight: 450;
}

.searchBt:hover {
    background-color: #82A7FC;
    border: 1px solid #82A7FC;
}

.fixed {
    width: 100%;
    position: fixed;
    top: 0;
    background-color: #fff;
    z-index: 3000;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 30px 0;
    box-shadow: 1px 5px 10px #f8f8f8;
}

.headerBox {
    position: absolute;
    top: 0;
    width: 100%;
    height: 100vh;
    min-width: 1180px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.corner {
    position: fixed;
    right: 35px;
    bottom: 100px;
    display: none;
    flex-direction: column;
    align-items: center;
}

.header {
    width: 90%;
    height: 65px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 5%;
}

.logo {
    height: 45px;
    min-width: 150px;
    display: flex;
    align-items: center;
}

.logoImg {
    height: 100%;
}

.logoText {
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 5px;
    background-color: #f0f4ff;
    color: #325ab4;
    padding: 5px 10px;
    margin-left: 10px;
}

.rightBox {
    height: 45px;
    min-width: 450px;
    display: flex;
    align-items: center;
}

.white {
    display: flex;
    align-items: center;
    height: 35px;
    margin: 0 20px;
    font-size: 14px;
    color: #888d94;
}

.white:hover {
    opacity: 0.6;
}

.current {
    font-size: 16px;
    font-weight: 550;
    color: #3370ff;
    border-bottom: 2px solid #3370ff;
}

.shu {
    width: 1px;
    height: 10px;
    background-color: #bbbfc4;
    margin: 0 20px;
}

.main {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    min-width: 1180px;
    margin: 60px 0;
    background-color: #fff;
}

.mainBox {
    width: 1180px;
    padding: 0 130px;
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    box-sizing: border-box;
}

.mainTitle {
    font-size: 30px;
    font-weight: 550;
    margin-bottom: 30px;
    margin-left: 25px;
}

.box {
    width: 100%;
    background-color: #fff;
    display: flex;
    flex-direction: column;
    padding: 20px 15px;
    box-sizing: border-box;
}

.box:hover {
    box-shadow: 1px 1px 10px 10px #f1f1f1;
    z-index: 900;
}

.name {
    text-align: left;
    font-size: 22px;
    font-weight: 550;
}

.labelBox {
    display: flex;
    font-size: 16px;
    align-items: center;
    color: #646A73;
    margin: 10px 0 20px;
}

.smallShu {
    width: 1px;
    height: 10px;
    background-color: #BBBFC4;
    margin: 0 5px;
}

.area {
    border-style: none;
    background: transparent;
    resize: none;
    overflow: hidden;
    padding: 0;
    color: #8F959E;
    line-height: 22px;
    text-align: left;
}

.footer {
    height: 230px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #1f2329;
}

.footerBox {
    min-width: 1180px;
    display: flex;
    align-items: center;
}

.footerTeal {
    display: flex;
    flex-direction: column;
}

.smallTitle {
    color: #7b7e81;
    font-size: 14px;
    text-align: left;
    margin: 5px 0;
}

.text1 {
    color: #7b7e81;
    font-size: 14px;
    text-align: left;
    margin: 5px 0 15px;
}

.lie {
    width: 1px;
    height: 80px;
    background-color: #34373b;
    margin: auto 75px;
}

.logoBox {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
}

.logoTitle {
    display: flex;
    align-items: center;
    padding: 5px 10px;
    background-color: #ffffff;
    border-radius: 5px;
    color: #1f2329;
    font-size: 12px;
    font-weight: 550;
}

.footerLogo {
    height: 40px;
    margin-right: 15px;
}

.footerAway {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.text2 {
    color: #fff;
    font-size: 14px;
    text-align: left;
    margin: 5px 0;
}

.text2:hover {
    color: #7b7e81;
    transition: 0.5s;
}

.footerWait {
    display: flex;
    flex-direction: column;
}

.yuanyuan {
    display: flex;
    margin: 10px 0;
}

.yuan {
    width: 35px;
    height: 35px;
    border-radius: 50px;
    /* background-color: #2be27e; */
    margin-right: 15px;
}

.none {
    display: none;
}
</style>
