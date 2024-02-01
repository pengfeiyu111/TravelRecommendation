<template>
<div class="mealBody">
    <Card>
        <Row :gutter="16">
            <Form ref="searchForm" :model="searchForm" inline :label-width="40" class="search-form">
                <Form-item label="" :label-width="0">
                    <Button @click="getTopCollectionFx" type="success" ghost>刷新数据</Button>
                </Form-item>
            </Form>
        </Row>
    </Card>
    <Card style="width:100%;">
        <Row class="topBox">
            <div class="top2">
                <span class="title">TOP2</span>
                <div class="name">{{twoItem.title}} - {{twoItem.type}}</div>
                <div class="money">{{twoItem.star}} 分</div>
            </div>
            <div class="top1">
                <span class="title">TOP1</span>
                <div class="name">{{oneItem.title}} - {{oneItem.type}}</div>
                <div class="money">{{oneItem.star}} 分</div>
            </div>
            <div class="top3">
                <span class="title">TOP3</span>
                <div class="name">{{threeItem.title}} - {{threeItem.type}}</div>
                <div class="money">{{threeItem.star}} 分</div>
            </div>
        </Row>
        <Row class="tableBox">
            <div class="template" v-for="(item,index) in wageRankList" :key="index">
                <div class="spanBox level1" :style="{width:((item.star/maxMoney)*50.0 + 50.0 + '%')}">
                    <span class="text">{{item.title}}</span>
                    <span class="text">{{item.type}}</span>
                    <span class="text">{{item.star}} 分</span>
                </div>
            </div>
        </Row>
    </Card>
</div>
</template>

<script>
import {
    getTopEvaluate
} from "./api.js";
export default {
    name: "single-window",
    components: {},
    data() {
        return {
            wageRankList: [],
            maxMoney: 10,
            oneItem: {},
            twoItem: {},
            threeItem: {},
        };
    },
    methods: {
        init() {
            this.getTopEvaluateFx();
        },
        getTopEvaluateFx() {
            var that = this;
            that.wageRankList = [];
            that.oneItem = {};
            that.twoItem = {};
            that.threeItem = {};
            getTopEvaluate().then(res => {
                console.log(res);
                if (res.success) {
                    that.threeItem = res.result[2];
                    that.twoItem = res.result[1];
                    that.oneItem = res.result[0];
                    that.maxMoney = res.result[3].star;
                    var arr = [];
                    for(var i = 3; i < res.result.length; i ++) {
                        arr.push(res.result[i]);
                    }
                    that.wageRankList = arr;
                }
            })
        }
    },
    mounted() {
        this.init();
    },
};
</script>

<style lang="less" scoped>
.mealBody {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    .topBox {
        display: flex;
        height: 240px;
        justify-content: center;
        margin-bottom: 35px;

        .top2 {
            width: 20%;
            height: 190px;
            display: flex;
            flex-direction: column;
            // border: 1px solid #FFFF00;
            background-image: linear-gradient(135deg, #CDC8B1, #EEEEE0, #E0EEE0);
            margin-top: 25px;
        }

        .top1 {
            width: 20%;
            height: 190px;
            display: flex;
            flex-direction: column;
            // border: 1px solid #FF6A6A;
            background-image: linear-gradient(135deg, #FFD700, #FFC125, #FFD700);
            margin: 0 5%;
        }

        .top3 {
            width: 20%;
            height: 190px;
            display: flex;
            flex-direction: column;
            // border: 1px solid #FF1493;
            background-image: linear-gradient(135deg, #EE9A00, #EE9A00, #CD8500);
            margin-top: 50px;
        }

        .title {
            text-align: center;
            font-size: 40px;
            margin: 10px 0;
            font-family: "华文行楷";
        }

        .name {
            text-align: center;
            font-size: 26px;
            margin: 10px 0;
        }

        .money {
            text-align: center;
            font-size: 24px;
            margin: 10px 0;
        }
    }

    .tableBox {
        display: flex;
        flex-direction: column;
        width: 95%;
        // margin: -150px auto 0 auto;

        .template {
            height: 50px;

            .spanBox {
                display: flex;
                flex-direction: row-reverse;
                margin: 10px 0;

                .text {
                    margin-right: 50px;
                    font-size: 22px;
                }
            }
        }

    }
}

.level1 {
    box-shadow: 5px 2px 5px #065383;
    background-image: linear-gradient(100deg, #1E90FF, #87CEFA);
}

.level2 {
    box-shadow: 5px 2px 5px #066e06;
    background-image: linear-gradient(100deg, #00c060, #9AFF9A);
}

.level3 {
    box-shadow: 5px 2px 5px #6b4e03;
    background-image: linear-gradient(100deg, #FFA500, #FFC125);
}

.level4 {
    background-color: #515a6e;
}
</style>
