<template>
<div id="home">
    <div class="body">
        <div id="header" :class="class1">
            <div class="logo">
                <img class="logoImg" src="../assets/langShiLogo.png" alt="" />
            </div>
            <div class="rightBox">
                <div class="white1" @click="toMainPage">首页</div>
                <div class="shu"></div>
                <div class="white1" @click="toZhaoPinPage">景点</div>
                <div class="shu"></div>
                <div class="white1" @click="toMainPage2">景点推荐</div>
                <div class="shu"></div>
                <div class="white1 current">旅游攻略</div>
                <div class="shu"></div>
                <!-- <div class="white1" @click="toMyOrderPage">服务保障</div>
                <div class="shu"></div> -->
                <div class="white1" @click="toMessagePage">景点排名</div>
                <div class="shu1"></div>
                <div class="white">
                    <el-button type="text" @click="loginOut">退出</el-button>
                </div>
            </div>
        </div>

        <el-form :model="form" label-width="120px" style="margin-top: 150px;">
            <el-form-item label="登陆账号">
                <el-input v-model="form.username" readonly />
            </el-form-item>
            <el-form-item label="姓名">
                <el-input v-model="form.nickname" />
            </el-form-item>
            <el-form-item label="性别">
                <el-radio-group v-model="form.sex">
                    <el-radio label="男" />
                    <el-radio label="女" />
                </el-radio-group>
            </el-form-item>
            <el-form-item label="手机号">
                <el-input v-model="form.mobile" />
            </el-form-item>
            <el-form-item label="邮箱">
                <el-input v-model="form.email" />
            </el-form-item>
            <el-form-item label="身份证号">
                <el-input v-model="form.address" />
            </el-form-item>
            <!-- <el-form-item label="个性签名">
                <el-input v-model="form.description" type="textarea" />
            </el-form-item> -->
            <el-form-item>
                <el-button type="primary" @click="onSubmit">确认更新</el-button>
                <el-button @click="getMyInfoFx">取消更改</el-button>
            </el-form-item>
        </el-form>
        <el-divider />
        <div class="mainBox">
            <div class="mainTitle">我的旅游景点收藏 （{{orderList.length}}）</div>
            <el-row :gutter="20" v-for="(item,index) in orderList" :key="index" @click="toCarsItemPage(item)" style="width: 100%;margin-bottom: 10px;">
                <el-col :span="6">
                    <img :src="item.image" style="width:100%" />
                </el-col>
                <el-col :span="18">
                    <el-row :gutter="20" class="spanContext">
                        <span class="spanContextLabel"> 景点名称：</span>{{item.spotName}}
                    </el-row>
                    <el-row :gutter="20" class="spanContext">
                        <span class="spanContextLabel"> 收藏时间：</span>{{item.time}} 次
                    </el-row>
                </el-col>
            </el-row>
            <!-- <div class="box" v-for="(item,index) in orderList" :key="index">
                <span class="name">【{{item.spotName}}】</span>
                <div class="labelBox">
                    <span>收藏时间：{{item.time}}</span>
                    <div class="smallShu"></div>
                </div>
            </div> -->
        </div>
        <el-divider />
        <div class="main">
            <div class="story">
                <div class="heng"></div>
                <span class="title1">旅游图册</span>
                <span class="notes">ByteDancer Story</span>
                <el-carousel :interval="2000" type="card" height="300px" class="lunbo">
                    <el-carousel-item v-for="(item,index) in gunList" :key="index">
                        <img :src="item.src" style="height:300px" alt="" />
                    </el-carousel-item>
                </el-carousel>
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
    loginOnWeb,
    getMyInfo,
    setMyInfo,
    getByAllOnUser
} from "./api.js";
import {
    ElMessage,
    ElMessageBox
} from 'element-plus'
import Cookies from "js-cookie";
export default {
    data() {
        return {
            form: {
                nickname: '',
                username: '',
                mobile: '',
                sex: '',
                address: '',
                description: '',
                email: ''
            },
            orderList: [],
            token: "",
            gunList: [{
                    title: "照片A",
                    src: "./gun1.png"
                },
                {
                    title: "照片B",
                    src: "./gun2.png"
                },
                {
                    title: "照片C",
                    src: "./gun3.png"
                }
            ],
            test: "hello world",
            class1: "header",
            class2: "white",
            class3: "shu",
            class4: "current",
            class4: "corner",
            class5: "corner1"
        };
    },
    components: {},
    methods: {
        init() {
            this.getMyInfoFx();
            this.getByAllOnUserFx();
        },
        toZhaoPinPage() {
            this.$router.push("/position");
        },
        toMain2Page() {
            this.$router.push("/position2");
        },
        toNewsPage() {
            this.$router.push("/news");
        },
        toMessagePage() {
            this.$router.push("/message");
        },
        toMyOrderPage() {
            this.$router.push("/myOrder");
        },
        toMainPage() {
            this.$router.push("/");
        },
        onSubmit() {
            var that = this;
            setMyInfo({
                id: that.form.id,
                nickname: that.form.nickname,
                mobile: that.form.mobile,
                sex: that.form.sex,
                address: that.form.address,
                email: that.form.email
            }).then(res => {
                if (res.data.success) {
                    that.form = res.data.result;
                    ElMessage({
                        type: 'success',
                        message: '更新成功',
                    })
                } else {
                    ElMessage({
                        type: 'error',
                        message: res.data.message,
                    })
                }
            })
        },
        getByAllOnUserFx() {
            var that = this;
            getByAllOnUser().then(res => {
                if (res.data.success) {
                    that.orderList = res.data.result;
                } else {
                    ElMessage({
                        type: 'error',
                        message: '查询我的订单失败  ' + res.data.message,
                    })
                }
            })
        },
        getMyInfoFx() {
            var that = this;
            getMyInfo().then(res => {
                if (res.data.success) {
                    that.form = res.data.result;
                } else {
                    ElMessage({
                        type: 'error',
                        message: res.data.message,
                    })
                }
            })
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
                        .catch((e) => {
                            console.log(e);
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
        loginOut() {
            Cookies.remove("token");
            this.removeStore("token");
            this.token = "";
            this.$router.push("/");
        },
        toCarsItemPage(e) {
            Cookies.set("carId", e.spotId);
            this.setStore("carId", e.spotId);
            this.$router.push("/positionItem");
        },
        classHeader: function () {
            //当前滑下的高度
            var Height = document.documentElement.scrollTop;
            //屏幕的可用高度
            var pinmu = window.screen.height;
            // console.log(Height, pinmu);
            // if (Height > pinmu - 100) {
            this.class1 = "header1";
            this.class2 = "white1";
            this.class3 = "shu1";
            this.class4 = "current1";
            this.class5 = "corner1";
            // } else {
            //     this.class1 = "header";
            //     this.class2 = "white";
            //     this.class3 = "shu";
            //     this.class4 = "current";
            //     this.class5 = "corner";
            // }
        },
    },
    mounted() {
        this.token = Cookies.get("token");
        console.log(this.token);
        this.init();
        this.classHeader();
        window.addEventListener('scroll', this.classHeader)
    },
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
    height: 100vh;
    min-height: 661px;
    box-sizing: border-box;
    background-color: rgb(255, 255, 255);
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

.cornerCircular {
    position: relative;
    width: 50px;
    height: 50px;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 50%;
    margin: 10px 0;
    background-color: #fff;
    box-shadow: 0px 0px 8px #a3a3a3b0;
    overflow: hidden;
}

.cornerCircular:hover {
    overflow: visible;
    color: #325ab4;
}

/* .backTop {
    position: absolute;
    left: -100px;
    font-size: 14px;
    padding: 10px;
    background-color: #4C4F54;
    border-radius: 5px;
    color: #fff;
} */

.code {
    position: absolute;
    left: -150px;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 10px;
    background-color: #fff;
    box-shadow: 1px 1px 5px #c0c0c0;
}

.codeImg {
    width: 100px;
    margin-bottom: 10px;
}

.codeText {
    color: #1f2329;
    font-size: 12px;
}

.submit {
    width: 160px;
    height: 55px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: #fff;
    color: #3370ff;
    border-radius: 60px;
    font-weight: 550;
    margin-top: 60px;
}

.submit:hover {
    background-color: #E1EAFF;
}

.header {
    position: fixed;
    top: 0;
    width: 90%;
    height: 65px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 5%;
}

.video {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100vh;
    min-height: 661px;
    object-fit: cover;
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
    color: #f08732;
    padding: 5px 10px;
    margin-left: 10px;
    font-size: 20px;
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
    color: #fff;
}

.white:hover {
    opacity: 0.6;
}

.current {
    font-size: 16px;
    font-weight: 550;
    color: #fff;
    border-bottom: 2px solid #fff;
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
}

.process {
    width: 100%;
    min-width: 1180px;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 50px;
}

.heng {
    width: 25px;
    height: 5px;
    background-color: #3370ff;
    margin-bottom: 20px;
}

.title1 {
    font-size: 36px;
    margin-bottom: 20px;
}

.notes {
    font-size: 18px;
    color: #999ea7;
    letter-spacing: 8px;
}

.processBox {
    width: 1180px;
    height: 200px;
    display: flex;
    justify-content: space-around;
    align-items: center;
    margin-top: 30px;
}

.step {
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
}

.stepImg {
    height: 50%;
}

.name1 {
    font-size: 24px;
    font-weight: 300;
}

.time {
    font-size: 16px;
    color: #888;
    letter-spacing: 2px;
}

.arrow {
    transform: rotate(90deg);
}

.buttonBox {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 80px 0;
}

.tea {
    padding: 15px 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #fff;
    background-color: #3370ff;
    border: 1px solid #3370ff;
    border-radius: 50px;
    margin: 0 5px;
    font-size: 18px;
}

.tea:hover {
    background-color: #4E83FD;
}

.look {
    padding: 15px 30px;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #3370ff;
    background-color: #fff;
    border: 1px solid #3370ff;
    border-radius: 50px;
    margin: 0 5px;
    font-size: 18px;
}

.look:hover {
    background-color: #E1EAFF;
}

.story {
    width: 100%;
    min-width: 1180px;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 50px;
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

.lunbo {
    width: 1000px;
    margin: 60px 0;
}

.el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
    text-align: center;
}

.el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
}

/* 高度大于100vh的时候加给.header */
.header1 {
    position: fixed;
    top: 0;
    width: 90%;
    height: 65px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 5%;
    background-color: rgba(255, 255, 255, 0.8);
}

/* 高度大于100vh的时候加给右上角的所有项 */
.white1 {
    display: flex;
    align-items: center;
    height: 35px;
    margin: 0 20px;
    font-size: 14px;
    color: #888d94;
}

.shu1 {
    width: 1px;
    height: 10px;
    background-color: #888d94;
    margin: 0 20px;
}

.white1:hover {
    color: #3370ff;
}

/* 高度大于100vh的时候加给右上角的当前项“主页” */
.current1 {
    font-size: 16px;
    font-weight: 550;
    color: #3370ff;
    border-bottom: 2px solid #3370ff;
}

/* 高度大于1800px(暂定)删除.corner的.none */
.corner1 {
    position: fixed;
    right: 35px;
    bottom: 100px;
    display: flex;
    flex-direction: column;
    align-items: center;
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
.spanContext {
    font-size: 17px;
    margin-top: 20px;
}

.spanContextLabel {
    font-weight: 550;
}
</style>
